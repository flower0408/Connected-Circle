package rs.ac.uns.ftn.svtkvtproject.service.interfaces;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.GroupDocument;
import rs.ac.uns.ftn.svtkvtproject.model.dto.SearchGroupByRangeOfPosts;

import java.util.List;

@Service
public interface SearchServieGroup {

    List<GroupDocument> searchGroupsByName(String name);
    List<GroupDocument> searchGroupsByDescription(String description);
    List<GroupDocument> searchGroupsByPDFContent(String content);
    List<GroupDocument> searchGroupsByPosts(SearchGroupByRangeOfPosts data);
    //List<GroupDocument> searchGroupsCombined(String name, String description, String pdfContent, Boolean useAndOperator);
    List<GroupDocument> searchGroupsBooleanQuery(String name, String description, String pdfContent,  String operation);
}
