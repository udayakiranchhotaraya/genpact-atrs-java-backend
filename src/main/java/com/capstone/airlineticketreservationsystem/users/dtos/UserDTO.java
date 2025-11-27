package com.capstone.airlineticketreservationsystem.users.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class UserDTO {

    public UserDTO() {
    }

    public UserDTO(String userUUID, String email, String firstName, String lastName, LocalDateTime createdAt) {
        this.userUUID = userUUID;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
    }

    public UserDTO(String userUUID, String email, String firstName, String lastName, String phoneNumber, LocalDate dateOfBirth, String passportNumber, String profilePictureURL, Boolean isAdmin, String frequentFlyerTier, LocalDateTime createdAt) {
        this.userUUID = userUUID;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.passportNumber = passportNumber;
        this.profilePictureURL = profilePictureURL;
        this.isAdmin = isAdmin;
        this.frequentFlyerTier = frequentFlyerTier;
        this.createdAt = createdAt;
    }

    public UserDTO(String userUUID, String email, String firstName, String lastName, String phoneNumber, LocalDate dateOfBirth, String passportNumber, String profilePictureURL, Boolean isAdmin, String frequentFlyerTier, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userUUID = userUUID;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.passportNumber = passportNumber;
        this.profilePictureURL = profilePictureURL;
        this.isAdmin = isAdmin;
        this.frequentFlyerTier = frequentFlyerTier;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private String userUUID;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String passportNumber;
    private String profilePictureURL;
    private Boolean isAdmin;
    private String frequentFlyerTier;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getFrequentFlyerTier() {
        return frequentFlyerTier;
    }

    public void setFrequentFlyerTier(String frequentFlyerTier) {
        this.frequentFlyerTier = frequentFlyerTier;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}