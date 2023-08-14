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

    /*@Query(nativeQuery = true,
            value = "select * from `user`;")
    Optional<List<User>> findAllUsers();*/

    @Query(nativeQuery = true,
            value = "select * from `user` where is_deleted = false;")
    Optional<List<User>> findAllActiveUsers();
}

