package rs.ac.uns.ftn.svtkvtproject.controller;

import rs.ac.uns.ftn.svtkvtproject.model.dto.GroupDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.GroupRequestDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Group;
import rs.ac.uns.ftn.svtkvtproject.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import rs.ac.uns.ftn.svtkvtproject.security.TokenUtils;
import rs.ac.uns.ftn.svtkvtproject.service.GroupRequestService;
import rs.ac.uns.ftn.svtkvtproject.service.GroupService;
import rs.ac.uns.ftn.svtkvtproject.service.PostService;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    AuthenticationManager authenticationManager;

    TokenUtils tokenUtils;

    private static final Logger logger = LogManager.getLogger(GroupController.class);

    @Autowired
    public GroupController(GroupService groupService, UserService userService, PostService postService, GroupRequestService groupRequestService,
                           AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        this.groupService = groupService;
        this.userService = userService;
        this.postService = postService;
        this.groupRequestService = groupRequestService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    @GetMapping()
    public ResponseEntity<List<GroupDTO>> getAll(@RequestHeader("authorization") String token) {
        logger.info("Authentication check");
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
    public ResponseEntity<GroupDTO> getOne(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
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

    @PostMapping("/add")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody @Validated GroupDTO newGroup, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
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
    public ResponseEntity<GroupDTO> editGroup(@PathVariable String id, @RequestBody @Validated GroupDTO editedGroup,
                                              @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
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
    public ResponseEntity deleteGroup(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
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
    public ResponseEntity addGroupAdmin(@PathVariable String groupId, @PathVariable String adminId, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
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
    public ResponseEntity addGroupMember(@PathVariable String groupId, @PathVariable String memberId, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
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
    public ResponseEntity deleteGroupAdmin(@PathVariable String groupId, @PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
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
    public ResponseEntity<List<GroupRequestDTO>> getGroupRequests(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
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
    public ResponseEntity<GroupRequestDTO> updateGroupRequest(@RequestBody @Validated GroupRequestDTO groupRequestDTO, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
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
    public ResponseEntity deleteGroupRequest(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
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
    public ResponseEntity<GroupRequestDTO> createGroupRequest(@PathVariable String id, @RequestBody @Validated GroupRequestDTO newGroupRequest,
                                                              @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
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
