package com.maxwell.AbasteceLegal.controller;

import com.maxwell.AbasteceLegal.security.services.UserPrinciple;
import com.maxwell.AbasteceLegal.util.LoginData;
import com.maxwell.AbasteceLegal.model.Role;
import com.maxwell.AbasteceLegal.util.LoginResponse;
import com.maxwell.AbasteceLegal.util.RoleName;
import com.maxwell.AbasteceLegal.model.User;
import com.maxwell.AbasteceLegal.repository.RoleRepository;
import com.maxwell.AbasteceLegal.repository.UserRepository;
import com.maxwell.AbasteceLegal.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginData loginData) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginData.getUsername(),
                        loginData.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrinciple userPrinciple = (UserPrinciple)authentication.getPrincipal();

        String token = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new LoginResponse(token, userPrinciple.getId(), userPrinciple.getUsername(),
                userPrinciple.getName(), userPrinciple.getEmail()));
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@RequestBody User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            return new ResponseEntity<String>("Fail -> Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<String>("Fail -> Email is already in use!",
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        user.setPassword(encoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
        roles.add(userRole);
        
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok().body(user);
    }
}