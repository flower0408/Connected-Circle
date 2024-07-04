package rs.ac.uns.ftn.svtkvtproject.service.interfaces;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.GroupDocument;
import rs.ac.uns.ftn.svtkvtproject.model.dto.SearchGroupByAvgNumberOfLikes;
import rs.ac.uns.ftn.svtkvtproject.model.dto.SearchGroupByRangeOfPosts;

import java.util.List;

@Service
public interface SearchServieGroup {

    List<GroupDocument> searchGroupsByName(String name, boolean usePhraseQuery, boolean useFuzzyQuery);
    List<GroupDocument> searchGroupsByDescription(String description, boolean usePhraseQuery, boolean useFuzzyQuery);
    List<GroupDocument> searchGroupsByPDFContent(String content, boolean usePhraseQuery, boolean useFuzzyQuery);
    List<GroupDocument> searchGroupsByPosts(SearchGroupByRangeOfPosts data);
    List<GroupDocument> searchGroupsBooleanQuery(String name, String description, String pdfContent,  String operation, boolean usePhraseQuery, boolean useFuzzyQuery);
    List<GroupDocument> searchGroupsByAvgLikes(SearchGroupByAvgNumberOfLikes data);
}
