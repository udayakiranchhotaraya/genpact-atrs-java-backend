package com.capstone.airlineticketreservationsystem.users.controllers;

import com.capstone.airlineticketreservationsystem.users.dtos.CreateUserRequest;
import com.capstone.airlineticketreservationsystem.users.dtos.SetPasswordRequest;
import com.capstone.airlineticketreservationsystem.users.dtos.UpdateUserRequest;
import com.capstone.airlineticketreservationsystem.users.dtos.UserDTO;
import com.capstone.airlineticketreservationsystem.users.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    // Step 1: User Registration (Start Onboarding)
    @PostMapping("/start-onboarding")
    public ResponseEntity<UserDTO> startUserOnboarding(@Valid @RequestBody CreateUserRequest createUserRequest) {
        UserDTO createdUser = userService.createUser(createUserRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Step 2: Verify Token and Set Password
    @PostMapping("/set-password")
    public ResponseEntity<Map<String, String>> verifyTokenAndSetPassword(
            @RequestParam String token,
            @Valid @RequestBody SetPasswordRequest setPasswordRequest) {

        userService.verifyTokenAndSetPassword(token, setPasswordRequest);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password set successfully");
        response.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(response);
    }

    // Current user endpoints
    @GetMapping(value = "/profile")
    public ResponseEntity<UserDTO> getCurrentUserProfile(HttpServletRequest request) {
        String userUUID = (String) request.getAttribute("userUUID");
        UserDTO user = userService.getUserByUUID(userUUID);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/profile")
    public ResponseEntity<UserDTO> updateCurrentUserProfile(
            HttpServletRequest request,
            @Valid @RequestBody UpdateUserRequest updateUserRequest) {

        String userUUID = (String) request.getAttribute("userUUID");
        UserDTO updatedUser = userService.updateUser(userUUID, updateUserRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping(value = "/password")
    public ResponseEntity<Map<String, String>> setCurrentUserPassword(
            HttpServletRequest request,
            @Valid @RequestBody SetPasswordRequest setPasswordRequest) {

        String userUUID = (String) request.getAttribute("userUUID");
        userService.setPassword(userUUID, setPasswordRequest);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password updated successfully");
        response.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteUser(HttpServletRequest request) {
        String userUUID = (String) request.getAttribute("userUUID");
        userService.deleteUser(userUUID);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        response.put("userUuid", userUUID);
        response.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(response);
    }

    // Admin-only endpoints (you can add @PreAuthorize later)
    // Keep existing endpoints for admin operations or other use cases
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/{userUUID}")
    public ResponseEntity<UserDTO> getUserByUUID(@PathVariable String userUUID) {
        UserDTO user = userService.getUserByUUID(userUUID);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

}
