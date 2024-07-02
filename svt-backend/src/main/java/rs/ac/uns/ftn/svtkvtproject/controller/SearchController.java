package rs.ac.uns.ftn.svtkvtproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.svtkvtproject.dto.SearchQueryDTO;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.PostDocument;
import rs.ac.uns.ftn.svtkvtproject.service.interfaces.SearchService;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/simple")
    public Page<PostDocument> simpleSearch(@RequestBody SearchQueryDTO simpleSearchQuery,
                                           Pageable pageable) {
        return searchService.simpleSearch(simpleSearchQuery.keywords(), pageable);
    }

    @PostMapping("/advanced")
    public Page<PostDocument> advancedSearch(@RequestBody SearchQueryDTO advancedSearchQuery,
                                           Pageable pageable) {
        return searchService.advancedSearch(advancedSearchQuery.keywords(), pageable);
    }
}
