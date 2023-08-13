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
    public User createUser(UserDTO userDTO) {

        Optional<User> user = userRepository.findFirstByUsername(userDTO.getUsername());

        if(user.isPresent()){
            logger.error("User with id: " + userDTO.getId() + " already exists in repository");
            return null;
        }

        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRole(Role.USER);
        newUser = userRepository.save(newUser);

        return newUser;
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }
}
