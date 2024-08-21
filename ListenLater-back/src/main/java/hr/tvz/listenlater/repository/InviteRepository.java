package hr.tvz.listenlater.repository;

import hr.tvz.listenlater.model.Album;
import hr.tvz.listenlater.model.AppUser;
import hr.tvz.listenlater.model.Invite;
import hr.tvz.listenlater.model.enums.InviteType;
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
public class InviteRepository {

    private final NamedParameterJdbcTemplate jdbcParams;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;

    public List<Invite> getInvitesByReceiverId(Long userId) {
        String sql = " SELECT * FROM INVITES " +
                " WHERE RECEIVER_ID = :userId " +
                " ORDER BY TIMESTAMP_CREATED ASC ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        return jdbcParams.query(sql, parameters, this::mapRowToInvite);
    }

    public Integer getInviteCountByReceiverId(Long userId) {
        String sql = " SELECT COUNT(*) FROM INVITES " +
                " WHERE RECEIVER_ID = :userId ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        Integer count = jdbcParams.queryForObject(sql, parameters, Integer.class);
        if (count == null) {
            return 0;
        }
        return count;
    }

    public boolean isFriendRequestPending(Long curUserId, Long friendId) {
        String sql = " SELECT COUNT(*) FROM INVITES " +
                " WHERE INVITE_TYPE = 'FRIEND_REQUEST' " +
                " AND (SENDER_ID = :curUserId AND RECEIVER_ID = :friendId) " +
                " OR (SENDER_ID = :friendId AND RECEIVER_ID = :curUserId) ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("curUserId", curUserId);
        parameters.addValue("friendId", friendId);

        int countNumber = jdbcParams.queryForObject(sql, parameters, Integer.class);
        return countNumber == 1;
    }

    public boolean sendFriendRequest(String curUserName, Long curUserId, Long friendId) {
        String sql = " INSERT INTO INVITES (SENDER_ID, RECEIVER_ID, INVITE_TYPE, MESSAGE) " +
                " VALUES (:senderId, :receiverId, :inviteType, :message) ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("senderId", curUserId);
        parameters.addValue("receiverId", friendId);
        parameters.addValue("inviteType", "FRIEND_REQUEST");
        parameters.addValue("message", curUserName + " would like to add you as a friend!");

        int rowsUpdated = jdbcParams.update(sql, parameters);
        return rowsUpdated == 1;
    }

    public boolean recommendAlbum(Long curUserId, Long friendId, Long albumId, String message) {
        String sql = " INSERT INTO INVITES (SENDER_ID, RECEIVER_ID, INVITE_TYPE, ALBUM_ID, MESSAGE) " +
                " VALUES (:senderId, :receiverId, :inviteType, :albumId, :message) ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("senderId", curUserId);
        parameters.addValue("receiverId", friendId);
        parameters.addValue("inviteType", "ALBUM_RECOMMENDATION");
        parameters.addValue("albumId", albumId);
        parameters.addValue("message", message);

        int rowsUpdated = jdbcParams.update(sql, parameters);
        return rowsUpdated == 1;
    }

    public boolean deleteInvite(Long inviteId) {
        String sql = " DELETE FROM INVITES " +
                " WHERE ID = :inviteId ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("inviteId", inviteId);

        int rowsUpdated = jdbcParams.update(sql, parameters);
        return rowsUpdated > 0;
    }

    private Invite mapRowToInvite(ResultSet rs, int rowNum) throws SQLException {
        Invite invite = new Invite();
        invite.setId(rs.getLong("ID"));

        Long senderId = rs.getLong("SENDER_ID");
        Long receiverId = rs.getLong("RECEIVER_ID");

        Optional<AppUser> optionalSender = userRepository.getEntityById(senderId);
        if (optionalSender.isPresent()) {
            invite.setSender(optionalSender.get());
            invite.setSenderId(optionalSender.get().getId());
        }

        Optional<AppUser> optionalReceiver = userRepository.getEntityById(receiverId);
        if (optionalReceiver.isPresent()) {
            invite.setReceiver(optionalReceiver.get());
            invite.setReceiverId(optionalReceiver.get().getId());
        }

        invite.setInviteType(InviteType.valueOf(rs.getString("INVITE_TYPE")));

        Optional<Album> optionalAlbum = albumRepository.getEntityById(rs.getLong("ALBUM_ID"));
        if (optionalAlbum.isPresent()) {
            invite.setAlbum(optionalAlbum.get());
            invite.setAlbumId(optionalAlbum.get().getId());
        }

        invite.setMessage(rs.getString("MESSAGE"));

        Timestamp timestamp = rs.getTimestamp("TIMESTAMP_CREATED");
        if (timestamp != null) {
            invite.setTimestampCreated(timestamp.toInstant());
        }

        return invite;
    }
}
