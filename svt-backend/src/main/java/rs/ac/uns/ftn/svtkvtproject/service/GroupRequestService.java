package rs.ac.uns.ftn.svtkvtproject.service;

import rs.ac.uns.ftn.svtkvtproject.model.dto.GroupRequestDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.GroupRequest;

import java.util.List;

public interface GroupRequestService {

    GroupRequest findById(Long id);

    List<GroupRequest> findAllRequestsForGroup(Long groupId);

    GroupRequest createGroupRequest(GroupRequestDTO groupRequestDTO);

    GroupRequest updateGroupRequest(GroupRequest groupRequest);

    Integer deleteGroupRequest(Long id);
}
