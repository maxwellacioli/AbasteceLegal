package com.maxwell.AbasteceLegal.security.services;

import com.maxwell.AbasteceLegal.model.User;
import com.maxwell.AbasteceLegal.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional op =  userRepository.findByUsername(username);

        if(op.isPresent()) {
            User user = (User) op.get();
            user.setAuthorities(user.getAuthorities());
            return user;
        } else {
            throw new UsernameNotFoundException("User Not Found with -> username : " + username);
        }
    }
}