package com.capstone.airlineticketreservationsystem.authentication.services;

import com.capstone.airlineticketreservationsystem.authentication.dtos.LoginRequest;
import com.capstone.airlineticketreservationsystem.authentication.dtos.LoginResponse;
import com.capstone.airlineticketreservationsystem.authentication.exceptions.AccountDeactivatedException;
import com.capstone.airlineticketreservationsystem.authentication.exceptions.InvalidCredentialsException;
import com.capstone.airlineticketreservationsystem.users.exceptions.UserNotFoundException;
import com.capstone.airlineticketreservationsystem.users.models.User;
import com.capstone.airlineticketreservationsystem.users.repositories.UserRepositoryDAO;
import com.capstone.airlineticketreservationsystem.utilities.JwtTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public AuthenticationService(UserRepositoryDAO userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    private final UserRepositoryDAO userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public LoginResponse authenticate(LoginRequest loginRequest) {

        // Find user by email or phone number
        User user = userRepository.findByEmail(loginRequest.getLoginId())
                .orElseGet(() -> userRepository.findByPhoneNumber(loginRequest.getLoginId())
                        .orElseThrow(() -> new UserNotFoundException("User not found with provided credentials")));

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid password");
        }
//        if (!loginRequest.getPassword().matches(user.getPasswordHash())) throw new InvalidCredentialsException("Invalid password");

        // Check if user is verified and active
        if (user.getDeleted()) {
            throw new AccountDeactivatedException("Account is deactivated");
        }

        // Determine role based on isAdmin field
        String role = user.getAdmin() ? "ROLE_ADMIN" : "ROLE_USER";

        // Generate token with UUID as subject and email as claim
        String accessToken = jwtTokenUtil.generateAccessToken(user.getUserUUID(), user.getEmail(), role);

        return new LoginResponse(accessToken);
    }
}
