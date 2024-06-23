package hr.tvz.listenlater.repository;

import hr.tvz.listenlater.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert inserter;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.inserter = new SimpleJdbcInsert(jdbc)
                .withTableName("TABLE_USER")
                .usingGeneratedKeyColumns("ID");
    }

    public User findByEmail(String email) {
        var query = jdbc.query("SELECT * FROM TABLE_USER WHERE EMAIL = '" + email + "' ",
                this::mapRowToUser);
        if (!query.isEmpty()) {
            return query.get(0);
        }
        return null;
    }

    public User changePassword(int id, String newPassword) {
        String sql = "UPDATE TABLE_USER SET PASSWORD = ? WHERE ID = ?";
        try {
            int rowsAffected = jdbc.update(sql, newPassword, id);
            if (rowsAffected == 1) {
                return getEntity(id);
            }
            return null;
        } catch (DataAccessException e) {
            throw e;
        }
    }

    public User updatePermissions(int id) {
        User user = this.getEntity(id);
        String sql = "UPDATE TABLE_USER SET IS_ADMIN = ? WHERE ID = ?";
        try {
            int rowsAffected = jdbc.update(sql, !user.isAdmin(), id);
            if (rowsAffected == 1) {
                return getEntity(id);
            }
            return null;
        } catch (DataAccessException e) {
            throw e;
        }
    }

    public List<User> getAllEntities() {
        return jdbc.query("SELECT * FROM TABLE_USER",
                this::mapRowToUser);
    }

    public User getEntity(int id) {
        return jdbc.query("SELECT * FROM TABLE_USER WHERE ID = " + id,
                this::mapRowToUser).get(0);
    }

    public User addNewEntity(User user) throws Exception {
        if (this.findByEmail(user.getEmail()) != null) {
            throw new Exception("User with this email already exists.");
        }

        Map<String,Object> parameters = new HashMap<>();

        parameters.put("USERNAME",user.getUsername());
        parameters.put("EMAIL",user.getEmail());
        parameters.put("PASSWORD",user.getPassword());
        parameters.put("IS_ADMIN",user.isAdmin());

        int insertId = inserter.executeAndReturnKey(parameters).intValue();
        user.setId(insertId);

        return user;
    }

    public User updateEntity(int id, User user) {
        jdbc.update("UPDATE TABLE_USER SET " +
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

        return user;
    }

    public Boolean deleteEntity(int id) {
        jdbc.update("DELETE FROM TABLE_ALBUM WHERE ID_USER = " + id);
        jdbc.update("DELETE FROM TABLE_USER WHERE ID = " + id);
        return true;
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
