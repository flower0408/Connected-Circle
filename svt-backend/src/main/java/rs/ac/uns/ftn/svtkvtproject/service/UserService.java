package rs.ac.uns.ftn.svtkvtproject.service;

import rs.ac.uns.ftn.svtkvtproject.model.dto.UserDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    User createUser(UserDTO userDTO);

    List<User> findAll();

}
