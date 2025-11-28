package com.capstone.airlineticketreservationsystem.users.services;

import com.capstone.airlineticketreservationsystem.users.dtos.CreateUserRequest;
import com.capstone.airlineticketreservationsystem.users.dtos.SetPasswordRequest;
import com.capstone.airlineticketreservationsystem.users.dtos.UpdateUserRequest;
import com.capstone.airlineticketreservationsystem.users.exceptions.UserAlreadyExistsException;
import com.capstone.airlineticketreservationsystem.users.dtos.UserDTO;
import com.capstone.airlineticketreservationsystem.users.exceptions.UserNotFoundException;
import com.capstone.airlineticketreservationsystem.users.models.FrequentFlyerTier;
import com.capstone.airlineticketreservationsystem.users.models.User;
import com.capstone.airlineticketreservationsystem.users.repositories.UserRepositoryDAO;
import com.capstone.airlineticketreservationsystem.utilities.EmailService;
import com.capstone.airlineticketreservationsystem.utilities.JwtTokenUtil;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService {

    public UserService(JwtTokenUtil jwtTokenUtil, UserRepositoryDAO userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    private final UserRepositoryDAO userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtTokenUtil jwtTokenUtil;

    public UserDTO createUser(CreateUserRequest createUserRequest) {

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + createUserRequest.getEmail() + " already exists");
        }

        User user = new User(
                createUserRequest.getEmail(),
                "TEMPORARY_PASSWORD", // Will be updated in step 2
                createUserRequest.getFirstName(),
                createUserRequest.getLastName()
        );

        User savedUser = userRepository.save(user);

        // Send verification email
        try {
            emailService.sendHtmlVerificationEmail(savedUser.getEmail(), savedUser.getUserUUID());
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }

        return new UserDTO(
                savedUser.getUserUUID(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getPhoneNumber(),
                savedUser.getDateOfBirth(),
                savedUser.getPassportNumber(),
                savedUser.getProfilePictureURL(),
                savedUser.getAdmin(),
                savedUser.getFrequentFlyerTier().name(),
                savedUser.getCreatedAt()
        );
    }

    public UserDTO getUserByUUID(String USER_UUID) {
        User user = userRepository.findByUUID(USER_UUID)
                .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + USER_UUID));
        return convertToDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return convertToDTO(user);
    }

    public UserDTO updateUser(String USER_UUID, UpdateUserRequest updateUserRequest) {
        // Finding the existing user
        User existingUser = userRepository.findByUUID(USER_UUID)
                .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + USER_UUID));

        // Manually checking and updating each provided field
        if (updateUserRequest.getEmail() != null) {
            // Check if new email already exists (excluding current user)
            if (!existingUser.getEmail().equals(updateUserRequest.getEmail()) &&
                    userRepository.existsByEmail(updateUserRequest.getEmail())) {
                throw new UserAlreadyExistsException("User with email " + updateUserRequest.getEmail() + " already exists");
            }
            existingUser.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getFirstName() != null) {
            existingUser.setFirstName(updateUserRequest.getFirstName());
        }
        if (updateUserRequest.getLastName() != null) {
            existingUser.setLastName(updateUserRequest.getLastName());
        }
        if (updateUserRequest.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(updateUserRequest.getPhoneNumber());
        }
        if (updateUserRequest.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(updateUserRequest.getDateOfBirth());
        }
        if (updateUserRequest.getPassportNumber() != null) {
            existingUser.setPassportNumber(updateUserRequest.getPassportNumber());
        }
        if (updateUserRequest.getProfilePictureURL() != null) {
            existingUser.setProfilePictureURL(updateUserRequest.getProfilePictureURL());
        }
        if (updateUserRequest.getAdmin() != null) {
            existingUser.setAdmin(updateUserRequest.getAdmin());
        }
        if (updateUserRequest.getFrequentFlyerTier() != null) {
            existingUser.setFrequentFlyerTier(FrequentFlyerTier.valueOf(updateUserRequest.getFrequentFlyerTier()));
        }

        User updatedUser = userRepository.update(existingUser);
        return convertToDTO(updatedUser);
    }

    public void setPassword(String USER_UUID, SetPasswordRequest setPasswordRequest) {
        User user = userRepository.findByUUID(USER_UUID)
                .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + USER_UUID));

        // Encode and set the new password
        user.setPasswordHash(passwordEncoder.encode(setPasswordRequest.getPassword()));
        userRepository.updatePassword(user);
    }

    public void verifyTokenAndSetPassword(String token, SetPasswordRequest setPasswordRequest) {
        String userUUID = jwtTokenUtil.validateTokenAndGetUserUUID(token);
        User user = userRepository.findByUUID(userUUID)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Update password logic here
        user.setPasswordHash(passwordEncoder.encode(setPasswordRequest.getPassword()));
        userRepository.updatePassword(user);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getUserUUID(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getPassportNumber(),
                user.getProfilePictureURL(),
                user.getAdmin(),
                user.getFrequentFlyerTier().name(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
