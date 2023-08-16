package rs.ac.uns.ftn.svtkvtproject.repository;

import rs.ac.uns.ftn.svtkvtproject.model.entity.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long> {

    @Query(nativeQuery = true,
            value = "select * from group_request where for_group_id = :groupId and is_deleted = false")
    Optional<List<GroupRequest>> findAllByForGroup(@Param("groupId") Long groupId);

    @Transactional
    Integer deleteGroupRequestById(Long id);
}
