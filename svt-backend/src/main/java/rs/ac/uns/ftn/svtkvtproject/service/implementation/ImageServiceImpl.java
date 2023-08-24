package rs.ac.uns.ftn.svtkvtproject.service.implementation;

import rs.ac.uns.ftn.svtkvtproject.model.dto.ImageDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Image;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import rs.ac.uns.ftn.svtkvtproject.repository.ImageRepository;
import rs.ac.uns.ftn.svtkvtproject.service.ImageService;
//import rs.ac.uns.ftn.svtkvtproject.service.PostService;
import rs.ac.uns.ftn.svtkvtproject.service.PostService;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private ImageRepository imageRepository;

    private PostService postService;

    @Autowired
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    private static final Logger logger = LogManager.getLogger(ImageServiceImpl.class);

    @Override
    public Image findById(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if (!image.isEmpty())
            return image.get();
        logger.error("Repository search for image with id: " + id + " returned null");
        return null;
    }

    @Override
    public List<Image> findImagesForPost(Long id) {
        Optional<List<Image>> images = imageRepository.findImagesForPost(id);
        if (!images.isEmpty())
            return images.get();
        logger.error("Repository search for images for post with id: " + id + " returned null");
        return null;
    }

    @Override
    public Image findProfileImageForUser(Long userId) {
        Optional<Image> image = imageRepository.findProfileImageForUser(userId);
        if (!image.isEmpty())
            return image.get();
        logger.error("Repository search for profile image for user with id: " + userId + " returned null");
        return null;
    }

    @Override
    public Image createImage(ImageDTO imageDTO) {
        Optional<Image> image = imageRepository.findById(imageDTO.getId());

        if (image.isPresent()) {
            logger.error("Image with id: " + imageDTO.getId() + " already exists in repository");
            return null;
        }

        Image newImage = new Image();
        newImage.setPath(imageDTO.getPath());

        if (imageDTO.getBelongsToPostId() != null) {
            Post post = postService.findById(imageDTO.getBelongsToPostId());
            newImage.setBelongsToPost(post);
        }

        if (imageDTO.getBelongsToUserId() != null) {
            User user = userService.findById(imageDTO.getBelongsToUserId());
            newImage.setBelongsToUser(user);
        }

        newImage.setDeleted(false);
        newImage = imageRepository.save(newImage);

        return newImage;
    }

    @Override
    public Image updateImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Integer deleteImage(Long id) {
        return imageRepository.deleteImageById(id);
    }

    @Override
    public Integer deletePostImages(Long postId) {
        return imageRepository.deleteImagesByBelongsToPostId(postId);
    }
}
