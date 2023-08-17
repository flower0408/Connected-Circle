package rs.ac.uns.ftn.svtkvtproject.controller;

import rs.ac.uns.ftn.svtkvtproject.model.dto.ImageDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.PostDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.ReactionDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Image;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Reaction;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/reactions")
public class ReactionController {

    ReactionService reactionService;

    PostService postService;

    CommentService commentService;

    UserService userService;

    AuthenticationManager authenticationManager;

    TokenUtils tokenUtils;

    private static final Logger logger = LogManager.getLogger(ReactionController.class);

    @Autowired
    public ReactionController(ReactionService reactionService, PostService postService, CommentService commentService,
                              UserService userService, AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        this.reactionService = reactionService;
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReactionDTO> getOne(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding reaction for id: " + id);
        Reaction reaction = reactionService.findById(Long.parseLong(id));
        if (reaction == null) {
            logger.error("Reaction not found for id: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating response");
        ReactionDTO reactionDTO = new ReactionDTO(reaction);
        logger.info("Created and sent response");

        return new ResponseEntity<>(reactionDTO, HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<List<ReactionDTO>> getReactionsForPost(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding reactions for post with id: " + id);
        List<Reaction> reactions = reactionService.findReactionsForPost(Long.parseLong(id));
        List<ReactionDTO> reactionDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Reaction reaction: reactions) {
            reactionDTOS.add(new ReactionDTO(reaction));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(reactionDTOS, HttpStatus.OK);
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<List<ReactionDTO>> getReactionsForComment(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding reactions for comment with id: " + id);
        List<Reaction> reactions = reactionService.findReactionsForComment(Long.parseLong(id));
        List<ReactionDTO> reactionDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Reaction reaction: reactions) {
            reactionDTOS.add(new ReactionDTO(reaction));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(reactionDTOS, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ReactionDTO> addReaction(@RequestBody @Validated ReactionDTO newReaction, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating reaction from DTO");
        Reaction createdReaction = reactionService.createReaction(newReaction);
        if (createdReaction == null) {
            logger.error("Reaction couldn't be created from DTO");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        logger.info("Creating response");
        ReactionDTO reactionDTO = new ReactionDTO(createdReaction);
        logger.info("Created and sent response");

        return new ResponseEntity<>(reactionDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteReaction(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found for token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Deleting reaction with id: " + id);
        Integer deleted = reactionService.deleteReaction(Long.parseLong(id));
        if (deleted != 0) {
            logger.info("Successfully deleted reaction with id: " + id);
            return new ResponseEntity(deleted, HttpStatus.NO_CONTENT);
        }
        logger.error("Failed to delete reaction with id: " + id);

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}

