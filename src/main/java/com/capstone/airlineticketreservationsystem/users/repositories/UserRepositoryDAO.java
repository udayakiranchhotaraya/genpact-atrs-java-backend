package com.capstone.airlineticketreservationsystem.users.repositories;

import com.capstone.airlineticketreservationsystem.users.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryDAO {
    public User save(User user);
    List<User> findAll();
    User update(User user);
    Optional<User> findByUUID(String userUUID);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Boolean existsByEmail(String email);
    void updatePassword(User user);
    int softDeleteByUUID(String userUUID);
    Optional<Long> findIdByUUID(String userUUID);

}
