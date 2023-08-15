package rs.ac.uns.ftn.svtkvtproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svtkvtproject.model.dto.*;
import rs.ac.uns.ftn.svtkvtproject.model.entity.FriendRequest;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Image;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import rs.ac.uns.ftn.svtkvtproject.security.TokenUtils;
import rs.ac.uns.ftn.svtkvtproject.service.FriendRequestService;
import rs.ac.uns.ftn.svtkvtproject.service.ImageService;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;
import rs.ac.uns.ftn.svtkvtproject.service.implementation.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api/users")
public class UserController {


    UserService userService;


    UserDetailsService userDetailsService;

    FriendRequestService friendRequestService;

    ImageService imageService;


    AuthenticationManager authenticationManager;


    TokenUtils tokenUtils;

    private static final Logger logger = LogManager.getLogger(UserController.class);

    //Ili preporucen nacin: Constructor Dependency Injection
    @Autowired
    public UserController(UserServiceImpl userService, AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService, FriendRequestService friendRequestService, ImageService imageService, TokenUtils tokenUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.friendRequestService = friendRequestService;
        this.imageService = imageService;
        this.tokenUtils = tokenUtils;
    }

    @GetMapping("/{userId}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> getOne(@PathVariable Long userId, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
       // User user = userService.findById(userId); /
        User user = userService.findByUsername(username);
        if (user == null) {
            //logger.error("User not found for token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding user with id: " + userId);
        User findUser = userService.findById(userId);
        if (findUser == null) {
            logger.error("User not found with id: " + userId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserDTO userDTO = new UserDTO(findUser);
        logger.info("Created and sent response");

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        User user2 = userService.findById(id);
        if (user2 != null) {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/edit")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> editUser(@RequestBody @Validated UserDTO editedUser, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        //User user1 = this.userService.findByUsername(user.getName());
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding original user with id: " + editedUser.getId());
        User oldUser = userService.findById(editedUser.getId());
        if (oldUser == null) {
            logger.error("Original user not found with id: " + editedUser.getId());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        logger.info("Applying changes of user");
        if (editedUser.getDisplayName() != null)
            oldUser.setDisplayName(editedUser.getDisplayName());
        if (editedUser.getDescription() != null)
            oldUser.setDescription(editedUser.getDescription());
        Image oldImage = imageService.findProfileImageForUser(user.getId());
        if (editedUser.getProfileImage() != null && oldImage == null)
            imageService.createImage(editedUser.getProfileImage());
        else if (editedUser.getProfileImage() != null){
            oldImage.setPath(editedUser.getProfileImage().getPath());
            imageService.updateImage(oldImage);
        }
        oldUser = userService.saveUser(oldUser);
        logger.info("Creating response");
        UserDTO updatedUser = new UserDTO(oldUser);
        logger.info("Created and sent response");

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("group/{id}/admins")
    public ResponseEntity<List<UserDTO>> getGroupAdmins(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding admins of group with id: " + id);
        List<User> users = userService.findGroupAdmins(Long.parseLong(id));
        List<UserDTO> userDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (User temp : users) {
            userDTOS.add(new UserDTO(temp));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @GetMapping("/user/{queryUsername}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> getOneByUsername(@PathVariable String queryUsername,  @RequestHeader("authorization") String token)  {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        //User user1 = this.userService.findByUsername(user.getName());
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding user with username: " + queryUsername);
        User findUser = userService.findByUsername(queryUsername);
        if (findUser == null) {
            logger.error("User not found with username: " + queryUsername);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating response");
        UserDTO userDTO = new UserDTO(findUser);
        logger.info("Created and sent response");

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> create(@RequestBody @Validated UserDTO newUser) {
        logger.info("Creating user from DTO");
        User createdUser = userService.createUser(newUser);
        if (createdUser == null) {
            logger.error("User couldn't be created from DTO");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        logger.info("Creating response");
        UserDTO userDTO = new UserDTO(createdUser);
        logger.info("Created and sent response");

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        logger.info("Checking user's username and password");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        logger.info("Putting user in security context");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Kreiraj token za tog korisnika
        logger.info("Creating token for user");
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user);
        long expiresIn = tokenUtils.getExpiredIn();
        //postavljanje poslednjeg pristupa aplikaciji
        logger.info("Setting last login time for user");
        User loggedInUser = userService.findByUsername(authenticationRequest.getUsername());
        loggedInUser.setLastLogin(LocalDateTime.now());
        userService.saveUser(loggedInUser);

        // Vrati token kao odgovor na uspesnu autentifikaciju
        logger.info("Setting last login time for user");
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    @GetMapping("/logout")
    public ResponseEntity logoutUser() {
        logger.info("Getting authentication from security context");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Checking if authentication is anonymous");
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            SecurityContextHolder.clearContext();
            logger.info("User successfully logged out");
            return new ResponseEntity("You have successfully logged out!", HttpStatus.OK);
        }

        logger.error("User is not authenticated and can't be logged out");
        return new ResponseEntity("User is not authenticated!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/change-password")
    public ResponseEntity<UserDTO> changePassword(@RequestBody @Validated UpdatePasswordDTO changePasswordRequest,
            @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Comparing hashes in request and database");
        String oldPassRequest = changePasswordRequest.getOldPassword();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(oldPassRequest, user.getPassword())) {
            logger.error("Hashes do not match");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Updating password for user with id: " + user.getId());
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        user = userService.saveUser(user);

        logger.info("You have successfully updated your password");
        return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestBody UserSearch userSearch, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding users that match names");
        List<User> users = userService.searchUsersByNames(userSearch.getFirstName(), userSearch.getLastName());
        List<UserDTO> userDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (User temp : users) {
            userDTOS.add(new UserDTO(temp));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @GetMapping("/all")
    //@PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll(@RequestHeader("authorization") String token)  {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        return this.userService.findAll();
    }

    @GetMapping("/whoami")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }

    @GetMapping("/friend-request")
    public ResponseEntity<List<FriendRequestDTO>> getFriendRequests(@RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding friend requests for user with id: " + user.getId());
        List<FriendRequest> friendRequestsFromUser = friendRequestService.findAllRequestsFromUser(user.getId());
        List<FriendRequest> friendRequestsToUser = friendRequestService.findAllRequestsToUser(user.getId());
        List<FriendRequestDTO> friendRequestDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (FriendRequest friendRequest: friendRequestsFromUser)
            friendRequestDTOS.add(new FriendRequestDTO(friendRequest));
        for (FriendRequest friendRequest: friendRequestsToUser)
            friendRequestDTOS.add(new FriendRequestDTO(friendRequest));
        logger.info("Created and sent response");

        return new ResponseEntity<>(friendRequestDTOS, HttpStatus.OK);
    }

    @PatchMapping("/friend-request")
    public ResponseEntity<FriendRequestDTO> updateFriendRequest(@RequestBody @Validated FriendRequestDTO friendRequestDTO, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding original friend request for id: " + friendRequestDTO.getId());
        FriendRequest oldFriendRequest = friendRequestService.findById(friendRequestDTO.getId());
        if (oldFriendRequest == null) {
            logger.error("Original friend request not found for id: " + friendRequestDTO.getId());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        logger.info("Applying changes");
        if (friendRequestDTO.isApproved())
            userService.createFriendship(friendRequestDTO.getFromUserId(), friendRequestDTO.getToUserId());
        oldFriendRequest.setApproved(friendRequestDTO.isApproved());
        oldFriendRequest.setAt(LocalDateTime.parse(friendRequestDTO.getAt()));
        oldFriendRequest = friendRequestService.updateFriendRequest(oldFriendRequest);
        logger.info("Creating response");
        FriendRequestDTO newFriendRequest = new FriendRequestDTO(oldFriendRequest);
        logger.info("Created and sent response");

        return new ResponseEntity<>(newFriendRequest, HttpStatus.OK);
    }

    @DeleteMapping("/friend-request/{id}")
    public ResponseEntity deleteFriendRequest(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Deleting friend request with id: " + id);
        Integer deleted = friendRequestService.deleteFriendRequest(Long.parseLong(id));
        if (deleted != 0) {
            logger.info("Successfully deleted friend request with id: " + id);
            return new ResponseEntity(deleted, HttpStatus.NO_CONTENT);
        }
        logger.error("Failed to delete friend request with id: " + id);

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{id}/friend-request")
    public ResponseEntity<Boolean> saveFriendRequest(@PathVariable String id, @RequestBody @Validated FriendRequestDTO friendRequestDTO,
                                                     @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating friend request from DTO");
        FriendRequest friendRequest = friendRequestService.createFriendRequest(friendRequestDTO);

        if (friendRequest == null) {
            logger.error("Friend request couldn't be created from DTO");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        logger.info("Successfully saved friend request for user with id: " + id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<UserDTO>> getUserFriends(@RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user1 = userService.findByUsername(username);
        if (user1 == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding friends of user with id: " + user1.getId());
        List<User> user = userService.findFriendsForUser(user1.getId());
        List<UserDTO> userDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (User temp: user) {
            userDTOS.add(new UserDTO(temp));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<ImageDTO> getProfileImage(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding profile image for user with id: " + id);
        Image image = imageService.findProfileImageForUser(Long.parseLong(id));
        if (image == null) {
            logger.info("Image not found for user with id: " + id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("Creating response");
        ImageDTO imageDTO = new ImageDTO(image);
        logger.info("Created and sent response");

        return new ResponseEntity<>(imageDTO, HttpStatus.OK);
    }

    @DeleteMapping("/image/{id}")
    public ResponseEntity deleteImage(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Deleting image with id: " + id);
        Integer deleted = imageService.deleteImage(Long.parseLong(id));
        if (deleted != 0) {
            logger.info("Successfully deleted image with id: " + id);
            return new ResponseEntity(deleted, HttpStatus.NO_CONTENT);
        }
        logger.error("Failed to delete image with id: " + id);

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
