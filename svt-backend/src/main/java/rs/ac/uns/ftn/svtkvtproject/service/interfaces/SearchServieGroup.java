package rs.ac.uns.ftn.svtkvtproject.service.interfaces;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.GroupDocument;

import java.util.List;

@Service
public interface SearchServieGroup {

    List<GroupDocument> searchGroupsByName(String name);
    List<GroupDocument> searchGroupsByDescription(String description);
    List<GroupDocument> searchGroupsByPDFContent(String content);
}
