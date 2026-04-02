package com.example.BazaarTrackerBackend.security.user;


import com.example.BazaarTrackerBackend.model.entity.User;
import com.example.BazaarTrackerBackend.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;

import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            User user = userRepository.findByEmail(email);

            if (user == null) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }

            return new CustomUserDetails(user);

        } catch (Exception e) {
            throw new UsernameNotFoundException("Error fetching user", e);
        }
    }
}