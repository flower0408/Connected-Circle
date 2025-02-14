package rs.ac.uns.ftn.svtkvtproject.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import rs.ac.uns.ftn.svtkvtproject.model.dto.*;
import rs.ac.uns.ftn.svtkvtproject.model.entity.*;
import rs.ac.uns.ftn.svtkvtproject.security.TokenUtils;
import rs.ac.uns.ftn.svtkvtproject.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/groups")
public class GroupController {

    GroupService groupService;

    UserService userService;

    PostService postService;
    GroupRequestService groupRequestService;

    ReportService reportService;

    BannedService bannedService;

    AuthenticationManager authenticationManager;

    TokenUtils tokenUtils;

    private static final Logger logger = LogManager.getLogger(GroupController.class);

    @Autowired
    public GroupController(GroupService groupService, UserService userService, PostService postService, GroupRequestService groupRequestService, ReportService reportService,
                           BannedService bannedService,
                           AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        this.groupService = groupService;
        this.userService = userService;
        this.postService = postService;
        this.groupRequestService = groupRequestService;
        this.reportService = reportService;
        this.bannedService = bannedService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<GroupDTO>> getAll(@RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding all groups");
        List<Group> groups = groupService.findAll();
        List<GroupDTO> groupDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Group group: groups) {
            groupDTOS.add(new GroupDTO(group));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(groupDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> getOne(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding group for id: " + id);
        Group group = groupService.findById(Long.parseLong(id));
        if (group == null) {
            logger.error("Group not found for id: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating response");
        GroupDTO groupDTO = new GroupDTO(group);
        logger.info("Created and sent response");

        return new ResponseEntity<>(groupDTO, HttpStatus.OK);
    }

    @GetMapping("members/{groupId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<UserDTO>> getGroupMembers(@PathVariable String groupId, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Finding members for group with id: " + groupId);
        List<Long> membersIds = groupService.findMembersByGroupId(Long.parseLong(groupId));
        if (membersIds == null) {
            logger.error("Members not found for group with id: " + groupId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Long userId: membersIds) {
            User user1 = userService.findById(userId);
            UserDTO userDTO = new UserDTO(user1);
            userDTOS.add(userDTO);
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/adminGroup/{adminId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity addGroupAdminNew(@PathVariable String groupId, @PathVariable String adminId, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Adding group admin with id: " + adminId);
        boolean added = groupService.addGroupAdmin(Long.parseLong(groupId), Long.parseLong(adminId));
        if (!added) {
            logger.error("Admin couldn't be added to group with id: " + groupId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Successfully added group admin for group with id: " + groupId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("admins/{groupId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<UserDTO>> getGroupAdmins(@PathVariable String groupId, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Finding admins for group with id: " + groupId);
        List<Long> adminsIds = groupService.findAdminsByGroupId(Long.parseLong(groupId));
        if (adminsIds == null) {
            logger.error("Admins not found for group with id: " + groupId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Long userId: adminsIds) {
            User user1 = userService.findById(userId);
            UserDTO userDTO = new UserDTO(user1);
            userDTOS.add(userDTO);
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }



        @PersistenceContext
        private EntityManager entityManager;

    @PostMapping("/block-member")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> blockMember(@RequestHeader("authorization") String token, @RequestBody BlockMemberRequestDTO request) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Long memberId = request.getMemberId();
            Long adminId = request.getAdminId();

            User admin = userService.findById(adminId);
            User member = userService.findById(memberId);

            Query query = entityManager.createNativeQuery(
                    "SELECT gm.group_id " +
                            "FROM group_members gm " +
                            "JOIN group_admins ga ON gm.group_id = ga.group_id " +
                            "WHERE gm.member_id = :memberId " +
                            "AND ga.admin_id = :adminId");
            query.setParameter("memberId", memberId);
            query.setParameter("adminId", adminId);

            List<Object> results = query.getResultList();

            if (results.isEmpty()) {
                return ResponseEntity.badRequest().body("Admin is not associated with a group");
            }

            BigInteger bigIntegerGroupId = (BigInteger) results.get(0);
            Long groupId = bigIntegerGroupId.longValue();

            Group group = groupService.findById(groupId);

            if (group == null) {
                return ResponseEntity.badRequest().body("Group not found");
            }

            groupService.deleteGroupMember(groupId, memberId);

            Banned banned = new Banned();
            banned.setByAdmin(admin);
            banned.setTowardsUser(member);
            banned.setBlocked(true);
            banned.setTimestamp(LocalDate.now());
            banned.setDeleted(false);
            banned.setGroup(group);

            logger.info("Banned object before saving: " + banned.toString());

            bannedService.saveBanned(banned);

            logger.info("Member blocked successfully");

            return ResponseEntity.ok("Member blocked successfully");
        } catch (Exception e) {
            logger.error("An error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }




    @GetMapping("reports/{groupId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ReportDTO>> getReportsForGroup(@PathVariable String groupId, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Finding reports for group with id: " + groupId);
        List<Long> reportsIds = groupService.findReportsByGroupId(Long.parseLong(groupId));
        if (reportsIds == null) {
            logger.error("Reports not found for group with id: " + groupId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
            List<ReportDTO> reportDTOS = new ArrayList<>();
            logger.info("Creating response");
            for (Long reportId: reportsIds) {
                Report report = reportService.findById(reportId);
                ReportDTO reportDTO = new ReportDTO(report);
                reportDTOS.add(reportDTO);
            }
            logger.info("Created and sent response");

        return new ResponseEntity<>(reportDTOS, HttpStatus.OK);
    }




    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody @Validated GroupDTO newGroup, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating group from DTO");
        Group createdGroup = groupService.createGroup(newGroup);
        if (createdGroup == null) {
            logger.error("Group couldn't be created from DTO");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        logger.info("Creating response");
        GroupDTO groupDTO = new GroupDTO(createdGroup);
        logger.info("Created and sent response");

        return new ResponseEntity<>(groupDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupDTO> editGroup(@PathVariable String id, @RequestBody @Validated GroupDTO editedGroup,
                                              @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding original group for id: " + id);
        Group oldGroup = groupService.findById(Long.parseLong(id));
        if (oldGroup == null) {
            logger.error("Original group not found for id: " + id);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        logger.info("Applying changes");
        oldGroup.setName(editedGroup.getName());
        oldGroup.setDescription(editedGroup.getDescription());
        oldGroup.setCreationDate(LocalDateTime.parse(editedGroup.getCreationDate()));
        oldGroup.setSuspended(editedGroup.isSuspended());
        oldGroup.setSuspendedReason(editedGroup.getSuspendedReason());
        oldGroup = groupService.updateGroup(oldGroup);
        logger.info("Creating response");
        GroupDTO updatedGroup = new GroupDTO(oldGroup);
        logger.info("Created and sent response");

        return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity deleteGroup(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Deleting group with id: " + id);
        groupService.deleteGroupMembers(Long.parseLong(id));
        groupService.deleteGroupAdmins(Long.parseLong(id));
        Integer deleted = groupService.deleteGroup(Long.parseLong(id));
        if (deleted != 0) {
            logger.info("Successfully deleted group with id: " + id);
            return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
        }
        logger.error("Failed to delete group with id: " + id);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{groupId}/admin/{adminId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity addGroupAdmin(@PathVariable String groupId, @PathVariable String adminId, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Adding group admin with id: " + adminId);
        boolean added = groupService.addGroupAdmin(Long.parseLong(groupId), Long.parseLong(adminId)) &&
                        groupService.addGroupMember(Long.parseLong(groupId), Long.parseLong(adminId));
        if (!added) {
            logger.error("Admin couldn't be added to group with id: " + groupId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Successfully added group admin for group with id: " + groupId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{groupId}/member/{memberId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity addGroupMember(@PathVariable String groupId, @PathVariable String memberId, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Adding group member with id: " + memberId);
        Boolean added = groupService.addGroupMember(Long.parseLong(groupId), Long.parseLong(memberId));
        if (!added) {
            logger.error("Member couldn't be added to group with id: " + groupId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Successfully added group member for group with id: " + groupId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{groupId}/admin/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity deleteGroupAdmin(@PathVariable String groupId, @PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Deleting group admin with id: " + id + " for group with id: " + groupId);
        Integer deleted = groupService.deleteGroupAdmin(Long.parseLong(groupId), Long.parseLong(id));
        if (deleted != 0) {
            logger.info("Successfully deleted group admin with id: " + id + " for group with id: " + groupId);
            return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
        }
        logger.error("Failed to delete group admin with id: " + id + " for group with id: " + groupId);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}/group-requests")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<GroupRequestDTO>> getGroupRequests(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding group requests for group with id: " + id);
        List<GroupRequest> groupRequests = groupRequestService.findAllRequestsForGroup(Long.parseLong(id));
        List<GroupRequestDTO> groupRequestDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (GroupRequest groupRequest: groupRequests)
            groupRequestDTOS.add(new GroupRequestDTO(groupRequest));
        logger.info("Created and sent response");

        return new ResponseEntity<>(groupRequestDTOS, HttpStatus.OK);
    }

    @PatchMapping("/group-request")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupRequestDTO> updateGroupRequest(@RequestBody @Validated GroupRequestDTO groupRequestDTO, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding original group request for id: " + groupRequestDTO.getId());
        GroupRequest oldGroupRequest = groupRequestService.findById(groupRequestDTO.getId());
        if (oldGroupRequest == null) {
            logger.error("Original group request not found for id: " + groupRequestDTO.getId());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        logger.info("Adding new group member if request is approved");
        if (groupRequestDTO.getApproved())
            groupService.addGroupMember(groupRequestDTO.getForGroupId(), groupRequestDTO.getCreatedByUserId());
        logger.info("Applying changes");
        oldGroupRequest.setApproved(groupRequestDTO.getApproved());
        oldGroupRequest.setAt(LocalDateTime.parse(groupRequestDTO.getAt()));
        oldGroupRequest = groupRequestService.updateGroupRequest(oldGroupRequest);
        logger.info("Creating response");
        GroupRequestDTO newGroupRequest = new GroupRequestDTO(oldGroupRequest);
        logger.info("Created and sent response");

        return new ResponseEntity<>(newGroupRequest, HttpStatus.OK);
    }

    @DeleteMapping("/group-request/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity deleteGroupRequest(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Deleting group request with id: " + id);
        Integer deleted = groupRequestService.deleteGroupRequest(Long.parseLong(id));
        if (deleted != 0) {
            logger.info("Successfully deleted group request with id: " + id);
            return new ResponseEntity(deleted, HttpStatus.NO_CONTENT);
        }
        logger.error("Failed to delete group request with id: " + id);

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{id}/group-request")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GroupRequestDTO> createGroupRequest(@PathVariable String id, @RequestBody @Validated GroupRequestDTO newGroupRequest,
                                                              @RequestHeader("authorization") String token) {
        logger.info("Authorization check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating group request from DTO");
        GroupRequest createdGroupRequest = groupRequestService.createGroupRequest(newGroupRequest);
        if (createdGroupRequest == null) {
            logger.error("Group request couldn't be created from DTO");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        logger.info("Creating response");
        GroupRequestDTO groupRequestDTO = new GroupRequestDTO(createdGroupRequest);
        logger.info("Created and sent response");

        return new ResponseEntity<>(groupRequestDTO, HttpStatus.CREATED);
    }
}
