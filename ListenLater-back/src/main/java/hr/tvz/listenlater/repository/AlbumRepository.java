package hr.tvz.listenlater.repository;

import hr.tvz.listenlater.model.Album;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AlbumRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate jdbcParams;

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

    public Album addNewEntity(Album album) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", album.getName())
                .addValue("artist", album.getArtist())
                .addValue("imgUrl", album.getImgUrl());

        jdbcParams.update("INSERT INTO ALBUMS (NAME, ARTIST, IMG_URL) VALUES (:name, :artist, :imgUrl)", parameters);
        return album;
    }

    public boolean updateEntity(Long id, Album album) {
        String sql = "UPDATE ALBUMS SET " +
                "NAME = :name," +
                "ARTIST = :artist," +
                "IMG_URL = :imgUrl " +
                "WHERE ID = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", album.getName())
                .addValue("artist", album.getArtist())
                .addValue("imgUrl", album.getImgUrl())
                .addValue("id", id);

        int rowsAffected = jdbcParams.update(sql, parameters);
        return rowsAffected > 0;
    }

    public boolean deleteEntity(Long id) {
        jdbc.update("DELETE FROM SAVED_ALBUMS WHERE ALBUM_ID = " + id);
        int rowsAffected = jdbc.update("DELETE FROM ALBUMS WHERE ID = " + id);
        return rowsAffected > 0;
    }

    public Album mapRowToAlbum(ResultSet rs, int rowNum) throws SQLException {
        Album album = new Album();

        album.setId(rs.getLong("ID"));
        album.setName(rs.getString("NAME"));
        album.setArtist(rs.getString("ARTIST"));
        album.setFullName(rs.getString("FULL_NAME"));
        album.setImgUrl(rs.getString("IMG_URL"));

        return album;
    }

}
