package com.capstone.airlineticketreservationsystem.users.repositories;

import com.capstone.airlineticketreservationsystem.users.models.User;

import java.util.Optional;

public interface UserRepositoryDAO {
    public User save(User user);
    User update(User user);
    Optional<User> findByUUID(String userUUID);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    void updatePassword(User user);
}
