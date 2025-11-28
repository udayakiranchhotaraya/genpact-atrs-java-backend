package com.capstone.airlineticketreservationsystem.users.repositories;

import com.capstone.airlineticketreservationsystem.users.models.FrequentFlyerTier;
import com.capstone.airlineticketreservationsystem.users.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static com.capstone.airlineticketreservationsystem.utilities.UUIDV7Generator.generateUUIDV7;

public class UserRepositoryImplDAO implements UserRepositoryDAO {

    public UserRepositoryImplDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User save(User user) {

        String sql = """
                INSERT INTO users (
                    users_uuid, email, password_hash, first_name, last_name,
                    phone_number, date_of_birth, passport_number, profile_picture_url,
                    is_admin, frequent_flyer_tier, is_deleted
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (user.getUserUUID() == null) {
            user.setUserUUID(generateUUIDV7().toString());
        }

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, user.getUserUUID());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPasswordHash());
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setString(6, user.getPhoneNumber());

            if (user.getDateOfBirth() != null) {
                preparedStatement.setDate(7, java.sql.Date.valueOf(user.getDateOfBirth()));
            } else {
                preparedStatement.setNull(7, java.sql.Types.DATE);
            }

            preparedStatement.setString(8, user.getPassportNumber());
            preparedStatement.setString(9, user.getProfilePictureURL());
            preparedStatement.setBoolean(10, user.getAdmin());
            preparedStatement.setString(11, user.getFrequentFlyerTier().name());
            preparedStatement.setBoolean(12, user.getDeleted());

            return preparedStatement;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            user.setId(key.longValue());

            String selectSql = "SELECT created_at FROM users WHERE id = ?";
            return jdbcTemplate.queryForObject(selectSql, (rs, rowNum) -> {
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return user;
            }, key.longValue());
        }
        throw new RuntimeException("Failed to insert user and retrieve generated ID.");
    }

    @Override
    public User update(User user) {
        String sql = """
                UPDATE users 
                SET 
                    email = ?, 
                    first_name = ?, 
                    last_name = ?, 
                    phone_number = ?, 
                    date_of_birth = ?, 
                    passport_number = ?, 
                    profile_picture_url = ?, 
                    is_admin = ?, 
                    frequent_flyer_tier = ?, 
                    is_deleted = ?, 
                    updated_at = CURRENT_TIMESTAMP 
                WHERE users_uuid = ?
            """;

        int rowsAffected = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getDateOfBirth() != null ? java.sql.Date.valueOf(user.getDateOfBirth()) : null,
                user.getPassportNumber(),
                user.getProfilePictureURL(),
                user.getAdmin(),
                user.getFrequentFlyerTier().name(),
                user.getDeleted(),
                user.getUserUUID()
        );

        if (rowsAffected > 0) {
            String selectSql = "SELECT created_at, updated_at FROM users WHERE users_uuid = ?";
            return jdbcTemplate.queryForObject(selectSql, (rs, rowNum) -> {
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                return user;
            }, user.getUserUUID());
        }
        throw new RuntimeException("Failed to update user with UUID: " + user.getUserUUID());
    }

    @Override
    public void updatePassword(User user) {
        String sql = "UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE users_uuid = ?";

        int rowsAffected = jdbcTemplate.update(sql,
                user.getPasswordHash(),
                user.getUserUUID()
        );

        if (rowsAffected == 0) {
            throw new RuntimeException("Failed to update password for user with UUID: " + user.getUserUUID());
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users WHERE is_deleted = false ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public Optional<User> findByUUID(String userUUID) {
        try {
            String sql = "SELECT * FROM users WHERE users_uuid = ? AND is_deleted = false";
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), userUUID);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            String sql = "SELECT * FROM users WHERE email = ? AND is_deleted = false";
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        try {
            String sql = "SELECT * FROM users WHERE phone_number = ? AND is_deleted = false";
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), phoneNumber);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND is_deleted = false";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public int softDeleteByUUID(String userUUID) {
        String sql = "UPDATE users SET is_deleted = true, updated_at = CURRENT_TIMESTAMP WHERE users_uuid = ? AND is_deleted = false";
        return jdbcTemplate.update(sql, userUUID);
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(java.sql.ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUserUUID(rs.getString("users_uuid"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setPhoneNumber(rs.getString("phone_number"));

            java.sql.Date dateOfBirth = rs.getDate("date_of_birth");
            if (dateOfBirth != null) {
                user.setDateOfBirth(dateOfBirth.toLocalDate());
            }

            user.setPassportNumber(rs.getString("passport_number"));
            user.setProfilePictureURL(rs.getString("profile_picture_url"));
            user.setAdmin(rs.getBoolean("is_admin"));
            user.setFrequentFlyerTier(FrequentFlyerTier.valueOf(rs.getString("frequent_flyer_tier")));
            user.setDeleted(rs.getBoolean("is_deleted"));
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

            return user;
        }
    }
}
