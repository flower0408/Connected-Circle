package rs.ac.uns.ftn.svtkvtproject.service;

import rs.ac.uns.ftn.svtkvtproject.model.dto.UserDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;

import java.util.List;

public interface UserService {

    User findByUsername(String username);
    User findById(Long id);
    User createUser(UserDTO userDTO);

    User saveUser(User user);

    Integer deleteUser(Long id);

    List<User> findAll();

    List<User> findGroupAdmins(Long groupId);

    Boolean checkUserIsAdmin(Long id);

    List<User> searchUsersByNames(String firstName, String lastName);

}
