package rs.ac.uns.ftn.svtkvtproject.service.implementation;

import rs.ac.uns.ftn.svtkvtproject.model.dto.FriendRequestDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.FriendRequest;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import rs.ac.uns.ftn.svtkvtproject.repository.FriendRequestRepository;
import rs.ac.uns.ftn.svtkvtproject.service.FriendRequestService;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    private FriendRequestRepository friendRequestRepository;

    @Autowired
    public void setFriendRequestRepository(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private static final Logger logger = LogManager.getLogger(FriendRequestServiceImpl.class);

    @Override
    public FriendRequest findById(Long id) {
        Optional<FriendRequest> friendRequest = friendRequestRepository.findById(id);
        if (!friendRequest.isEmpty())
            return friendRequest.get();
        logger.error("Repository search for friend request with id: " + id + " returned null");
        return null;
    }

    @Override
    public List<FriendRequest> findAllRequestsFromUser(Long userId) {
        Optional<List<FriendRequest>> friendRequests = friendRequestRepository.findAllFromUser(userId);
        if (!friendRequests.isEmpty())
            return friendRequests.get();
        logger.error("Repository search for friend requests from user with id: " + userId + " returned null");
        return null;
    }

    @Override
    public List<FriendRequest> findAllRequestsToUser(Long userId) {
        Optional<List<FriendRequest>> friendRequests = friendRequestRepository.findAllToUser(userId);
        if (!friendRequests.isEmpty())
            return friendRequests.get();
        logger.error("Repository search for friend requests to user with id: " + userId + " returned null");
        return null;
    }

    @Override
    public FriendRequest createFriendRequest(FriendRequestDTO friendRequestDTO) {
        Optional<FriendRequest> friendRequest = friendRequestRepository.findById(friendRequestDTO.getId());
        if (friendRequest.isPresent()) {
            logger.error("Friend request with id: " + friendRequestDTO.getId() + " already exists in repository");
            return null;
        }
        FriendRequest newFriendRequest = new FriendRequest();
        newFriendRequest.setCreatedAt(LocalDateTime.parse(friendRequestDTO.getCreatedAt()));
        if (friendRequestDTO.getAt() != null)
            newFriendRequest.setAt(LocalDateTime.parse(friendRequestDTO.getAt()));
        User fromUser = userService.findById(friendRequestDTO.getFromUserId());
        if (fromUser == null)
            return null;
        User toUser = userService.findById(friendRequestDTO.getToUserId());
        if (toUser == null)
            return null;
        newFriendRequest.setFromUser(fromUser);
        newFriendRequest.setToUser(toUser);
        newFriendRequest = friendRequestRepository.save(newFriendRequest);
        return newFriendRequest;
    }

    @Override
    public FriendRequest updateFriendRequest(FriendRequest friendRequest) {
        return friendRequestRepository.save(friendRequest);
    }

    @Override
    public Integer deleteFriendRequest(Long id) {
        return friendRequestRepository.deleteFriendRequestById(id);
    }
}
