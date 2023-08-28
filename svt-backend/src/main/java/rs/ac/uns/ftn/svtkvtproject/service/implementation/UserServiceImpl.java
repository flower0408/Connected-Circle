package rs.ac.uns.ftn.svtkvtproject.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtkvtproject.model.dto.UserDTO;
import rs.ac.uns.ftn.svtkvtproject.model.enums.Role;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import rs.ac.uns.ftn.svtkvtproject.repository.UserRepository;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;


    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findFirstByUsername(username);
        if (!user.isEmpty())
            return user.get();
        logger.error("Repository search for user with username: " + username + " returned null");
        return null;
    }
    @Override
    public User findById(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id);
    }

    @Override
    public User createUser(UserDTO userDTO) {

        Optional<User> user = userRepository.findFirstByUsername(userDTO.getUsername());

        if(user.isPresent()){
            logger.error("User with id: " + userDTO.getId() + " already exists in repository");
            return null;
        }

        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setEmail(userDTO.getEmail());
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setAdmin(false);
        newUser.setDeleted(false);
        newUser.setRole(Role.USER);
        newUser = userRepository.save(newUser);

        return newUser;
    }

    /*@Override
    public List<User> findAll() {
        Optional<List<User>> users = userRepository.findAllUsers();
        if (!users.isEmpty())
            return users.get();
        logger.error("Repository search for all users returned null");
        return null;
    }*/

    @Override
    public List<User> findAll() {
        return this.userRepository.findAllActiveUsers().orElse(Collections.emptyList());
    }



    @Override
    public List<User> findGroupAdmins(Long groupId) {
        Optional<List<User>> users = userRepository.findGroupAdmins(groupId);
        if (!users.isEmpty())
            return users.get();
        logger.error("Repository search for group admins for group with id: " + groupId + " returned null");
        return null;
    }


    @Override
    public List<User> searchUsersByNames(String firstName, String lastName) {
        Optional<List<User>> users = userRepository.findUsersByFirstAndLastName(firstName, lastName);
        if (!users.isEmpty())
            return users.get();
        logger.error("Repository search for users with provided query returned null");
        return null;
    }

    @Override
    public Integer createFriendship(Long userId, Long friendId) {
        return userRepository.saveFriendship(userId, friendId);
    }

    @Override
    public List<User> findFriendsForUser(Long userId) {
        Optional<List<User>> users = userRepository.findFriendsByUserId(userId);
        if (!users.isEmpty())
            return users.get();
        logger.error("Repository search for friends of user with id: " + userId + " returned null");
        return null;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Integer deleteUser(Long id) {
        return userRepository.deleteUserById(id);
    }

    @Override
    public Boolean checkUserIsAdmin(Long id) {
        return userRepository.checkIfUserIsAdmin(id).isPresent();
    }

}
