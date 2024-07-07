package hr.tvz.listenlater.repository;

import hr.tvz.listenlater.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert inserter;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.inserter = new SimpleJdbcInsert(jdbc)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("ID");
    }

    public Optional<User> findUserByEmail(String email) {
        List<User> query = jdbc.query("SELECT * FROM USERS WHERE EMAIL = '" + email + "' ",
                this::mapRowToUser);
        if (query.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(query.getFirst());
    }

    public Optional<User> findUserByUsername(String username) {
        List<User> query = jdbc.query("SELECT * FROM USERS WHERE USERNAME = '" + username + "' ",
                this::mapRowToUser);
        if (query.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(query.getFirst());
    }

    public boolean changePassword(int id, String newPassword) {
        String sql = "UPDATE USERS SET PASSWORD = ? WHERE ID = ?";
        int rowsAffected = jdbc.update(sql, newPassword, id);
        return rowsAffected == 1;
    }

    public boolean updatePermissions(int id) {
        Optional<User> optionalUser = getEntityById(id);

        if (optionalUser.isEmpty()) {
            return false;
        }
        User user = optionalUser.get();

        String sql = "UPDATE USERS SET IS_ADMIN = ? WHERE ID = ?";
        int rowsAffected = jdbc.update(sql, !user.isAdmin(), id);
        return rowsAffected == 1;
    }

    public List<User> getAllEntities() {
        return jdbc.query("SELECT * FROM USERS",
                this::mapRowToUser);
    }

    public Optional<User> getEntityById(int id) {
        List<User> query = jdbc.query("SELECT * FROM USERS WHERE ID = " + id,
                this::mapRowToUser);
        if (query.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(query.getFirst());
    }

    public User addNewEntity(User user) {
        Map<String,Object> parameters = new HashMap<>();

        parameters.put("USERNAME",user.getUsername());
        parameters.put("EMAIL",user.getEmail());
        parameters.put("PASSWORD",user.getPassword());
        parameters.put("IS_ADMIN",user.isAdmin());

        int insertId = inserter.executeAndReturnKey(parameters).intValue();
        user.setId(insertId);

        return user;
    }

    public boolean updateEntity(int id, User user) {
        int rowsAffected = jdbc.update("UPDATE USERS SET " +
                        "USERNAME = ?," +
                        "EMAIL = ?," +
                        "PASSWORD = ?," +
                        "IS_ADMIN = ? " +
                        "WHERE ID = ?",
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isAdmin(),
                id
        );
        return rowsAffected == 1;
    }

    public boolean deleteEntity(int id) {
        jdbc.update("DELETE FROM ALBUMS WHERE USER_ID = " + id);
        int rowsAffected = jdbc.update("DELETE FROM USERS WHERE ID = " + id);
        return rowsAffected == 1;
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("ID"));
        user.setUsername(rs.getString("USERNAME"));
        user.setEmail(rs.getString("EMAIL"));
        user.setPassword(rs.getString("PASSWORD"));
        user.setAdmin(rs.getBoolean("IS_ADMIN"));

        return user;
    }

}
