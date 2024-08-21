package hr.tvz.listenlater.repository;

import hr.tvz.listenlater.model.Album;
import hr.tvz.listenlater.model.SavedAlbum;
import hr.tvz.listenlater.model.dto.AlbumDTO;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AlbumRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate jdbcParams;

    public Optional<Album> findByFullName(String fullName) {
        String query = " SELECT * FROM ALBUMS " +
                " WHERE UPPER(FULL_NAME) = UPPER(:fullName) ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("fullName", fullName);

        List<Album> album = jdbcParams.query(query, parameters, this::mapRowToAlbum);
        if (album.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(album.getFirst());
    }

    public List<Album> getAllEntities() {
        return jdbc.query("SELECT * FROM ALBUMS",
                this::mapRowToAlbum);
    }

    public Optional<Album> getEntityById(Long id) {
        List<Album> query = jdbc.query("SELECT * FROM ALBUMS WHERE ID = " + id,
                this::mapRowToAlbum);
        if (query.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(query.getFirst());
    }

    public Long addNewEntity(AlbumDTO albumDTO) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("fullName", albumDTO.getFullName())
                .addValue("name", albumDTO.getName())
                .addValue("artist", albumDTO.getArtist())
                .addValue("imgUrl", albumDTO.getImgUrl());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcParams.update("INSERT INTO ALBUMS (FULL_NAME, NAME, ARTIST, IMG_URL) VALUES (:fullName, :name, :artist, :imgUrl)", parameters, keyHolder, new String[]{"ID"});
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public boolean updateEntity(Long id, Album album) {
        String sql = "UPDATE ALBUMS SET " +
                "FULL_NAME = :fullName," +
                "NAME = :name," +
                "ARTIST = :artist," +
                "IMG_URL = :imgUrl " +
                "WHERE ID = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("fullName", album.getFullName())
                .addValue("name", album.getName())
                .addValue("artist", album.getArtist())
                .addValue("imgUrl", album.getImgUrl())
                .addValue("id", id);

        int rowsAffected = jdbcParams.update(sql, parameters);
        return rowsAffected > 0;
    }

    public boolean deleteEntity(Long id) {
        jdbc.update("DELETE FROM SAVED_ALBUMS WHERE ALBUM_ID = " + id);
        jdbc.update("DELETE FROM INVITES WHERE ALBUM_ID = " + id);
        int rowsAffected = jdbc.update("DELETE FROM ALBUMS WHERE ID = " + id);
        return rowsAffected > 0;
    }

    public Album mapRowToAlbum(ResultSet rs, int rowNum) throws SQLException {
        Album album = new Album();

        album.setId(rs.getLong("ID"));
        album.setFullName(rs.getString("FULL_NAME"));
        album.setName(rs.getString("NAME"));
        album.setArtist(rs.getString("ARTIST"));
        album.setImgUrl(rs.getString("IMG_URL"));

        return album;
    }

}
