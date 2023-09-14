package rs.ac.uns.ftn.svtkvtproject.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtkvtproject.model.dto.BannedDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.*;
import rs.ac.uns.ftn.svtkvtproject.model.enums.ReportReason;
import rs.ac.uns.ftn.svtkvtproject.repository.BannedRepository;
import rs.ac.uns.ftn.svtkvtproject.repository.ReportRepository;
import rs.ac.uns.ftn.svtkvtproject.service.BannedService;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BannedServiceImpl implements BannedService {

    private BannedRepository bannedRepository;

    @Autowired
    public void setBannedRepository(BannedRepository bannedRepository) {
        this.bannedRepository = bannedRepository;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    private static final Logger logger = LogManager.getLogger(BannedServiceImpl.class);

    @Override
    public Banned findById(Long id) {
        Optional<Banned> banned = bannedRepository.findById(id);
        if (!banned.isEmpty())
            return banned.get();
        logger.error("Repository search for banned with id: " + id + " returned null");
        return null;
    }

    @Override
    public List<Banned> findAll() {
        return this.bannedRepository.findAll();
    }

    @Override
    public List<Banned> findAllGroup(Long groupId) {
        Optional<List<Banned>> bans = bannedRepository.findAllForGroup(groupId);
        if (!bans.isEmpty())
            return bans.get();
        logger.error("Repository search for reports  returned null");
        return null;
    }

    @Override
    public List<Banned> findAllAdmin() {
        Optional<List<Banned>> bans = bannedRepository.findAllForAdmin();
        if (!bans.isEmpty())
            return bans.get();
        logger.error("Repository search for reports  returned null");
        return null;
    }

    @Override
    public Banned createBanned(BannedDTO bannedDTO) {
        Optional<Banned> banned = bannedRepository.findById(bannedDTO.getId());

        if (banned.isPresent()) {
            logger.error("Banned with id: " + bannedDTO.getId() + " already exists in repository");
            return null;
        }

        Banned newBanned= new Banned();
        newBanned.setTimestamp(LocalDate.parse(bannedDTO.getTimestamp()));
        newBanned.setBlocked(bannedDTO.isBlocked());

        User user = userService.findById(bannedDTO.getByAdminId());

        if (user == null) {
            logger.error("User with id: " + bannedDTO.getByAdminId() +
                    ", that made the banned, was not found in the database");
            return null;
        }

        newBanned.setTowardsUser(user);
        User user1 = userService.findById(bannedDTO.getTowardsUserId());
        newBanned.setTowardsUser(user1);


        newBanned.setDeleted(false);
        newBanned = bannedRepository.save(newBanned);

        return newBanned;
    }

    @Override
    public Integer deleteBanned(Long id) {
        return bannedRepository.deleteBannedById(id);
    }

    @Override
    public Banned saveBanned(Banned banned) {
        return bannedRepository.save(banned);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void unblockUserAndSaveBanned(Long bannedId) {
        Banned banned = entityManager.find(Banned.class, bannedId);
        if (banned != null && banned.isBlocked() && banned.getTowardsUser() != null) {
            Long userId = banned.getTowardsUser().getId();
            int updatedRows = entityManager.createQuery(
                            "UPDATE User u SET u.isDeleted = false WHERE u.id = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();

            if (updatedRows > 0) {
                banned.setBlocked(false);
                saveBanned(banned);
            }
        }
    }

    @Override
    @Transactional
    public void unblockMemberAndSaveBanned(Long bannedId) {
        Banned banned = entityManager.find(Banned.class, bannedId);
        if (banned != null && banned.isBlocked() && banned.getTowardsUser() != null && banned.getGroup() != null) {
            Long memberId = banned.getTowardsUser().getId();
            Long groupId = banned.getGroup().getId();

            int updatedRows = entityManager.createNativeQuery(
                            "INSERT INTO group_members (group_id, member_id) VALUES (:groupId, :memberId)")
                    .setParameter("groupId", groupId)
                    .setParameter("memberId", memberId)
                    .executeUpdate();

            if (updatedRows > 0) {
                banned.setBlocked(false);
                saveBanned(banned);
            }
        }
    }

}
