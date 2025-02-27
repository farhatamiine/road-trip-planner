package com.amine.roadtripplanner.Service;

import com.amine.roadtripplanner.Dto.request.LoginRequest;
import com.amine.roadtripplanner.Dto.request.RegisterRequest;
import com.amine.roadtripplanner.Dto.response.AuthResponse;
import com.amine.roadtripplanner.Entities.Role;
import com.amine.roadtripplanner.Entities.User;
import com.amine.roadtripplanner.Exception.AuthenticationException;
import com.amine.roadtripplanner.Repositories.RoleRepository;
import com.amine.roadtripplanner.Repositories.UserRepository;
import com.amine.roadtripplanner.Security.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenUtil jwtTokenUtil,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }


    @Transactional
    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new AuthenticationException("Username is already taken");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AuthenticationException("Email is already in use");
        }

        // Get user role
        Role userRole = roleRepository.findRoleByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));

        // Create new user
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .roles(new HashSet<>(Collections.singletonList(userRole)))
                .isEnabled(true)
                .isNonLocked(true)
                .isNonExpired(true)
                .isCredentialsNonExpired(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        // Generate JWT token
        String token = jwtTokenUtil.generateToken(user);

        return new AuthResponse(token, user.getUsername());
    }


    @Transactional
    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        // Update last login time
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // Generate JWT token
        String token = jwtTokenUtil.generateToken(user);

        return new AuthResponse(token, user.getUsername());
    }
}
