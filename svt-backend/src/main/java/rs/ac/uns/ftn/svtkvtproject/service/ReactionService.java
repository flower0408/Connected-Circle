package rs.ac.uns.ftn.svtkvtproject.service;

import rs.ac.uns.ftn.svtkvtproject.model.dto.ReactionDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Reaction;

import java.util.List;

public interface ReactionService {

    Reaction findById(Long id);

    List<Reaction> findReactionsForPost(Long postId);

    List<Reaction> findReactionsForComment(Long commentId);

    Reaction createReaction(ReactionDTO reactionDTO);

    Integer deleteReaction(Long id);

    Integer deletePostReactions(Long postId);

    Integer deleteCommentReactions(Long commentId);

    Integer deleteReactionsFromUser(Long userId);
}