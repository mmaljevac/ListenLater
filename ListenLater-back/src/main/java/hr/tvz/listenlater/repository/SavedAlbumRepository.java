package hr.tvz.listenlater.repository;

import hr.tvz.listenlater.model.Album;
import hr.tvz.listenlater.model.AppUser;
import hr.tvz.listenlater.model.SavedAlbum;
import hr.tvz.listenlater.model.enums.Action;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class SavedAlbumRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate jdbcParams;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;

    public Optional<SavedAlbum> getSavedAlbum(Long userId, Long albumId) {
        String query = " SELECT * FROM SAVED_ALBUMS " +
                " WHERE USER_ID = :userId " +
                " AND ALBUM_ID = :albumId ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        parameters.addValue("albumId", albumId);

        List<SavedAlbum> savedAlbum = jdbcParams.query(query, parameters, this::mapRowToSavedAlbum);
        if (savedAlbum.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(savedAlbum.getFirst());
    }

    public List<SavedAlbum> getSavedAlbumsByUserId(Long userId) {
        String sql = " SELECT * FROM ALBUMS a " +
                " JOIN SAVED_ALBUMS sa ON a.ID = sa.ALBUM_ID " +
                " JOIN USERS u ON sa.USER_ID = u.ID " +
                " WHERE u.ID = :userId " +
                " ORDER BY a.ARTIST ASC, a.NAME ASC ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);

        return jdbcParams.query(sql, parameters, this::mapRowToSavedAlbum);
    }

    public List<SavedAlbum> getSavedAlbumsByUserIdAndAction(Long userId, String action) {
        String sql = " SELECT * FROM ALBUMS a " +
                " JOIN SAVED_ALBUMS sa ON a.ID = sa.ALBUM_ID " +
                " JOIN USERS u ON sa.USER_ID = u.ID " +
                " WHERE u.ID = :userId " +
                " AND sa.ACTION = :action " +
                " ORDER BY a.ARTIST ASC, a.NAME ASC ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        parameters.addValue("action", action);

        return jdbcParams.query(sql, parameters, this::mapRowToSavedAlbum);
    }

    public boolean saveAlbum(Long userId, Long albumId, String action) {
        String sql = " INSERT INTO SAVED_ALBUMS (USER_ID, ALBUM_ID, ACTION) " +
                " VALUES (:userId, :albumId, :action) ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        parameters.addValue("albumId", albumId);
        parameters.addValue("action", action);

        int rowsUpdated = jdbcParams.update(sql, parameters);
        return rowsUpdated > 0;
    }

    public boolean updateSavedAlbumAction(Long userId, Long albumId, String newAction) {
        String sql = " UPDATE SAVED_ALBUMS SET ACTION = :newAction " +
                " WHERE USER_ID = :userId" +
                " AND ALBUM_ID = :albumId ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        parameters.addValue("albumId", albumId);
        parameters.addValue("newAction", newAction);

        int rowsUpdated = jdbcParams.update(sql, parameters);
        return rowsUpdated > 0;
    }

    public boolean deleteSavedAlbum(Long userId, Long albumId) {
        String sql = " DELETE FROM SAVED_ALBUMS " +
                " WHERE USER_ID = :userId " +
                " AND ALBUM_ID = :albumId ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        parameters.addValue("albumId", albumId);

        int rowsUpdated = jdbcParams.update(sql, parameters);
        return rowsUpdated > 0;
    }

    private SavedAlbum mapRowToSavedAlbum(ResultSet rs, int rowNum) throws SQLException {
        SavedAlbum savedAlbum = new SavedAlbum();

        Long userId = rs.getLong("USER_ID");
        Long albumId = rs.getLong("ALBUM_ID");

        savedAlbum.setAction(Action.valueOf(rs.getString("ACTION")));

        Timestamp timestamp = rs.getTimestamp("DATE_ADDED");
        if (timestamp != null) {
            savedAlbum.setDateAdded(timestamp.toInstant());
        }

        Optional<AppUser> optionalUser = userRepository.getEntityById(userId);
        if (optionalUser.isPresent()) {
            savedAlbum.setUser(optionalUser.get());
        }

        Optional<Album> optionalAlbum = albumRepository.getEntityById(albumId);
        if (optionalAlbum.isPresent()) {
            savedAlbum.setAlbum(optionalAlbum.get());
        }

        savedAlbum.setUserId(userId);
        savedAlbum.setAlbumId(albumId);

        return savedAlbum;
    }
}