package rs.ac.uns.ftn.svtkvtproject.service.interfaces;

import io.minio.GetObjectResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileServiceMinio {

    String store(MultipartFile file, String serverFilename);

    void delete(String serverFilename);

    GetObjectResponse loadAsResource(String serverFilename);

}
