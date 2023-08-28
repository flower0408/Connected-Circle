package rs.ac.uns.ftn.svtkvtproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Banned;


@Repository
public interface BannedRepository extends JpaRepository<Banned, Long> {

    @Transactional
    Integer deleteBannedById(Long id);
}
