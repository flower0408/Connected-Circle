package rs.ac.uns.ftn.svtkvtproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svtkvtproject.dto.PostDocumentFileDTO;
import rs.ac.uns.ftn.svtkvtproject.dto.PostDocumentFileResponseDTO;
import rs.ac.uns.ftn.svtkvtproject.service.interfaces.IndexingService;

@RestController
@RequestMapping("/api/index")
@RequiredArgsConstructor
public class IndexController {

    private final IndexingService indexingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDocumentFileResponseDTO addDocumentFile(
        @ModelAttribute PostDocumentFileDTO documentFile) {
        var serverFilename = indexingService.indexDocument(documentFile.file());
        return new PostDocumentFileResponseDTO(serverFilename);
    }
}
