package rs.ac.uns.ftn.svtkvtproject.repository;

import rs.ac.uns.ftn.svtkvtproject.model.entity.Group;
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
            value = "delete from group_admins where group_id = :id")
    Integer deleteGroupAdmins(@Param("id") Long id);

    @Query(nativeQuery = true,
            value = "select id from post p where p.id in (select post_id from group_posts where group_id = :groupId)")
    Optional<List<Long>> findPostsByGroupId(@Param("groupId") Long groupId);

    @Query(nativeQuery = true,
            value = "select * from `group` where id in (select group_id from group_members where member_id = :memberId)")
    Optional<List<Group>> findGroupsByMemberId(@Param("memberId") Long memberId);

    @Query(nativeQuery = true,
            value = "select count(*) from group_members where group_id = :groupId and member_id = :userId")
    Integer findUserInGroup(@Param("groupId") Long groupId, @Param("userId") Long userId);

    @Query(nativeQuery = true,
            value = "select * from `group` where id in (select group_id from group_posts where post_id = :postId)")
    Optional<Group> checkIfPostInGroup(@Param("postId") Long postId);
}
