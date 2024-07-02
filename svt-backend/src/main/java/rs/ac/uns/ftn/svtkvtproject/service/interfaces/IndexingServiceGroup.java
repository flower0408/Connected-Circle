package rs.ac.uns.ftn.svtkvtproject.service.interfaces;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.GroupDocument;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Group;

@Service
public interface IndexingServiceGroup {
    GroupDocument indexDocument(Group group, MultipartFile documentFile);
}
