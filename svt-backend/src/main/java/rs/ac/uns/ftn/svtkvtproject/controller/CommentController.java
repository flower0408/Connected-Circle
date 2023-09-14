package rs.ac.uns.ftn.svtkvtproject.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import rs.ac.uns.ftn.svtkvtproject.model.dto.CommentDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.PostDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Comment;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import rs.ac.uns.ftn.svtkvtproject.security.TokenUtils;
import rs.ac.uns.ftn.svtkvtproject.service.CommentService;
import rs.ac.uns.ftn.svtkvtproject.service.PostService;
import rs.ac.uns.ftn.svtkvtproject.service.ReactionService;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/comments")
public class CommentController {

    CommentService commentService;

    PostService postService;

    UserService userService;

    ReactionService reactionService;

    AuthenticationManager authenticationManager;

    TokenUtils tokenUtils;

    private static final Logger logger = LogManager.getLogger(CommentController.class);

    @Autowired
    public CommentController(CommentService commentService, PostService postService, UserService userService,
                             ReactionService reactionService, AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
        this.reactionService = reactionService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentDTO> getOne(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding comment for id: " + id);
        Comment comment = commentService.findById(Long.parseLong(id));
        if (comment == null) {
            logger.error("Comment not found for id: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CommentDTO commentDTO = new CommentDTO(comment);
        logger.info("Found comment for id: " + id);

        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<CommentDTO>> getAll(@RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding all comments");
        List<Comment> comments = commentService.findAll();
        List<CommentDTO> commentsDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Comment comment: comments) {
            commentsDTOS.add(new CommentDTO(comment));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(commentsDTOS, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentDTO> addComment(@RequestBody @Validated CommentDTO newComment, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating comment from DTO");
        Comment createdComment = commentService.createComment(newComment);
        if (createdComment == null) {
            logger.error("Comment couldn't be created from DTO");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        logger.info("Creating response");
        CommentDTO commentDTO = new CommentDTO(createdComment);
        logger.info("Created and sent response");

        return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommentDTO> editComment(@PathVariable String id, @RequestBody @Validated CommentDTO editedComment,
                                                  @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding original comment for id: " + editedComment.getId());
        Comment oldComment = commentService.findById(Long.parseLong(id));
        if (oldComment == null) {
            logger.error("Original comment not found for id: " + editedComment.getId());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        logger.info("Applying changes");
        oldComment.setText(editedComment.getText());
        oldComment.setTimestamp(LocalDate.parse(editedComment.getTimestamp()));
        oldComment = commentService.updateComment(oldComment);
        logger.info("Creating response");
        CommentDTO updatedComment = new CommentDTO(oldComment);
        logger.info("Created and sent response");

        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity deleteComment(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Deleting comment with id: " + id);
        commentService.deleteCommentReply(Long.parseLong(id));
        Integer deleted = commentService.deleteComment(Long.parseLong(id));
        reactionService.deleteCommentReactions(Long.parseLong(id));
        if (deleted != 0) {
            logger.info("Successfully deleted comment with id: " + id);
            return new ResponseEntity(deleted, HttpStatus.NO_CONTENT);
        }
        logger.error("Failed to delete comment with id: " + id);

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}

