package rs.ac.uns.ftn.svtkvtproject.service.implementation;

import rs.ac.uns.ftn.svtkvtproject.controller.CommentController;
import rs.ac.uns.ftn.svtkvtproject.model.dto.CommentDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Comment;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import rs.ac.uns.ftn.svtkvtproject.repository.CommentRepository;
import rs.ac.uns.ftn.svtkvtproject.service.CommentService;
import rs.ac.uns.ftn.svtkvtproject.service.PostService;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

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

    private static final Logger logger = LogManager.getLogger(CommentServiceImpl.class);

    @Override
    public Comment findById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (!comment.isEmpty())
            return comment.get();
        logger.error("Repository search for comment with id: " + id + " returned null");
        return null;
    }

    @Override
    public List<Comment> findAll() {
        return this.commentRepository.findAll();
    }

    @Override
    public List<Comment> findCommentsForPost(Long postId) {
        Optional<List<Comment>> comments = commentRepository.findCommentsForPost(postId);
        if (!comments.isEmpty())
            return comments.get();
        logger.error("Repository search for comments for post with id: " + postId + " returned null");
        return null;
    }

    @Override
    public Comment createComment(CommentDTO commentDTO) {
        Optional<Comment> comment = commentRepository.findById(commentDTO.getId());

        if (comment.isPresent()) {
            logger.error("Comment with id: " + commentDTO.getId() + " already exists in repository");
            return null;
        }

        Comment newComment = new Comment();
        newComment.setText(commentDTO.getText());
        newComment.setTimestamp(LocalDate.parse(commentDTO.getTimestamp()));

        if (commentDTO.getRepliesToCommentId() != null) {
            Comment repliesTo = this.findById(commentDTO.getRepliesToCommentId());
            newComment.setRepliesTo(repliesTo);
        }

        User user = userService.findById(commentDTO.getBelongsToUserId());
        newComment.setBelongsToUser(user);

        if (commentDTO.getBelongsToPostId() != null) {
            Post post = postService.findById(commentDTO.getBelongsToPostId());
            newComment.setBelongsToPost(post);
        }

        newComment.setDeleted(false);
        newComment = commentRepository.save(newComment);

        return newComment;
    }

    @Override
    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Integer deleteComment(Long id) {
        return commentRepository.deleteCommentById(id);
    }

    @Override
    public Integer deleteCommentReply(Long commentId) {
        return commentRepository.deleteReplyToComment(commentId);
    }

    @Override
    public Integer deletePostComments(Long postId) {
        List<Comment> postComments = this.findCommentsForPost(postId);
        for (Comment comment: postComments) {
            this.deleteCommentReply(comment.getId()); //brisanje prvo svih odgovora na komentare
        }
        return commentRepository.deleteCommentsByBelongsToPostId(postId); //brisanje komentara
    }

    @Override
    public Integer deleteUserComments(Long userId) {
        return commentRepository.deleteCommentsByBelongsToUserId(userId);
    }
}
