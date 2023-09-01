package rs.ac.uns.ftn.svtkvtproject.repository;

import rs.ac.uns.ftn.svtkvtproject.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);

    Optional<List<Group>> findAllByCreationDate(LocalDateTime creationDate);

    @Transactional
    Integer deleteGroupById(Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "insert into group_admins (group_id, admin_id) values (:groupId, :adminId);")
    Integer addGroupAdmin(@Param("groupId") Long groupId, @Param("adminId") Long adminId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "insert into group_members (group_id, member_id) values (:groupId, :memberId);")
    Integer addGroupMember(@Param("groupId") Long groupId, @Param("memberId") Long memberId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "delete from group_admins where group_id = :groupId and admin_id = :adminId")
    Integer deleteGroupAdmin(@Param("groupId") Long groupId, @Param("adminId") Long adminId);


    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "delete from group_members where group_id = :id")
    Integer deleteGroupMembers(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "delete from group_members where group_id = :groupId and member_id = :memberId")
    Integer deleteGroupMember(@Param("groupId") Long groupId, @Param("memberId") Long memberId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "delete from group_admins where group_id = :id")
    Integer deleteGroupAdmins(@Param("id") Long id);

    @Query(nativeQuery = true,
            value = "SELECT p.id FROM post p " +
                    "JOIN user u ON p.posted_by_user_id = u.id " +
                    "WHERE p.id IN (SELECT post_id FROM group_posts WHERE group_id = :groupId) " +
                    "AND p.is_deleted = false " +
                    "AND u.is_deleted = false")
    Optional<List<Long>> findPostsByGroupId(@Param("groupId") Long groupId);

    @Query(nativeQuery = true,
            value = "SELECT p.id FROM post p " +
                    "JOIN user u ON p.posted_by_user_id = u.id " +
                    "WHERE p.id IN (SELECT post_id FROM group_posts WHERE group_id = :groupId) " +
                    "AND p.is_deleted = false " +
                    "AND u.is_deleted = false " +
                    "ORDER BY p.creation_date ASC;\n")
    Optional<List<Long>> findPostsByGroupIdAsc(@Param("groupId") Long groupId);

    @Query(nativeQuery = true,
            value = "SELECT p.id FROM post p " +
                    "JOIN user u ON p.posted_by_user_id = u.id " +
                    "WHERE p.id IN (SELECT post_id FROM group_posts WHERE group_id = :groupId) " +
                    "AND p.is_deleted = false " +
                    "AND u.is_deleted = false " +
                    "ORDER BY p.creation_date DESC;\n")
    Optional<List<Long>> findPostsByGroupIdDesc(@Param("groupId") Long groupId);



    @Query(nativeQuery = true,
            value = "SELECT *\n" +
                    "FROM report r\n" +
                    "WHERE r.on_comment_id IN (\n" +
                    "    SELECT id\n" +
                    "    FROM comment c\n" +
                    "    WHERE c.belongs_to_post_id IN (\n" +
                    "        SELECT id\n" +
                    "        FROM post p\n" +
                    "        WHERE p.id IN (\n" +
                    "            SELECT post_id\n" +
                    "            FROM group_posts\n" +
                    "            WHERE group_id = :groupId\n" +
                    "        )\n" +
                    "    )\n" +
                    ") OR r.on_comment_id IN (\n" +
                    "    SELECT replies_to_comment_id\n" +
                    "    FROM comment c\n" +
                    "    WHERE c.belongs_to_post_id IN (\n" +
                    "        SELECT id\n" +
                    "        FROM post p\n" +
                    "        WHERE p.id IN (\n" +
                    "            SELECT post_id\n" +
                    "            FROM group_posts\n" +
                    "            WHERE group_id = :groupId\n" +
                    "        )\n" +
                    "    )\n" +
                    ")\n" +
                    "UNION\n" +
                    "SELECT *\n" +
                    "FROM report r\n" +
                    "WHERE r.on_post_id IN (\n" +
                    "    SELECT id\n" +
                    "    FROM post p\n" +
                    "    WHERE p.id IN (\n" +
                    "        SELECT post_id\n" +
                    "        FROM group_posts\n" +
                    "        WHERE group_id = :groupId\n" +
                    "    )\n" +
                    ");")
    Optional<List<Long>> findReportsByGroupId(@Param("groupId") Long groupId);

    @Query(nativeQuery = true,
            value = "select * from `group` where id in (select group_id from group_members where member_id = :memberId)")
    Optional<List<Group>> findGroupsByMemberId(@Param("memberId") Long memberId);


    @Query(nativeQuery = true,
            value = "select count(*) from group_members where group_id = :groupId and member_id = :userId")
    Integer findUserInGroup(@Param("groupId") Long groupId, @Param("userId") Long userId);

    @Query(nativeQuery = true,
            value = "select * from `group` where id in (select group_id from group_posts where post_id = :postId)")
    Optional<Group> checkIfPostInGroup(@Param("postId") Long postId);



    @Query(nativeQuery = true, value = "select distinct member_id from `group_members` where group_id = :groupId")
    Optional<List<Long>> findGroupMembers(@Param("groupId") Long groupId);

}
