package rs.ac.uns.ftn.svtkvtproject.service.implementation;

import rs.ac.uns.ftn.svtkvtproject.model.enums.ReactionType;
import rs.ac.uns.ftn.svtkvtproject.model.dto.ReactionDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Comment;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Reaction;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import rs.ac.uns.ftn.svtkvtproject.repository.ReactionRepository;
import rs.ac.uns.ftn.svtkvtproject.service.CommentService;
import rs.ac.uns.ftn.svtkvtproject.service.PostService;
import rs.ac.uns.ftn.svtkvtproject.service.ReactionService;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReactionServiceImpl implements ReactionService {

    private ReactionRepository reactionRepository;

    @Autowired
    public void setReactionRepository(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    private PostService postService;

    @Autowired
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private static final Logger logger = LogManager.getLogger(ReactionServiceImpl.class);

    @Override
    public Reaction findById(Long id) {
        Optional<Reaction> reaction = reactionRepository.findById(id);
        if (!reaction.isEmpty())
            return reaction.get();
        logger.error("Repository search for reaction with id: " + id + " returned null");
        return null;
    }

    @Override
    public List<Reaction> findReactionsForPost(Long postId) {
        Optional<List<Reaction>> reactions = reactionRepository.findAllByOnPostId(postId);
        if (!reactions.isEmpty())
            return reactions.get();
        logger.error("Repository search for reactions for post with id: " + postId + " returned null");
        return null;
    }

    @Override
    public List<Reaction> findReactionsForComment(Long commentId) {
        Optional<List<Reaction>> reactions = reactionRepository.findAllByOnCommentId(commentId);
        if (!reactions.isEmpty())
            return reactions.get();
        logger.error("Repository search for reactions for comment with id: " + commentId + " returned null");
        return null;
    }

    @Override
    public Reaction createReaction(ReactionDTO reactionDTO) {
        Optional<Reaction> reaction = reactionRepository.findById(reactionDTO.getId());

        if (reaction.isPresent()) {
            logger.error("Reaction with id: " + reactionDTO.getId() + " already exists in repository");
            return null;
        }

        Reaction newReaction = new Reaction();
        newReaction.setType(ReactionType.valueOf(reactionDTO.getReactionType()));
        newReaction.setTimestamp(LocalDate.parse(reactionDTO.getTimestamp()));

        User user = userService.findById(reactionDTO.getMadeByUserId());

        if (user == null) {
            logger.error("User with id: " + reactionDTO.getMadeByUserId() +
                    ", that made the reaction, was not found in the database");
            return null;
        }

        newReaction.setMadeBy(user);

        if (reactionDTO.getOnCommentId() != null) {
            Comment comment = commentService.findById(reactionDTO.getOnCommentId());
            newReaction.setOnComment(comment);
        }

        if (reactionDTO.getOnPostId() != null) {
            Post post = postService.findById(reactionDTO.getOnPostId());
            newReaction.setOnPost(post);
        }

        newReaction.setDeleted(false);
        newReaction = reactionRepository.save(newReaction);

        return newReaction;
    }

    @Override
    public Integer deleteReaction(Long id) {
        return reactionRepository.deleteReactionById(id);
    }

    @Override
    public Integer deletePostReactions(Long postId) {
        return reactionRepository.deleteReactionsByOnPostId(postId);
    }

    @Override
    public Integer deleteCommentReactions(Long commentId) {
        return reactionRepository.deleteReactionsByOnCommentId(commentId);
    }

    @Override
    public Integer deleteReactionsFromUser(Long userId) {
        return reactionRepository.deleteReactionsMadeByUserId(userId);
    }
}
