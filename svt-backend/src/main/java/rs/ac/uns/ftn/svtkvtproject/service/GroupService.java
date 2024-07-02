package rs.ac.uns.ftn.svtkvtproject.service;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtkvtproject.model.dto.GroupDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Group;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupService {

    Group findById(Long id);

    Group findByName(String name);

    List<Group> findByCreationDate(LocalDateTime creationDate);

    List<Group> findAll();

    List<Long> findPostsByGroupId(Long id);

    List<Long> findPostsByGroupIdAsc(Long id);

    List<Long> findPostsByGroupIdDesc(Long id);

    List<Long> findReportsByGroupId(Long id);

    List<Group> findGroupsForUser(Long userId);

    Group checkIfPostInGroup(Long postId);

    Group createGroup(GroupDTO groupDTO, MultipartFile attachedFile);

    Group updateGroup(Group group);

    Integer deleteGroup(Long id);

    Boolean addGroupAdmin(Long groupId, Long adminId);

    Boolean addGroupMember(Long groupId, Long memberId);

    Integer deleteGroupAdmin(Long groupId, Long adminId);

    Integer deleteGroupMembers(Long id);

    Integer deleteGroupMember(Long groupId, Long memberId);

    Integer deleteGroupAdmins(Long id);

    Boolean checkUser(Long groupId, Long userId);

    List<Long> findMembersByGroupId(Long id);

    List<Long> findAdminsByGroupId(Long id);
}
