package rs.ac.uns.ftn.svtkvtproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.GroupDocument;
import rs.ac.uns.ftn.svtkvtproject.elasticrepository.GroupDocumentRepository;
import rs.ac.uns.ftn.svtkvtproject.exceptionhandling.exception.LoadingException;
import rs.ac.uns.ftn.svtkvtproject.exceptionhandling.exception.StorageException;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Group;
import rs.ac.uns.ftn.svtkvtproject.service.interfaces.FileServiceMinio;
import rs.ac.uns.ftn.svtkvtproject.service.interfaces.IndexingServiceGroup;
import org.apache.tika.language.detect.LanguageDetector;

import jakarta.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IndexingServiceGroupImpl implements IndexingServiceGroup {
    private final GroupDocumentRepository groupDocumentRepository;
    private final FileServiceMinio fileServiceMinio;
    private final LanguageDetector languageDetector;


    @Override
    @Transactional
    public GroupDocument indexDocument(Group group, MultipartFile documentFile) {
        var newGroupDocument = new GroupDocument();

        var title = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        newGroupDocument.setName(group.getName());

        var documentContent = extractDocumentContent(documentFile);
        if (detectLanguage(documentContent).equals("SR")) {
            newGroupDocument.setContentSr(documentContent);
        } else {
            newGroupDocument.setContentEn(documentContent);
        }
        newGroupDocument.setDescription(group.getDescription());


        var serverFilename = fileServiceMinio.store(documentFile, UUID.randomUUID().toString());
        newGroupDocument.setServerFilename(serverFilename);

        newGroupDocument.setNumPosts(0);
        newGroupDocument.setDatabaseId(Math.toIntExact(group.getId()));
        groupDocumentRepository.save(newGroupDocument);
        return newGroupDocument;
    }

    private String extractDocumentContent(MultipartFile multipartPdfFile) {
        String documentContent;
        try (var pdfFile = multipartPdfFile.getInputStream()) {
            var pdDocument = PDDocument.load(pdfFile);
            var textStripper = new PDFTextStripper();
            documentContent = textStripper.getText(pdDocument);
            pdDocument.close();
        } catch (IOException e) {
            throw new LoadingException("Error while trying to load PDF file content.");
        }

        return documentContent;
    }

    private String detectLanguage(String text) {
        var detectedLanguage = languageDetector.detect(text).getLanguage().toUpperCase();
        if (detectedLanguage.equals("HR")) {
            detectedLanguage = "SR";
        }

        return detectedLanguage;
    }

    private String detectMimeType(MultipartFile file) {
        var contentAnalyzer = new Tika();

        String trueMimeType;
        String specifiedMimeType;
        try {
            trueMimeType = contentAnalyzer.detect(file.getBytes());
            specifiedMimeType =
                    Files.probeContentType(Path.of(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (IOException e) {
            throw new StorageException("Failed to detect mime type for file.");
        }

        if (!trueMimeType.equals(specifiedMimeType) &&
                !(trueMimeType.contains("zip") && specifiedMimeType.contains("zip"))) {
            throw new StorageException("True mime type is different from specified one, aborting.");
        }

        return trueMimeType;
    }



}
