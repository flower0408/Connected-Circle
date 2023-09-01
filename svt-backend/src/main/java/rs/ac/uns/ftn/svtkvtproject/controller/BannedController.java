package rs.ac.uns.ftn.svtkvtproject.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svtkvtproject.model.dto.BannedDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.ReportDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Banned;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Report;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import rs.ac.uns.ftn.svtkvtproject.security.TokenUtils;
import rs.ac.uns.ftn.svtkvtproject.service.BannedService;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/banned")
public class BannedController {

    BannedService bannedService;

    UserService userService;

    AuthenticationManager authenticationManager;

    TokenUtils tokenUtils;

    private static final Logger logger = LogManager.getLogger(BannedController.class);

    @Autowired
    public void BannedController(BannedService bannedService, UserService userService, AuthenticationManager authenticationManager, TokenUtils tokenUtils){
        this.bannedService = bannedService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    @GetMapping()
    public ResponseEntity<List<BannedDTO>> getAll(@RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding all bans");
        List<Banned> bans = bannedService.findAllAdmin();
        List<BannedDTO> bannedDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Banned banned: bans) {
            bannedDTOS.add(new BannedDTO(banned));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(bannedDTOS, HttpStatus.OK);
    }

    @GetMapping("/forGroup")
    public ResponseEntity<List<BannedDTO>> getAllBanned(@RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding all bans");
        List<Banned> bans = bannedService.findAllGroup();
        List<BannedDTO> bannedDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Banned banned: bans) {
            bannedDTOS.add(new BannedDTO(banned));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(bannedDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BannedDTO> getOne(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding banned for id: " + id);
        Banned banned = bannedService.findById(Long.parseLong(id));
        if (banned == null) {
            logger.error("Banned not found for id: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating response");
        BannedDTO bannedDTO = new BannedDTO(banned);
        logger.info("Created and sent response");

        return new ResponseEntity<>(bannedDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteReport(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found for token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Deleting banned with id: " + id);
        Integer deleted = bannedService.deleteBanned(Long.parseLong(id));
        if (deleted != 0) {
            logger.info("Successfully deleted banned with id: " + id);
            return new ResponseEntity(deleted, HttpStatus.NO_CONTENT);
        }
        logger.error("Failed to delete banned with id: " + id);

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @PatchMapping("/unblock/{id}")
    public ResponseEntity<?> unblockUserAndBanned(@PathVariable Long id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found for token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        bannedService.unblockUserAndSaveBanned(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/unblockMember/{id}")
    public ResponseEntity<?> unblockMemberAndBanned(@PathVariable Long id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found for token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        bannedService.unblockMemberAndSaveBanned(id);
        return ResponseEntity.ok().build();
    }


   /* @Transactional
    @PatchMapping("/edit/{id}")
    public ResponseEntity<BannedDTO> editBanned(
            @PathVariable String id,
            @RequestBody @Validated BannedDTO editedBanned,
            @RequestHeader("authorization") String token
    ) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Finding original banned with id: " + editedBanned.getId());
        Banned oldBanned = bannedService.findById(Long.parseLong(id));

        oldBanned.setBlocked(editedBanned.isBlocked());

        // Load the towardsUser eagerly only when needed
        User towardsUser = userService.findById(oldBanned.getTowardsUserId());
        if (towardsUser != null) {
            if (!oldBanned.isBlocked()) {
                towardsUser.setDeleted(false); // Update isDeleted status
            }
            userService.saveUser(towardsUser);
            logger.info("User with ID " + towardsUser.getId() + " is no longer deleted.");
        }
        oldBanned = bannedService.saveBanned(oldBanned);

        logger.info("Creating response");
        BannedDTO updatedBanned = new BannedDTO(oldBanned);
        logger.info("Created and sent response");

        return new ResponseEntity<>(updatedBanned, HttpStatus.OK);
    }*/
   /*@Transactional
   @PatchMapping("/edit/{id}")
   public ResponseEntity<BannedDTO> editBanned(
           @PathVariable String id,
           @RequestBody @Validated BannedDTO editedBanned,
           @RequestHeader("authorization") String token
   ) {
       logger.info("Authentication check");
       String cleanToken = token.substring(7);
       String username = tokenUtils.getUsernameFromToken(cleanToken);
       User user = userService.findByUsername(username);
       if (user == null) {
           logger.error("User not found with token: " + cleanToken);
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }

       logger.info("Finding original banned with id: " + editedBanned.getId());
       Banned oldBanned = bannedService.findById(Long.parseLong(id));

       boolean oldBlockedStatus = oldBanned.isBlocked();
       oldBanned.setBlocked(editedBanned.isBlocked());

       // If the block status has changed (unblocked), update user isDeleted status
       if (oldBlockedStatus && !editedBanned.isBlocked()) {
           User towardsUser = userService.findById(oldBanned.getTowardsUserId());
           if (towardsUser != null)  {
               towardsUser.setDeleted(false);
               userService.saveUser(towardsUser);
               logger.info("User with ID " + towardsUser.getId() + " is no longer deleted.");
           }
       }

       oldBanned = bannedService.saveBanned(oldBanned);

       logger.info("Creating response");
       BannedDTO updatedBanned = new BannedDTO(oldBanned);
       logger.info("Created and sent response");

       return new ResponseEntity<>(updatedBanned, HttpStatus.OK);
   }*/



}
