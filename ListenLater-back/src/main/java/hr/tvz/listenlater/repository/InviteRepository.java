package hr.tvz.listenlater.repository;

import hr.tvz.listenlater.model.Album;
import hr.tvz.listenlater.model.AppUser;
import hr.tvz.listenlater.model.Invite;
import hr.tvz.listenlater.model.enums.InviteType;
import lombok.AllArgsConstructor;
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
