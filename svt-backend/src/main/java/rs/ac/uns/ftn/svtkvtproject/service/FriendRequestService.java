package rs.ac.uns.ftn.svtkvtproject.service;

import rs.ac.uns.ftn.svtkvtproject.model.dto.FriendRequestDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.FriendRequest;
import java.util.List;

public interface FriendRequestService {

    FriendRequest findById(Long id);

    List<FriendRequest> findAllRequestsFromUser(Long userId);

    List<FriendRequest> findAllRequestsToUser(Long userId);

    FriendRequest createFriendRequest(FriendRequestDTO friendRequestDTO);

    FriendRequest updateFriendRequest(FriendRequest friendRequest);

    Integer deleteFriendRequest(Long id);
}
