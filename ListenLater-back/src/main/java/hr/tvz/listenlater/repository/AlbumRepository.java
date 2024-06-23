package hr.tvz.listenlater.repository;

import hr.tvz.listenlater.model.Album;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AlbumRepository {

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert inserter;

    public AlbumRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.inserter = new SimpleJdbcInsert(jdbc)
                .withTableName("TABLE_ALBUM")
                .usingGeneratedKeyColumns("ID");
    }

    public List<Album> getAlbumsByUser(int id) {
        return jdbc.query("SELECT * FROM TABLE_ALBUM WHERE ID_USER = " + id + " ORDER BY ARTIST ASC",
                this::mapRowToAlbum);
    }
    public List<Album> getAllEntities() {
        return jdbc.query("SELECT * FROM TABLE_ALBUM",
                this::mapRowToAlbum);
    }

    public Album getEntity(int id) {
        return jdbc.query("SELECT * FROM TABLE_ALBUM WHERE ID = " + id,
                this::mapRowToAlbum).get(0);
    }

    public Album findByImgUrl(String imgUrl) {
        var query = jdbc.query("SELECT * FROM TABLE_ALBUM WHERE IMG_URL = '" + imgUrl + "' ",
                this::mapRowToAlbum);
        if (!query.isEmpty()) {
            return query.get(0);
        }
        return null;
    }

    public Album addNewEntity(Album album) {
        Map<String,Object> parameters = new HashMap<>();

        parameters.put("NAME",album.getName());
        parameters.put("ARTIST",album.getArtist());
        parameters.put("IMG_URL",album.getImgUrl());
        parameters.put("ID_USER",album.getIdUser());

        int insertId = inserter.executeAndReturnKey(parameters).intValue();
        album.setId(insertId);

        return album;
    }

    public Album updateEntity(int id, Album album) {
        jdbc.update("UPDATE TABLE_ALBUM SET " +
                        "NAME = ?," +
                        "ARTIST = ?," +
                        "IMG_URL = ?," +
                        "ID_USER = ? " +
                        "WHERE ID = ?",
                album.getName(),
                album.getArtist(),
                album.getImgUrl(),
                album.getIdUser(),
                id
        );

        return album;
    }

    public Boolean deleteEntity(int id) {
        jdbc.update("DELETE FROM TABLE_ALBUM WHERE ID = " + id);
        return true;
    }

    private Album mapRowToAlbum(ResultSet rs, int rowNum) throws SQLException {
        Album album = new Album();

        album.setId(rs.getInt("ID"));
        album.setName(rs.getString("NAME"));
        album.setArtist(rs.getString("ARTIST"));
        album.setImgUrl(rs.getString("IMG_URL"));
        album.setIdUser(rs.getInt("ID_USER"));

        return album;
    }

}
