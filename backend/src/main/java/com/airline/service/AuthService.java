package com.airline.service;

import com.airline.dto.AuthDTOs;
import com.airline.entity.Role;
import com.airline.entity.User;
import com.airline.entity.enums.UserRoleEnum;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.RoleRepository;
import com.airline.repository.UserRepository;
import com.airline.security.JwtUtils;
import com.airline.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public AuthDTOs.JwtResponse login(AuthDTOs.LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        User user = userRepository.findByEmail(userDetails.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setLastLoginAt(ZonedDateTime.now());
        userRepository.save(user);

        return AuthDTOs.JwtResponse.builder()
            .token(jwt)
            .type("Bearer")
            .id(userDetails.getId())
            .email(userDetails.getEmail())
            .firstName(userDetails.getFirstName())
            .lastName(userDetails.getLastName())
            .roles(roles)
            .build();
    }

    @Transactional
    public AuthDTOs.UserProfileResponse register(AuthDTOs.RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already registered!");
        }

        Role customerRole = roleRepository.findByName(UserRoleEnum.ROLE_CUSTOMER)
            .orElseThrow(() -> new ResourceNotFoundException("Error: Customer Role not found."));

        Set<Role> roles = new HashSet<>();
        roles.add(customerRole);

        User user = User.builder()
            .email(registerRequest.getEmail())
            .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
            .firstName(registerRequest.getFirstName())
            .lastName(registerRequest.getLastName())
            .phoneNumber(registerRequest.getPhoneNumber())
            .dateOfBirth(registerRequest.getDateOfBirth())
            .gender(registerRequest.getGender())
            .nationality(registerRequest.getNationality())
            .passportNumber(registerRequest.getPassportNumber())
            .passportExpiry(registerRequest.getPassportExpiry())
            .isActive(true)
            .isEmailVerified(true)
            .roles(roles)
            .build();

        User savedUser = userRepository.save(user);

        return AuthDTOs.UserProfileResponse.builder()
            .id(savedUser.getId())
            .email(savedUser.getEmail())
            .firstName(savedUser.getFirstName())
            .lastName(savedUser.getLastName())
            .phoneNumber(savedUser.getPhoneNumber())
            .dateOfBirth(savedUser.getDateOfBirth())
            .gender(savedUser.getGender())
            .nationality(savedUser.getNationality())
            .passportNumber(savedUser.getPassportNumber())
            .passportExpiry(savedUser.getPassportExpiry())
            .roles(savedUser.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()))
            .isActive(savedUser.getIsActive())
            .build();
    }

    @Transactional(readOnly = true)
    public AuthDTOs.UserProfileResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return AuthDTOs.UserProfileResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .phoneNumber(user.getPhoneNumber())
            .dateOfBirth(user.getDateOfBirth())
            .gender(user.getGender())
            .nationality(user.getNationality())
            .passportNumber(user.getPassportNumber())
            .passportExpiry(user.getPassportExpiry())
            .roles(user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()))
            .isActive(user.getIsActive())
            .build();
    }
}
