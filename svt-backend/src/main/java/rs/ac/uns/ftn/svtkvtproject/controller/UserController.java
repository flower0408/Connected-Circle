package rs.ac.uns.ftn.svtkvtproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svtkvtproject.model.dto.JwtAuthenticationRequest;
import rs.ac.uns.ftn.svtkvtproject.model.dto.UserDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.UserTokenState;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import rs.ac.uns.ftn.svtkvtproject.security.TokenUtils;
import rs.ac.uns.ftn.svtkvtproject.service.UserService;
import rs.ac.uns.ftn.svtkvtproject.service.implementation.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("api/users")
public class UserController {


    UserService userService;


    UserDetailsService userDetailsService;


    AuthenticationManager authenticationManager;


    TokenUtils tokenUtils;

    private static final Logger logger = LogManager.getLogger(UserController.class);

     //Ili preporucen nacin: Constructor Dependency Injection
    @Autowired
    public UserController(UserServiceImpl userService, AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService, TokenUtils tokenUtils){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> create(@RequestBody @Validated UserDTO newUser){
        logger.info("Creating user from DTO");
        User createdUser = userService.createUser(newUser);

        if(createdUser == null){
            logger.error("User couldn't be created from DTO");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        logger.info("Creating response");
        UserDTO userDTO = new UserDTO(createdUser);
        logger.info("Created and sent response");

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {

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

        // Vrati token kao odgovor na uspesnu autentifikaciju
        logger.info("Setting last login time for user");
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.userService.findAll();
    }

    @GetMapping("/whoami")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }
}
