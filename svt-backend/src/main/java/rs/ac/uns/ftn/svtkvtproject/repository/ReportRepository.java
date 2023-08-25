package rs.ac.uns.ftn.svtkvtproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Reaction;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Report;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

   /* @Query(nativeQuery = true,
            value = "SELECT DISTINCT r FROM report r WHERE r.is_deleted = false")
    List<Report> findAll();*/
   @Query(nativeQuery = true,
           value = "SELECT r.*\n" +
                   "FROM report r\n" +
                   "LEFT JOIN comment c ON r.on_comment_id = c.id\n" +
                   "LEFT JOIN post p ON r.on_post_id = p.id\n" +
                   "LEFT JOIN group_posts gp ON p.id = gp.post_id OR c.belongs_to_post_id = gp.post_id\n" +
                   "WHERE gp.group_id IS NULL\n" +
                   "AND r.is_deleted = false;") // Add the condition for is_deleted
   Optional<List<Report>> findAllReports();


 @Query(nativeQuery = true,
            value = "select * from report where on_post_id = :postId and is_deleted = false")
    Optional<List<Report>> findAllByOnPostId(@Param("postId") Long postId);

    @Query(nativeQuery = true,
            value = "select * from report where on_comment_id = :commentId and is_deleted = false")
    Optional<List<Report>> findAllByOnCommentId(@Param("commentId") Long commentId);

    @Query(nativeQuery = true,
            value = "select * from report where on_user_id = :userId and is_deleted = false")
    Optional<List<Report>> findAllByOnUserId(@Param("userId") Long userId);

    @Transactional
    Integer deleteReportById(Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "update report set is_deleted = true where on_post_id = :postId")
    Integer deleteReportsByOnPostId(@Param("postId") Long postId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "update reaction set is_deleted = true where on_comment_id = :commentId")
    Integer deleteReportsByOnCommentId(@Param("commentId") Long commentId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "update reaction set is_deleted = true where by_user_id = :userId")
    Integer deleteReportsMadeByUserId(@Param("userId") Long userId);



}
