package rs.ac.uns.ftn.svtkvtproject.service;

import rs.ac.uns.ftn.svtkvtproject.model.dto.ImageDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Image;

import java.util.List;

public interface ImageService {

    Image findById(Long id);

    List<Image> findImagesForPost(Long id);

    Image findProfileImageForUser(Long userId);

    Image createImage(ImageDTO imageDTO);

    Image updateImage(Image image);

    Integer deleteImage(Long id);

    Integer deletePostImages(Long postId);
}
