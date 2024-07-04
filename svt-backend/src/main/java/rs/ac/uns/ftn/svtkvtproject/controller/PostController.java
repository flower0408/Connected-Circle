package rs.ac.uns.ftn.svtkvtproject.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.GroupDocument;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.PostDocument;
import rs.ac.uns.ftn.svtkvtproject.model.dto.CommentDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.ImageDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.PostDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.SearchPostsByNumberOfLikes;
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
import rs.ac.uns.ftn.svtkvtproject.service.interfaces.SearchServicePost;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    SearchServicePost searchServicePost;

    private static final Logger logger = LogManager.getLogger(PostController.class);

    @Autowired
    public PostController(PostService postService, UserService userService, GroupService groupService, ImageService imageService,
                          CommentService commentService, ReactionService reactionService, AuthenticationManager authenticationManager, TokenUtils tokenUtils, SearchServicePost searchServicePost) {
        this.postService = postService;
        this.userService = userService;
        this.groupService = groupService;
        this.imageService = imageService;
        this.commentService = commentService;
        this.reactionService = reactionService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.searchServicePost = searchServicePost;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PostDTO> getOne(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ImageDTO>> getImagesForPost(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PostDTO>> getAll(@RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PostDTO>> getHomepagePosts(@RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PostDTO>> getHomepagePostsSorted(@PathVariable String order, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PostDTO>> getAllForGroup(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PostDTO>> getAllForGroupSortedAsc(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PostDTO>> getAllForGroupSortedDesc(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Boolean> checkUserInGroup(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PostDTO> addPost(@ModelAttribute  PostDTO newPost, @RequestHeader("authorization") String token, @RequestParam(required = false) MultipartFile attachedPDF) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating post from DTO");
        Post createdPost = postService.createPost(newPost, attachedPDF);
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

    @GetMapping("/title/{title}")
    public ResponseEntity<List<PostDocument>> findPostsByTitle(@PathVariable String title, @RequestParam(required = false, defaultValue = "false") boolean usePhraseQuery,
                                                               @RequestParam(required = false, defaultValue = "false") boolean useFuzzyQuery) {
        return new ResponseEntity<>(this.searchServicePost.getPostsByPostName(title, usePhraseQuery, useFuzzyQuery), HttpStatus.OK);
    }

    @GetMapping("/content/{content}")
    public ResponseEntity<List<PostDocument>> findPostsByContent(@PathVariable String content, @RequestParam(required = false, defaultValue = "false") boolean usePhraseQuery,
                                                                 @RequestParam(required = false, defaultValue = "false") boolean useFuzzyQuery) {
        return new ResponseEntity<>(this.searchServicePost.getPostsByPostContent(content, usePhraseQuery, useFuzzyQuery), HttpStatus.OK);
    }

    @GetMapping("/pdf-content/{content}")
    public ResponseEntity<List<PostDocument>> findPostsByPDFContent(@PathVariable String content, @RequestParam(required = false, defaultValue = "false") boolean usePhraseQuery,
                                                                    @RequestParam(required = false, defaultValue = "false") boolean useFuzzyQuery) {
        return new ResponseEntity<>(this.searchServicePost.getPostsByPDFContent(content, usePhraseQuery, useFuzzyQuery), HttpStatus.OK);
    }

    @GetMapping("/search-by-likes")
    public ResponseEntity<List<PostDocument>> findPostsByLikes(@RequestBody SearchPostsByNumberOfLikes criteria) {
        return new ResponseEntity<>(this.searchServicePost.getPostsByNumberOfLikes(criteria), HttpStatus.OK);

    }
    //GET http://localhost:8080/api/posts/search?title=test&content=provera&pdfContent=natasa&operation=OR
    @GetMapping("/search")
    public ResponseEntity<List<PostDocument>> searchGroupsBooleanQuery(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String pdfContent,
            @RequestParam(required = false, defaultValue = "OR") String operation,
            @RequestParam(required = false, defaultValue = "false") boolean usePhraseQuery,
            @RequestParam(required = false, defaultValue = "false") boolean useFuzzyQuery) {
        return new ResponseEntity<>(this.searchServicePost.searchPostsBooleanQuery(title, content, pdfContent, operation, usePhraseQuery, useFuzzyQuery), HttpStatus.OK);
    }

    @PatchMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PostDTO> editPost(@PathVariable String id, @RequestBody @Validated PostDTO editedPost,
                                              @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity deletePost(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<CommentDTO>> getCommentsForPost(@PathVariable String id,
                                                               @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);

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

   @GetMapping("/{id}/comments/sort/{order}")
   @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
   public ResponseEntity<List<CommentDTO>> getSortedCommentsForPost(@PathVariable String id, @RequestHeader("authorization") String token,
           @PathVariable String order) {
       logger.info("Authorization check");
       String cleanToken = token.substring(7);
       String username = tokenUtils.getUsernameFromToken(cleanToken);
       User user = userService.findByUsername(username);

       if (user == null) {
           logger.error("User not found for token: " + cleanToken);
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }

       logger.info("Finding comments for post with id: " + id);

       List<CommentDTO> commentDTOS = new ArrayList<>();
       List<Comment> comments;

       switch (order.toLowerCase()) {
           case "likes_asc":
               comments = commentService.findCommentsForPostLikesAsc(Long.parseLong(id));
               break;
           case "likes_desc":
               comments = commentService.findCommentsForPostLikesDesc(Long.parseLong(id));
               break;
           case "dislikes_asc":
               comments = commentService.findCommentsForPostDislikesAsc(Long.parseLong(id));
               break;
           case "dislikes_desc":
               comments = commentService.findCommentsForPostDislikesDesc(Long.parseLong(id));
               break;
           case "hearts_asc":
               comments = commentService.findCommentsForPostHeartsAsc(Long.parseLong(id));
               break;
           case "hearts_desc":
               comments = commentService.findCommentsForPostHeartsDesc(Long.parseLong(id));
               break;
           case "timestamp_asc":
               comments = commentService.findCommentsForPostTimestampAsc(Long.parseLong(id));
               break;
           case "timestamp_desc":
               comments = commentService.findCommentsForPostTimestampDesc(Long.parseLong(id));
               break;
           default:
               comments = commentService.findCommentsForPostLikesAsc(Long.parseLong(id));
               break;
       }

       for (Comment comment : comments) {
           commentDTOS.add(new CommentDTO(comment));
       }

       logger.info("Created and sent response");

       return new ResponseEntity<>(commentDTOS, HttpStatus.OK);
   }


}
