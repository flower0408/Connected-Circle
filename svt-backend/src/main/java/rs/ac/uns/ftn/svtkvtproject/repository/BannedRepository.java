package rs.ac.uns.ftn.svtkvtproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Banned;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Report;

import java.util.List;
import java.util.Optional;


@Repository
public interface BannedRepository extends JpaRepository<Banned, Long> {

    @Transactional
    Integer deleteBannedById(Long id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM banned WHERE group_id = :groupId  AND blocked = true;")
    Optional<List<Banned>> findAllForGroup(@Param("groupId") Long postId);

    @Query(nativeQuery = true,
            value = "SELECT * FROM banned WHERE blocked = true AND group_id IS NULL;")
    Optional<List<Banned>> findAllForAdmin();
}
