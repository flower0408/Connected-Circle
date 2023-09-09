package rs.ac.uns.ftn.svtkvtproject.service;

import rs.ac.uns.ftn.svtkvtproject.model.dto.CommentDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Comment;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;

import java.util.List;

public interface CommentService {

    Comment findById(Long id);

    List<Comment> findAll();

    List<Comment> findCommentsForPost(Long postId);

    List<Comment> findCommentsForPostLikesAsc(Long postId);

    List<Comment> findCommentsForPostLikesDesc(Long postId);

    List<Comment> findCommentsForPostDislikesAsc(Long postId);

    List<Comment> findCommentsForPostDislikesDesc(Long postId);

    List<Comment> findCommentsForPostHeartsAsc(Long postId);

    List<Comment> findCommentsForPostHeartsDesc(Long postId);

    List<Comment> findCommentsForPostTimestampAsc(Long postId);

    List<Comment> findCommentsForPostTimestampDesc(Long postId);

    Comment createComment(CommentDTO commentDTO);

    Comment updateComment(Comment comment);

    Integer deleteComment(Long id);

    Integer deleteCommentReply(Long commentId);

    Integer deletePostComments(Long postId);

    Integer deleteUserComments(Long userId);
}

