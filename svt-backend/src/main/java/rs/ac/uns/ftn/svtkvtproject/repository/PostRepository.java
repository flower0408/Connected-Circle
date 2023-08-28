package rs.ac.uns.ftn.svtkvtproject.repository;

import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<List<Post>> findAllByCreationDate(LocalDateTime creationDate);

    Optional<List<Post>> findAllByPostedBy(User user);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM post p " +
                    "JOIN user u ON p.posted_by_user_id = u.id " +
                    "WHERE p.id IN ( " +
                    "    SELECT id FROM post WHERE posted_by_user_id = :userId AND p.is_deleted = false " +
                    "    UNION " +
                    "    SELECT id FROM post WHERE posted_by_user_id = :userId AND p.is_deleted = false AND id IN ( " +
                    "        SELECT friend_id FROM user_friends WHERE user_id = :userId " +
                    "        UNION " +
                    "        SELECT user_id FROM user_friends WHERE friend_id = :userId " +
                    "    ) " +
                    "    UNION " +
                    "    SELECT id FROM post WHERE posted_by_user_id IN ( " +
                    "        SELECT friend_id FROM user_friends WHERE user_id = :userId " +
                    "    ) AND p.is_deleted = false " +
                    "    UNION " +
                    "    SELECT id FROM post WHERE posted_by_user_id IN ( " +
                    "        SELECT user_id FROM user_friends WHERE friend_id = :userId " +
                    "    ) AND p.is_deleted = false " +
                    "    UNION " +
                    "    SELECT post_id FROM group_posts WHERE group_id IN ( " +
                    "        SELECT group_id FROM group_members WHERE member_id = :userId " +
                    "    ) AND p.is_deleted = false " +
                    ") " +
                    "AND p.id NOT IN ( " +
                    "    SELECT post_id FROM group_posts WHERE group_id NOT IN ( " +
                    "        SELECT group_id FROM group_members WHERE member_id = :userId " +
                    "    ) AND p.is_deleted = false " +
                    ") " +
                    "AND p.is_deleted = false " +
                    "AND u.is_deleted = false ")
    Optional<List<Post>> findHomepagePosts(@Param("userId") Long userId);


    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM post p " +
                    "JOIN user u ON p.posted_by_user_id = u.id " +
                    "WHERE p.id IN ( " +
                    "    SELECT id FROM post WHERE posted_by_user_id = :userId AND p.is_deleted = false " +
                    "    UNION " +
                    "    SELECT id FROM post WHERE posted_by_user_id = :userId AND p.is_deleted = false AND id IN ( " +
                    "        SELECT friend_id FROM user_friends WHERE user_id = :userId " +
                    "        UNION " +
                    "        SELECT user_id FROM user_friends WHERE friend_id = :userId " +
                    "    ) " +
                    "    UNION " +
                    "    SELECT id FROM post WHERE posted_by_user_id IN ( " +
                    "        SELECT friend_id FROM user_friends WHERE user_id = :userId " +
                    "    ) AND p.is_deleted = false " +
                    "    UNION " +
                    "    SELECT id FROM post WHERE posted_by_user_id IN ( " +
                    "        SELECT user_id FROM user_friends WHERE friend_id = :userId " +
                    "    ) AND p.is_deleted = false " +
                    "    UNION " +
                    "    SELECT post_id FROM group_posts WHERE group_id IN ( " +
                    "        SELECT group_id FROM group_members WHERE member_id = :userId " +
                    "    ) AND p.is_deleted = false " +
                    ") " +
                    "AND p.id NOT IN ( " +
                    "    SELECT post_id FROM group_posts WHERE group_id NOT IN ( " +
                    "        SELECT group_id FROM group_members WHERE member_id = :userId " +
                    "    ) AND p.is_deleted = false " +
                    ") " +
                    "AND p.is_deleted = false " +
                    "AND u.is_deleted = false " +
                    "ORDER BY p.creation_date ASC;\n")
    Optional<List<Post>> findHomepagePostsSortedAsc(@Param("userId") Long userId);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM post p " +
                    "JOIN user u ON p.posted_by_user_id = u.id " +
                    "WHERE p.id IN ( " +
                    "    SELECT id FROM post WHERE posted_by_user_id = :userId AND p.is_deleted = false " +
                    "    UNION " +
                    "    SELECT id FROM post WHERE posted_by_user_id = :userId AND p.is_deleted = false AND id IN ( " +
                    "        SELECT friend_id FROM user_friends WHERE user_id = :userId " +
                    "        UNION " +
                    "        SELECT user_id FROM user_friends WHERE friend_id = :userId " +
                    "    ) " +
                    "    UNION " +
                    "    SELECT id FROM post WHERE posted_by_user_id IN ( " +
                    "        SELECT friend_id FROM user_friends WHERE user_id = :userId " +
                    "    ) AND p.is_deleted = false " +
                    "    UNION " +
                    "    SELECT id FROM post WHERE posted_by_user_id IN ( " +
                    "        SELECT user_id FROM user_friends WHERE friend_id = :userId " +
                    "    ) AND p.is_deleted = false " +
                    "    UNION " +
                    "    SELECT post_id FROM group_posts WHERE group_id IN ( " +
                    "        SELECT group_id FROM group_members WHERE member_id = :userId " +
                    "    ) AND p.is_deleted = false " +
                    ") " +
                    "AND p.id NOT IN ( " +
                    "    SELECT post_id FROM group_posts WHERE group_id NOT IN ( " +
                    "        SELECT group_id FROM group_members WHERE member_id = :userId " +
                    "    ) AND p.is_deleted = false " +
                    ") " +
                    "AND p.is_deleted = false " +
                    "AND u.is_deleted = false " +
                    "ORDER BY p.creation_date DESC;\n")
    Optional<List<Post>> findHomepagePostsSortedDesc(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "insert into group_posts (group_id, post_id) values (:groupId, :postId)")
    Integer saveGroupPost(@Param("groupId") Long groupId, @Param("postId") Long postId);

    @Transactional
    Integer deletePostById(Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "delete from group_posts where post_id = :id")
    Integer deletePostFromGroup(@Param("id") Long id);
}
