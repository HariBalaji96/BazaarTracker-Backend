package com.example.BazaarTrackerBackend.service.user;

import com.example.BazaarTrackerBackend.dto.user.*;
import com.example.BazaarTrackerBackend.model.entity.User;
import com.example.BazaarTrackerBackend.repository.user.UserRepository;
import com.example.BazaarTrackerBackend.exception.CustomException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ GET CURRENT USER
    public UserProfileResponse getCurrentUser() throws Exception {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new CustomException("User not found", 404);
        }

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    // ✅ UPDATE PROFILE
    public UserProfileResponse updateProfile(UpdateProfileRequest request) throws Exception {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new CustomException("User not found", 404);
        }

        user.setName(request.getName());

        userRepository.save(user);

        return getCurrentUser();
    }

    // 🔐 CHANGE PASSWORD
    public void changePassword(ChangePasswordRequest request) throws Exception {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new CustomException("Invalid old password", 400);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }
}