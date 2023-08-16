package rs.ac.uns.ftn.svtkvtproject.service.implementation;

import rs.ac.uns.ftn.svtkvtproject.model.dto.PostDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import rs.ac.uns.ftn.svtkvtproject.repository.PostRepository;
import rs.ac.uns.ftn.svtkvtproject.service.GroupService;
import rs.ac.uns.ftn.svtkvtproject.service.PostService;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    @Autowired
    public void setPostRepository(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private GroupService groupService;

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    private static final Logger logger = LogManager.getLogger(PostServiceImpl.class);

    @Override
    public Post findById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (!post.isEmpty())
            return post.get();
        logger.error("Repository search for post with id: " + id + " returned null");
        return null;
    }

    @Override
    public List<Post> findByCreationDate(LocalDateTime creationDate) {
        Optional<List<Post>> posts = postRepository.findAllByCreationDate(creationDate);
        if (!posts.isEmpty())
            return posts.get();
        logger.error("Repository search for post created on date: " + creationDate.toString() + " returned null");
        return null;
    }

    @Override
    public List<Post> findByPostedBy(User user) {
        Optional<List<Post>> posts = postRepository.findAllByPostedBy(user);
        if (!posts.isEmpty())
            return posts.get();
        logger.error("Repository search for post created by user with id: " + user.getId() + " returned null");
        return null;
    }

    @Override
    public List<Post> findAll() {
        return this.postRepository.findAll();
    }

    @Override
    public List<Post> findHomepagePosts(Long userId) {
        Optional<List<Post>> posts = postRepository.findHomepagePosts(userId);
        if (!posts.isEmpty())
            return posts.get();
        logger.error("Repository search for homepage posts for user with id: " + userId + " returned null");
        return null;
    }

    @Override
    public List<Post> findHomepagePostsSortedAsc(Long userId) {
        Optional<List<Post>> posts = postRepository.findHomepagePostsSortedAsc(userId);
        if (!posts.isEmpty())
            return posts.get();
        logger.error("Repository search for sorted homepage posts for user with id: " + userId + " returned null");
        return null;
    }

    @Override
    public List<Post> findHomepagePostsSortedDesc(Long userId) {
        Optional<List<Post>> posts = postRepository.findHomepagePostsSortedDesc(userId);
        if (!posts.isEmpty())
            return posts.get();
        logger.error("Repository search for sorted homepage posts for user with id: " + userId + " returned null");
        return null;
    }

    @Override
    public Post createPost(PostDTO postDTO) {
        Optional<Post> post = postRepository.findById(postDTO.getId());

        if (post.isPresent()) {
            logger.error("Post with id: " + postDTO.getId() + " already exists in repository");
            return null;
        }

        Post newPost = new Post();
        newPost.setContent(postDTO.getContent());
        newPost.setCreationDate(LocalDateTime.parse(postDTO.getCreationDate()));
        newPost.setPostedBy(userService.findById(postDTO.getPostedByUserId()));
        newPost.setDeleted(false);

        if (postDTO.getBelongsToGroupId() != null) {
            boolean userInGroup = groupService.checkUser(postDTO.getBelongsToGroupId(), postDTO.getPostedByUserId());

            if (!userInGroup) {
                logger.error("User with id: " + postDTO.getPostedByUserId() +
                        " tried posting in group with id: " + postDTO.getBelongsToGroupId() +
                        " while not being a member");
                return null;
            }
        }

        newPost = postRepository.save(newPost);

        if (postDTO.getBelongsToGroupId() != null)
            postRepository.saveGroupPost(postDTO.getBelongsToGroupId(), newPost.getId());

        return newPost;
    }

    @Override
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Integer deletePost(Long id) {
        return postRepository.deletePostById(id);
    }

    @Override
    public Integer deletePostFromGroup(Long id) {
        return postRepository.deletePostFromGroup(id);
    }
}
