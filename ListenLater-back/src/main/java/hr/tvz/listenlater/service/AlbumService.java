package hr.tvz.listenlater.service;

import hr.tvz.listenlater.model.Album;
import hr.tvz.listenlater.repository.AlbumRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AlbumService {

    @Autowired
    private final AlbumRepository albumRepository;

    public List<Album> getAlbumsByUser(int id) {
        return this.albumRepository.getAlbumsByUser(id);
    }

    public List<Album> getAllEntities() {
        return this.albumRepository.getAllEntities();
    }

    public Album getEntity(int id) {
        return this.albumRepository.getEntity(id);
    }

    public Album addNewEntity(Album album) {
        return this.albumRepository.addNewEntity(album);
    }

    public Album updateEntity(int id, Album album) {
        return this.albumRepository.updateEntity(id, album);
    }

    public boolean deleteEntity(int id) {
        return this.albumRepository.deleteEntity(id);
    }

}
