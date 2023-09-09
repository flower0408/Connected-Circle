package rs.ac.uns.ftn.svtkvtproject.repository;

import rs.ac.uns.ftn.svtkvtproject.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(nativeQuery = true,
            value = "select * from comment where belongs_to_post_id = :postId and is_deleted = false")
    Optional<List<Comment>> findCommentsForPost(@Param("postId") Long postId);

    @Query(nativeQuery = true,
            value = "SELECT c.* " +
                    "FROM comment c " +
                    "LEFT JOIN (SELECT on_comment_id, COUNT(*) AS like_count FROM reaction WHERE type = 'LIKE' GROUP BY on_comment_id) l ON c.id = l.on_comment_id " +
                    "WHERE c.belongs_to_post_id = :postId AND c.is_deleted = false AND c.replies_to_comment_id IS NULL " +
                    "ORDER BY l.like_count ASC")
    Optional<List<Comment>> findCommentsForPostLikesAsc(@Param("postId") Long postId);

    @Query(nativeQuery = true,
            value = "SELECT c.* " +
                    "FROM comment c " +
                    "LEFT JOIN (SELECT on_comment_id, COUNT(*) AS like_count FROM reaction WHERE type = 'LIKE' GROUP BY on_comment_id) l ON c.id = l.on_comment_id " +
                    "WHERE c.belongs_to_post_id = :postId AND c.is_deleted = false AND c.replies_to_comment_id IS NULL " +
                    "ORDER BY l.like_count DESC")
    Optional<List<Comment>> findCommentsForPostLikesDesc(@Param("postId") Long postId);

    @Query(nativeQuery = true,
            value = "SELECT c.* " +
                    "FROM comment c " +
                    "LEFT JOIN (SELECT on_comment_id, COUNT(*) AS dislike_count FROM reaction WHERE type = 'DISLIKE' GROUP BY on_comment_id) d ON c.id = d.on_comment_id " +
                    "WHERE c.belongs_to_post_id = :postId AND c.is_deleted = false AND c.replies_to_comment_id IS NULL " +
                    "ORDER BY d.dislike_count ASC")
    Optional<List<Comment>> findCommentsForPostDislikesAsc(@Param("postId") Long postId);

    @Query(nativeQuery = true,
            value = "SELECT c.* " +
                    "FROM comment c " +
                    "LEFT JOIN (SELECT on_comment_id, COUNT(*) AS dislike_count FROM reaction WHERE type = 'DISLIKE' GROUP BY on_comment_id) d ON c.id = d.on_comment_id " +
                    "WHERE c.belongs_to_post_id = :postId AND c.is_deleted = false AND c.replies_to_comment_id IS NULL " +
                    "ORDER BY d.dislike_count DESC")
    Optional<List<Comment>> findCommentsForPostDislikesDesc(@Param("postId") Long postId);

    @Query(nativeQuery = true,
            value = "SELECT c.* " +
                    "FROM comment c " +
                    "LEFT JOIN (SELECT on_comment_id, COUNT(*) AS heart_count FROM reaction WHERE type = 'HEART' GROUP BY on_comment_id) h ON c.id = h.on_comment_id " +
                    "WHERE c.belongs_to_post_id = :postId AND c.is_deleted = false AND c.replies_to_comment_id IS NULL " +
                    "ORDER BY h.heart_count ASC")
    Optional<List<Comment>> findCommentsForPostHeartsAsc(@Param("postId") Long postId);

    @Query(nativeQuery = true,
            value = "SELECT c.* " +
                    "FROM comment c " +
                    "LEFT JOIN (SELECT on_comment_id, COUNT(*) AS heart_count FROM reaction WHERE type = 'HEART' GROUP BY on_comment_id) h ON c.id = h.on_comment_id " +
                    "WHERE c.belongs_to_post_id = :postId AND c.is_deleted = false AND c.replies_to_comment_id IS NULL " +
                    "ORDER BY h.heart_count DESC")
    Optional<List<Comment>> findCommentsForPostHeartsDesc(@Param("postId") Long postId);

    @Query(nativeQuery = true,
            value = "SELECT c.* " +
                    "FROM comment c " +
                    "WHERE c.belongs_to_post_id = :postId AND c.is_deleted = false AND c.replies_to_comment_id IS NULL " +
                    "ORDER BY c.timestamp ASC")
    Optional<List<Comment>> findCommentsForPostTimestampAsc(@Param("postId") Long postId);

    @Query(nativeQuery = true,
            value = "SELECT c.* " +
                    "FROM comment c " +
                    "WHERE c.belongs_to_post_id = :postId AND c.is_deleted = false AND c.replies_to_comment_id IS NULL " +
                    "ORDER BY c.timestamp DESC")
    Optional<List<Comment>> findCommentsForPostTimestampDesc(@Param("postId") Long postId);

    @Transactional
    Integer deleteCommentById(Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "update comment set is_deleted = true where belongs_to_post_id = :postId")
    Integer deleteCommentsByBelongsToPostId(@Param("postId") Long postId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "update comment set is_deleted = true where replies_to_comment_id = :commentId")
    Integer deleteReplyToComment(@Param("commentId") Long commentId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "update comment set is_deleted = true where belongs_to_user_id = :userId")
    Integer deleteCommentsByBelongsToUserId(@Param("userId") Long userId);
}
