package rs.ac.uns.ftn.svtkvtproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByIdAndIsDeletedFalse(Long id);

    Optional<User> findFirstByUsername(String username);
    @Query(nativeQuery = true,
            value = "select * from `user` where id in (select admin_id from group_admins where group_id = :groupId)")
    Optional<List<User>> findGroupAdmins(@Param("groupId") Long groupId);

    @Query(nativeQuery = true,
            value = "select * from `user` " +
                    "where (first_name like concat('%', :firstName, '%') or last_name like concat('%', :firstName, '%') or " +
                    "first_name like concat('%', :lastName, '%') or last_name like concat('%', :lastName, '%')) " +
                    "and is_deleted = false;")
    Optional<List<User>> findUsersByFirstAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Transactional
    @Modifying
    Integer deleteUserById(Long id);

    @Query(nativeQuery = true,
            value = "select * from `user` where id = :id and is_admin = true and is_deleted = false;")
    Optional<User> checkIfUserIsAdmin(@Param("id") Long id);

    @Query(nativeQuery = true,
            value = "select * from `user` where is_deleted = false;")
    Optional<List<User>> findAllActiveUsers();

    @Query(nativeQuery = true,
            value = "select * from `user` where id in (select user_id from user_friends where friend_id = :userId)" +
                    "or id in (select friend_id from user_friends where user_id = :userId)")
    Optional<List<User>> findFriendsByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "insert into user_friends(user_id, friend_id) values (:userId, :friendId);")
    Integer saveFriendship(@Param("userId") Long userId, @Param("friendId") Long friendId);


}

