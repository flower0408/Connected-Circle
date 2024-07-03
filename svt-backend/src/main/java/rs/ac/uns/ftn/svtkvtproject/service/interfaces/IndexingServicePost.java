package rs.ac.uns.ftn.svtkvtproject.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.PostDocument;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;

public interface IndexingServicePost {

    PostDocument indexDocument(Post post, MultipartFile documentFile);
}
