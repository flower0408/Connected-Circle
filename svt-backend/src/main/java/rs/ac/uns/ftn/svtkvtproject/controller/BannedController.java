package rs.ac.uns.ftn.svtkvtproject.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<BannedDTO>> getAll(@RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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

    @GetMapping("/forGroup/{groupId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<BannedDTO>> getAllBanned(@PathVariable String groupId, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding all bans");
        List<Banned> bans = bannedService.findAllGroup(Long.parseLong(groupId));
        List<BannedDTO> bannedDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Banned banned: bans) {
            bannedDTOS.add(new BannedDTO(banned));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(bannedDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BannedDTO> getOne(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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
    public ResponseEntity deleteBanned(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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


    @PatchMapping("/unblock/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> unblockUserAndBanned(@PathVariable Long id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> unblockMemberAndBanned(@PathVariable Long id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
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


}
