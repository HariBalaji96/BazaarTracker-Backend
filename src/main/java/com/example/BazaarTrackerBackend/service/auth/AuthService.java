package com.example.BazaarTrackerBackend.service.auth;



import com.example.BazaarTrackerBackend.dto.auth.AuthResponse;
import com.example.BazaarTrackerBackend.dto.auth.LoginRequest;
import com.example.BazaarTrackerBackend.dto.auth.SignupRequest;
import com.example.BazaarTrackerBackend.model.entity.User;
import com.example.BazaarTrackerBackend.model.enums.UserRole;
import com.example.BazaarTrackerBackend.repository.user.UserRepository;
import com.example.BazaarTrackerBackend.security.jwt.JwtUtil;
import com.google.cloud.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // 🔐 SIGNUP
    public AuthResponse signup(SignupRequest request) throws Exception {

        // 🔹 Check existing user
        User existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser != null) {
            throw new RuntimeException("Email already registered");
        }

        // 🔹 Create user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setCompanyName(request.getCompanyName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(UserRole.ADMIN); // default role
        user.setActive(true);

        user.setCreatedAt(Timestamp.now());
        user.setUpdatedAt(Timestamp.now());

        userRepository.save(user);

        // 🔹 Generate JWT
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    // 🔐 LOGIN
    public AuthResponse login(LoginRequest request) throws Exception {

        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        // 🔹 Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 🔹 Generate token
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}