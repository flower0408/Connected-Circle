package rs.ac.uns.ftn.svtkvtproject.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.PostDocument;
import rs.ac.uns.ftn.svtkvtproject.elasticrepository.PostDocumentRepository;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;
import rs.ac.uns.ftn.svtkvtproject.repository.PostRepository;
import rs.ac.uns.ftn.svtkvtproject.service.interfaces.FileServiceMinio;
import rs.ac.uns.ftn.svtkvtproject.service.interfaces.IndexingService;
import rs.ac.uns.ftn.svtkvtproject.exceptionhandling.exception.LoadingException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService {

    private final PostDocumentRepository dummyIndexRepository;

    private final PostRepository dummyRepository;

    private final FileServiceMinio fileServiceMinio;

    private final LanguageDetector languageDetector;


    @Override
    @Transactional
    public String indexDocument(MultipartFile documentFile) {
        var newEntity = new Post();
        var newIndex = new PostDocument();

        var documentContent = extractDocumentContent(documentFile);
        if (detectLanguage(documentContent).equals("SR")) {
            newIndex.setContent(documentContent);
        } else {
            newIndex.setContent(documentContent);
        }

        var serverFilename = fileServiceMinio.store(documentFile, UUID.randomUUID().toString());
        newIndex.setServerFilename(serverFilename);

        var savedEntity = dummyRepository.save(newEntity);

        dummyIndexRepository.save(newIndex);

        return serverFilename;
    }
    /*
    *  public String indexDocument(MultipartFile documentFile, String content) {
        Post newEntity = new Post();
        PostDocument newIndex = new PostDocument();

        String serverFilename = fileService.store(documentFile, UUID.randomUUID().toString());

        newEntity.setContent(content);
        newEntity.setServerFilename(serverFilename);

        String extractedContent = extractDocumentContent(documentFile);
        newIndex.setContent(content + " " + extractedContent); // Combine post content and PDF content
        newIndex.setServerFilename(serverFilename);

        Post savedEntity = postRepository.save(newEntity);

        newIndex.setId(savedEntity.getId().toString());
        postDocumentRepository.save(newIndex);

        return serverFilename;
    }*/

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

}