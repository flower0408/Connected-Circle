package rs.ac.uns.ftn.svtkvtproject.service;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtkvtproject.model.dto.PostDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {

    Post findById(Long id);

    List<Post> findByCreationDate(LocalDateTime creationDate);

    List<Post> findByPostedBy(User user);

    List<Post> findAll();

    List<Post> findHomepagePosts(Long userId);

    List<Post> findHomepagePostsSortedAsc(Long userId);
    List<Post> findHomepagePostsSortedDesc(Long userId);

    Post createPost(PostDTO postDTO, MultipartFile attachedPDF);

    Post updatePost(Post post);

    Integer deletePost(Long id);

    Integer deletePostFromGroup(Long id);
}
