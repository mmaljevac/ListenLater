package hr.tvz.listenlater.repository;

import hr.tvz.listenlater.model.AppUser;
import hr.tvz.listenlater.model.Friends;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class FriendsRepository {

    private final NamedParameterJdbcTemplate jdbcParams;
    private final UserRepository userRepository;

    public List<AppUser> getFriendsByUserId(Long userId) {
        String sql = " SELECT * FROM USERS u " +
                " JOIN FRIENDS f ON (u.ID = f.USER1_ID OR u.ID = f.USER2_ID) " +
                " WHERE (f.USER1_ID = :userId OR f.USER2_ID = :userId) " +
                " AND u.ID != :userId " +
                " ORDER BY u.USERNAME ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        return jdbcParams.query(sql, parameters, userRepository::mapRowToUser);
    }

    public boolean isFriends(Long curUserId, Long friendId) {
        String sql = " SELECT COUNT(*) FROM FRIENDS " +
                " WHERE (USER1_ID = :curUserId AND USER2_ID = :friendId) " +
                " OR (USER1_ID = :friendId AND USER2_ID = :curUserId) ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("curUserId", curUserId);
        parameters.addValue("friendId", friendId);

        int countNumber = jdbcParams.queryForObject(sql, parameters, Integer.class);
        return countNumber == 1;
    }

    public boolean addFriend(Long curUserId, Long friendId) {
        String sql = " INSERT INTO FRIENDS (USER1_ID, USER2_ID) " +
                " VALUES (:curUserId, :friendId) ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("curUserId", curUserId);
        parameters.addValue("friendId", friendId);

        int rowsUpdated = jdbcParams.update(sql, parameters);
        return rowsUpdated > 0;
    }

    public boolean removeFriend(Long curUserId, Long friendId) {
        String sql = " DELETE FROM FRIENDS " +
                " WHERE (USER1_ID = :curUserId AND USER2_ID = :friendId) " +
                " OR (USER1_ID = :friendId AND USER2_ID = :curUserId) ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("curUserId", curUserId);
        parameters.addValue("friendId", friendId);

        int rowsUpdated = jdbcParams.update(sql, parameters);
        return rowsUpdated > 0;
    }

    private Friends mapRowToFriends(ResultSet rs, int rowNum) throws SQLException {
        Friends friends = new Friends();
        friends.setId(rs.getLong("ID"));

        Long user1Id = rs.getLong("USER1_ID");
        Long user2Id = rs.getLong("USER2_ID");

        Optional<AppUser> optionalUser1 = userRepository.getEntityById(user1Id);
        if (optionalUser1.isPresent()) {
            friends.setUser1(optionalUser1.get());
            friends.setUser1Id(optionalUser1.get().getId());
        }

        Optional<AppUser> optionalUser2 = userRepository.getEntityById(user2Id);
        if (optionalUser2.isPresent()) {
            friends.setUser2(optionalUser2.get());
            friends.setUser2Id(optionalUser2.get().getId());
        }

        Timestamp timestamp = rs.getTimestamp("TIMESTAMP_ADDED");
        if (timestamp != null) {
            friends.setTimestampAdded(timestamp.toInstant());
        }

        return friends;
    }
}
