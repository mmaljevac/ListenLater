package hr.tvz.listenlater.repository;

import hr.tvz.listenlater.model.AppUser;
import hr.tvz.listenlater.model.enums.Role;
import hr.tvz.listenlater.model.enums.Status;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate jdbcParams;
    private final SimpleJdbcInsert inserter;

    public UserRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate jdbcParams) {
        this.jdbc = jdbc;
        this.jdbcParams = jdbcParams;
        this.inserter = new SimpleJdbcInsert(jdbc)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("ID");
    }

    public Optional<AppUser> findUserByEmail(String email) {
        String sql = "SELECT * FROM USERS WHERE EMAIL = :email";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);

        List<AppUser> query = jdbcParams.query(sql, parameters, this::mapRowToUser);

        if (query.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(query.getFirst());
    }

    public Optional<AppUser> findUserByUsername(String username) {
        String sql = "SELECT * FROM USERS WHERE USERNAME = :username";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("username", username);

        List<AppUser> query = jdbcParams.query(sql, parameters, this::mapRowToUser);

        if (query.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(query.getFirst());
    }

    public boolean changePassword(Long id, String newPassword) {
        String sql = "UPDATE USERS SET PASSWORD = ? WHERE ID = ?";
        int rowsAffected = jdbc.update(sql, newPassword, id);
        return rowsAffected > 0;
    }

    public boolean updateUserRole(Long id, Role newRole) {
        String sql = "UPDATE USERS SET ROLE = ? WHERE ID = ?";
        int rowsAffected = jdbc.update(sql, newRole.getValue(), id);
        return rowsAffected > 0;
    }

    public List<AppUser> getUsersByUsername(String username) {
        String sql = "SELECT * FROM USERS WHERE UPPER(USERNAME) LIKE :username";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("username", "%" + username.toUpperCase() + "%");

        return jdbcParams.query(sql, parameters, this::mapRowToUser);
    }

    public List<AppUser> getAllEntities() {
        return jdbc.query("SELECT * FROM USERS",
                this::mapRowToUser);
    }

    public Optional<AppUser> getEntityById(Long id) {
        List<AppUser> query = jdbc.query("SELECT * FROM USERS WHERE ID = " + id,
                this::mapRowToUser);
        if (query.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(query.getFirst());
    }

    public AppUser addNewEntity(AppUser user) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("role", user.getRole().getValue())
                .addValue("status", user.getStatus().getValue())
                .addValue("dateCreated", user.getDateCreated());

        Long insertId = inserter.executeAndReturnKey(parameters).longValue();
        user.setId(insertId);

        return user;
    }

    public boolean updateEntity(Long id, AppUser user) {
        String sql = "UPDATE USERS SET " +
                "USERNAME = :username, " +
                "EMAIL = :email, " +
                "PASSWORD = :password " +
                "WHERE ID = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("id", id);

        int rowsAffected = jdbcParams.update(sql, parameters);
        return rowsAffected == 1;
    }

    public boolean deleteEntity(Long id) {
        // TODO dodati provjeru je li album spremljen od bar jednog usera, izbrisati ga ako nije
        jdbc.update("DELETE FROM SAVED_ALBUMS WHERE USER_ID = " + id);
        int rowsAffected = jdbc.update("DELETE FROM USERS WHERE ID = " + id);
        return rowsAffected > 0;
    }

    public boolean updateUserStatus(Long id, Status newStatus) {
        int rowsAffected = jdbc.update("UPDATE USERS SET STATUS = ? WHERE ID = ?", newStatus.toString(), id);
        return rowsAffected > 0;
    }

    private AppUser mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        AppUser user = new AppUser();

        user.setId(rs.getLong("ID"));
        user.setUsername(rs.getString("USERNAME"));
        user.setEmail(rs.getString("EMAIL"));
        user.setPassword(rs.getString("PASSWORD"));
        user.setRole(Role.valueOf(rs.getString("ROLE")));
        user.setStatus(Status.valueOf(rs.getString("STATUS")));

        Date sqlDate = rs.getDate("DATE_CREATED");
        if (sqlDate != null) {
            user.setDateCreated(sqlDate.toLocalDate());
        } else {
            user.setDateCreated(null);
        }

        return user;
    }

}
