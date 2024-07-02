package rs.ac.uns.ftn.svtkvtproject.service;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Image;

public interface FileService {

    Image uploadImage(MultipartFile file);

    Resource getImage(String fileName);

}
