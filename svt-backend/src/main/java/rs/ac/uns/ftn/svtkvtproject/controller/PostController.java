package rs.ac.uns.ftn.svtkvtproject.controller;

import rs.ac.uns.ftn.svtkvtproject.model.dto.CommentDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.ImageDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.PostDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.*;
import rs.ac.uns.ftn.svtkvtproject.security.TokenUtils;
import rs.ac.uns.ftn.svtkvtproject.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {

    PostService postService;

    GroupService groupService;

    UserService userService;

    ImageService imageService;

    CommentService commentService;

    ReactionService reactionService;

    AuthenticationManager authenticationManager;

    TokenUtils tokenUtils;

    private static final Logger logger = LogManager.getLogger(PostController.class);

    @Autowired
    public PostController(PostService postService, UserService userService, GroupService groupService, ImageService imageService,
                          CommentService commentService, ReactionService reactionService, AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        this.postService = postService;
        this.userService = userService;
        this.groupService = groupService;
        this.imageService = imageService;
        this.commentService = commentService;
        this.reactionService = reactionService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getOne(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding post for id: " + id);
        Post post = postService.findById(Long.parseLong(id));
        if (post == null) {
            logger.error("Post not found for id: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating response");
        PostDTO postDTO = new PostDTO(post);
        logger.info("Created and sent response");

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<List<ImageDTO>> getImagesForPost(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding images for post with id: " + id);
        List<Image> images = imageService.findImagesForPost(Long.parseLong(id));
        List<ImageDTO> imageDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Image image: images) {
            imageDTOS.add(new ImageDTO(image));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(imageDTOS, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<PostDTO>> getAll(@RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding all posts");
        List<Post> posts = postService.findAll();
        List<PostDTO> postsDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Post post: posts) {
            postsDTOS.add(new PostDTO(post));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(postsDTOS, HttpStatus.OK);
    }

    @GetMapping("/homepage")
    public ResponseEntity<List<PostDTO>> getHomepagePosts(@RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding all homepage posts");
        List<Post> posts = postService.findHomepagePosts(user.getId());
        List<PostDTO> postsDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Post post: posts) {
            PostDTO postDTO = new PostDTO(post);
            Group group = groupService.checkIfPostInGroup(post.getId());
            if (group != null)
                postDTO.setBelongsToGroupId(group.getId());
            postsDTOS.add(postDTO);
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(postsDTOS, HttpStatus.OK);
    }

    @GetMapping("/homepage/sort/{order}")
    public ResponseEntity<List<PostDTO>> getHomepagePostsSorted(@PathVariable String order, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Sorting homepage posts");
        List<Post> posts = new ArrayList<>();
        if (order.equals("asc"))
            posts = postService.findHomepagePostsSortedAsc(user.getId());
        else if (order.equals("desc"))
            posts = postService.findHomepagePostsSortedDesc(user.getId());
        List<PostDTO> postsDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Post post: posts) {
            PostDTO postDTO = new PostDTO(post);
            Group group = groupService.checkIfPostInGroup(post.getId());
            if (group != null)
                postDTO.setBelongsToGroupId(group.getId());
            postsDTOS.add(postDTO);
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(postsDTOS, HttpStatus.OK);
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<List<PostDTO>> getAllForGroup(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding all posts for group with id: " + id);
        List<Long> postsIds = groupService.findPostsByGroupId(Long.parseLong(id));
        if (postsIds == null) {
            logger.error("Posts not found for group with id: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<PostDTO> postDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Long postId: postsIds) {
            Post post = postService.findById(postId);
            PostDTO postDTO = new PostDTO(post);
            postDTO.setBelongsToGroupId(Long.parseLong(id));
            postDTOS.add(postDTO);
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(postDTOS, HttpStatus.OK);
    }

    @GetMapping("/group/{id}/sort/asc")
    public ResponseEntity<List<PostDTO>> getAllForGroupSortedAsc(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding all posts for group with id: " + id);
        List<Long> postsIds = groupService.findPostsByGroupIdAsc(Long.parseLong(id));
        if (postsIds == null) {
            logger.error("Posts not found for group with id: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<PostDTO> postDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Long postId: postsIds) {
            Post post = postService.findById(postId);
            PostDTO postDTO = new PostDTO(post);
            postDTO.setBelongsToGroupId(Long.parseLong(id));
            postDTOS.add(postDTO);
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(postDTOS, HttpStatus.OK);
    }

    @GetMapping("/group/{id}/sort/desc")
    public ResponseEntity<List<PostDTO>> getAllForGroupSortedDesc(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding all posts for group with id: " + id);
        List<Long> postsIds = groupService.findPostsByGroupIdDesc(Long.parseLong(id));
        if (postsIds == null) {
            logger.error("Posts not found for group with id: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<PostDTO> postDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Long postId: postsIds) {
            Post post = postService.findById(postId);
            PostDTO postDTO = new PostDTO(post);
            postDTO.setBelongsToGroupId(Long.parseLong(id));
            postDTOS.add(postDTO);
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(postDTOS, HttpStatus.OK);
    }

    @GetMapping("/group/{id}/user")
    public ResponseEntity<Boolean> checkUserInGroup(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (userService.checkUserIsAdmin(user.getId())) {
            logger.info("User is system admin and has access to group");
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        logger.info("Checking if user is in group with id: " + id);
        Boolean userInGroup = groupService.checkUser(Long.parseLong(id), user.getId());
        if (!userInGroup) {
            logger.error("User not found in group with id: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("User found in group with id: " + id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<PostDTO> addPost(@RequestBody @Validated PostDTO newPost, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating post from DTO");
        Post createdPost = postService.createPost(newPost);
        if (createdPost == null) {
            logger.error("Post couldn't be created from DTO");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        logger.info("Checking if post has images and adding them");
        if (newPost.getImages() != null) {
            for (ImageDTO imageDTO: newPost.getImages()) { //kada se pravi post, ne postoji id post-a
                imageDTO.setBelongsToPostId(createdPost.getId()); //pa se mora ovde postaviti ili image nece pripadati nikom
                imageService.createImage(imageDTO);
            }
        }
        logger.info("Creating response");
        PostDTO postDTO = new PostDTO(createdPost);
        logger.info("Created and sent response");

        return new ResponseEntity<>(postDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<PostDTO> editPost(@PathVariable String id, @RequestBody @Validated PostDTO editedPost,
                                              @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding original post for id: " + id);
        Post oldPost = postService.findById(Long.parseLong(id));
        if (oldPost == null) {
            logger.error("Original post not found for id: " + id);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        logger.info("Applying changes");
        oldPost.setContent(editedPost.getContent());
        if (editedPost.getImages().size() > 0) {
            imageService.deletePostImages(Long.parseLong(id));
            for (ImageDTO imageDTO: editedPost.getImages())
                imageService.createImage(imageDTO);
        }
        oldPost = postService.updatePost(oldPost);
        logger.info("Creating response");
        PostDTO updatedPost = new PostDTO(oldPost);
        logger.info("Created and sent response");

        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deletePost(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Deleting post with id: " + id);
        postService.deletePostFromGroup(Long.parseLong(id));
        Integer deletedFromAll = postService.deletePost(Long.parseLong(id));
        imageService.deletePostImages(Long.parseLong(id));
        commentService.deletePostComments(Long.parseLong(id));
        reactionService.deletePostReactions(Long.parseLong(id));
        if (deletedFromAll != 0) {
            logger.info("Successfully deleted post with id: " + id);
            return new ResponseEntity(deletedFromAll, HttpStatus.NO_CONTENT);
        }
        logger.error("Failed to delete post with id: " + id);

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsForPost(@PathVariable String id,
                                                               @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7); //izbacivanje 'Bearer' iz tokena
        String username = tokenUtils.getUsernameFromToken(cleanToken); //izvlacenje username-a iz tokena
        User user = userService.findByUsername(username); //provera da li postoji u bazi

        if (user == null) {
            logger.error("User not found for token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Finding comments for post with id: " + id);
        List<Comment> comments = commentService.findCommentsForPost(Long.parseLong(id));
        List<CommentDTO> commentDTOS = new ArrayList<>();

        logger.info("Creating response");
        for (Comment comment: comments) {
            commentDTOS.add(new CommentDTO(comment));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(commentDTOS, HttpStatus.OK);
    }
}
