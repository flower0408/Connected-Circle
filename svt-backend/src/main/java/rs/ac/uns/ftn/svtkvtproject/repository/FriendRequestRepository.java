package rs.ac.uns.ftn.svtkvtproject.repository;

import rs.ac.uns.ftn.svtkvtproject.model.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query(nativeQuery = true,
            value = "select * from friend_request where from_user_id = :userId and is_deleted = false")
    Optional<List<FriendRequest>> findAllFromUser(@Param("userId") Long userId);

    @Query(nativeQuery = true,
            value = "select * from friend_request where to_user_id = :userId and is_deleted = false")
    Optional<List<FriendRequest>> findAllToUser(@Param("userId") Long userId);

    @Transactional
    Integer deleteFriendRequestById(Long id);
}
