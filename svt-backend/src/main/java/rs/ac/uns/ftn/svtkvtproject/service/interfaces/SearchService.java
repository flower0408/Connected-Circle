package rs.ac.uns.ftn.svtkvtproject.service.interfaces;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.PostDocument;

import java.util.List;

@Service
public interface SearchService {

    Page<PostDocument> simpleSearch(List<String> keywords, Pageable pageable);

    Page<PostDocument> advancedSearch(List<String> expression, Pageable pageable);
}
