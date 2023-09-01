package rs.ac.uns.ftn.svtkvtproject.service.implementation;

import rs.ac.uns.ftn.svtkvtproject.model.dto.GroupDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Group;
import rs.ac.uns.ftn.svtkvtproject.repository.GroupRepository;
import rs.ac.uns.ftn.svtkvtproject.service.GroupService;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    private GroupRepository groupRepository;

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private static final Logger logger = LogManager.getLogger(GroupServiceImpl.class);

    @Override
    public Group findById(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        if (!group.isEmpty())
            return group.get();
        logger.error("Repository search for group with id: " + id + " returned null");
        return null;
    }

    @Override
    public Group findByName(String name) {
        Optional<Group> group = groupRepository.findByName(name);
        if (!group.isEmpty())
            return group.get();
        logger.error("Repository search for group with name: " + name + " returned null");
        return null;
    }

    @Override
    public List<Group> findByCreationDate(LocalDateTime creationDate) {
        Optional<List<Group>> groups = groupRepository.findAllByCreationDate(creationDate);
        if (!groups.isEmpty())
            return groups.get();
        logger.error("Repository search for group created on date: " + creationDate.toString() + " returned null");
        return null;
    }

    @Override
    public List<Group> findAll() {
        return this.groupRepository.findAll();
    }

    @Override
    public List<Long> findPostsByGroupId(Long id) {
        Optional<List<Long>> postsIds = groupRepository.findPostsByGroupId(id);
        if (!postsIds.isEmpty())
            return postsIds.get();
        logger.error("Repository search for posts for group with id: " + id + " returned null");
        return null;
    }

    @Override
    public List<Long> findPostsByGroupIdAsc(Long id) {
        Optional<List<Long>> postsIds = groupRepository.findPostsByGroupIdAsc(id);
        if (!postsIds.isEmpty())
            return postsIds.get();
        logger.error("Repository search for posts for group with id: " + id + " returned null");
        return null;
    }


    @Override
    public List<Long> findPostsByGroupIdDesc(Long id) {
        Optional<List<Long>> postsIds = groupRepository.findPostsByGroupIdDesc(id);
        if (!postsIds.isEmpty())
            return postsIds.get();
        logger.error("Repository search for posts for group with id: " + id + " returned null");
        return null;
    }

    @Override
    public List<Long> findReportsByGroupId(Long id) {
        Optional<List<Long>> postsIds = groupRepository.findReportsByGroupId(id);
        if (!postsIds.isEmpty())
            return postsIds.get();
        logger.error("Repository search for reports for group with id: " + id + " returned null");
        return null;
    }

    @Override
    public List<Long> findMembersByGroupId(Long id) {
        Optional<List<Long>> membersIds = groupRepository.findGroupMembers(id);
        if (!membersIds.isEmpty())
            return membersIds.get();
        logger.error("Repository search for members for group with id: " + id + " returned null");
        return null;
    }


    @Override
    public List<Long> findAdminsByGroupId(Long id) {
        Optional<List<Long>> adminsIds = groupRepository.findGroupAdmins(id);
        if (!adminsIds.isEmpty())
            return adminsIds.get();
        logger.error("Repository search for admins for group with id: " + id + " returned null");
        return null;
    }

    @Override
    public List<Group> findGroupsForUser(Long userId) {
        Optional<List<Group>> groups = groupRepository.findGroupsByMemberId(userId);
        if (!groups.isEmpty())
            return groups.get();
        logger.error("Repository search for groups for user with id: " + userId + " returned null");
        return null;
    }

    @Override
    public Group checkIfPostInGroup(Long postId) {
        Optional<Group> group = groupRepository.checkIfPostInGroup(postId);
        if (!group.isEmpty())
            return group.get();
        logger.error("Repository search in groups for post with id: " + postId + " returned null");
        return null;
    }

    @Override
    public Group createGroup(GroupDTO groupDTO) {
        Optional<Group> group = groupRepository.findByName(groupDTO.getName());

        if (group.isPresent()) {
            logger.error("Group with id: " + groupDTO.getId() + " already exists in repository");
            return null;
        }

        Group newGroup = new Group();
        newGroup.setName(groupDTO.getName());
        newGroup.setDescription(groupDTO.getDescription());
        newGroup.setCreationDate(LocalDateTime.parse(groupDTO.getCreationDate()));
        newGroup.setSuspended(groupDTO.isSuspended());
        newGroup.setSuspendedReason(groupDTO.getSuspendedReason());
        newGroup.setDeleted(false);
        newGroup = groupRepository.save(newGroup);

        return newGroup;
    }

    @Override
    public Group updateGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Integer deleteGroup(Long id) {
        return groupRepository.deleteGroupById(id);
    }

    @Override
    public Boolean addGroupAdmin(Long groupId, Long adminId) {
        return groupRepository.addGroupAdmin(groupId, adminId) > 0;
    }

    @Override
    public Boolean addGroupMember(Long groupId, Long memberId) {
        return groupRepository.addGroupMember(groupId, memberId) > 0;
    }

    @Override
    public Integer deleteGroupAdmin(Long groupId, Long adminId) {
        return groupRepository.deleteGroupAdmin(groupId, adminId);
    }

    @Override
    public Integer deleteGroupMembers(Long id) {
        return groupRepository.deleteGroupMembers(id);
    }

    @Override
    public Integer deleteGroupMember(Long groupId, Long memberId) {
        return groupRepository.deleteGroupMember(groupId, memberId);
    }

    @Override
    public Integer deleteGroupAdmins(Long id) {
        return groupRepository.deleteGroupAdmins(id);
    }

    @Override
    public Boolean checkUser(Long groupId, Long userId) {
        return (groupRepository.findUserInGroup(groupId, userId) > 0 || userService.checkUserIsAdmin(userId));
    }

}
