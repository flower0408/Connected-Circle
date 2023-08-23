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
            value = "SELECT *\n" +
                    "FROM post\n" +
                    "WHERE id IN (\n" +
                    "    SELECT id FROM post WHERE posted_by_user_id = :userId AND is_deleted = false\n" +
                    "    UNION\n" +
                    "    SELECT id FROM post WHERE posted_by_user_id = 1 AND is_deleted = false AND id IN (\n" +
                    "        SELECT friend_id FROM user_friends WHERE user_id = :userId\n" +
                    "        UNION\n" +
                    "        SELECT user_id FROM user_friends WHERE friend_id = :userId\n" +
                    "    )\n" +
                    "    UNION\n" +
                    "    SELECT id FROM post WHERE posted_by_user_id IN (\n" +
                    "        SELECT friend_id FROM user_friends WHERE user_id = :userId\n" +
                    "    ) AND is_deleted = false\n" +
                    "    UNION\n" +
                    "    SELECT id FROM post WHERE posted_by_user_id IN (\n" +
                    "        SELECT user_id FROM user_friends WHERE friend_id = :userId\n" +
                    "    ) AND is_deleted = false\n" +
                    "    UNION\n" +
                    "    SELECT post_id FROM group_posts WHERE group_id IN (\n" +
                    "        SELECT group_id FROM group_members WHERE member_id = :userId\n" +
                    "    ) AND is_deleted = false\n" +
                    ")\n" +
                    "AND id NOT IN (\n" +
                    "    SELECT post_id FROM group_posts WHERE group_id NOT IN (\n" +
                    "        SELECT group_id FROM group_members WHERE member_id = :userId\n" +
                    "    ) AND is_deleted = false\n" +
                    ")\n" +
                    "AND is_deleted = false;")
    Optional<List<Post>> findHomepagePosts(@Param("userId") Long userId);

    @Query(nativeQuery = true,
            value = "SELECT *\n" +
                    "FROM post\n" +
                    "WHERE id IN (\n" +
                    "    SELECT id FROM post WHERE posted_by_user_id = 1 AND is_deleted = false\n" +
                    "    UNION\n" +
                    "    SELECT id FROM post WHERE posted_by_user_id = 1 AND is_deleted = false AND id IN (\n" +
                    "        SELECT friend_id FROM user_friends WHERE user_id = 1\n" +
                    "        UNION\n" +
                    "        SELECT user_id FROM user_friends WHERE friend_id = 1\n" +
                    "    )\n" +
                    "    UNION\n" +
                    "    SELECT id FROM post WHERE posted_by_user_id IN (\n" +
                    "        SELECT friend_id FROM user_friends WHERE user_id = 1\n" +
                    "    ) AND is_deleted = false\n" +
                    "    UNION\n" +
                    "    SELECT id FROM post WHERE posted_by_user_id IN (\n" +
                    "        SELECT user_id FROM user_friends WHERE friend_id = 1\n" +
                    "    ) AND is_deleted = false\n" +
                    "    UNION\n" +
                    "    SELECT post_id FROM group_posts WHERE group_id IN (\n" +
                    "        SELECT group_id FROM group_members WHERE member_id = 1\n" +
                    "    ) AND is_deleted = false\n" +
                    ")\n" +
                    "AND id NOT IN (\n" +
                    "    SELECT post_id FROM group_posts WHERE group_id NOT IN (\n" +
                    "        SELECT group_id FROM group_members WHERE member_id = 1\n" +
                    "    ) AND is_deleted = false\n" +
                    ")\n" +
                    "AND is_deleted = false\n" +
                    "ORDER BY creation_date ASC;\n")
    Optional<List<Post>> findHomepagePostsSortedAsc(@Param("userId") Long userId);

    @Query(nativeQuery = true,
            value = "SELECT *\n" +
                    "FROM post\n" +
                    "WHERE id IN (\n" +
                    "    SELECT id FROM post WHERE posted_by_user_id = 1 AND is_deleted = false\n" +
                    "    UNION\n" +
                    "    SELECT id FROM post WHERE posted_by_user_id = 1 AND is_deleted = false AND id IN (\n" +
                    "        SELECT friend_id FROM user_friends WHERE user_id = 1\n" +
                    "        UNION\n" +
                    "        SELECT user_id FROM user_friends WHERE friend_id = 1\n" +
                    "    )\n" +
                    "    UNION\n" +
                    "    SELECT id FROM post WHERE posted_by_user_id IN (\n" +
                    "        SELECT friend_id FROM user_friends WHERE user_id = 1\n" +
                    "    ) AND is_deleted = false\n" +
                    "    UNION\n" +
                    "    SELECT id FROM post WHERE posted_by_user_id IN (\n" +
                    "        SELECT user_id FROM user_friends WHERE friend_id = 1\n" +
                    "    ) AND is_deleted = false\n" +
                    "    UNION\n" +
                    "    SELECT post_id FROM group_posts WHERE group_id IN (\n" +
                    "        SELECT group_id FROM group_members WHERE member_id = 1\n" +
                    "    ) AND is_deleted = false\n" +
                    ")\n" +
                    "AND id NOT IN (\n" +
                    "    SELECT post_id FROM group_posts WHERE group_id NOT IN (\n" +
                    "        SELECT group_id FROM group_members WHERE member_id = 1\n" +
                    "    ) AND is_deleted = false\n" +
                    ")\n" +
                    "AND is_deleted = false\n" +
                    "ORDER BY creation_date DESC;\n")
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
