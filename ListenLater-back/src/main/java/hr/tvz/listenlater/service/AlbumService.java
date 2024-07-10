package hr.tvz.listenlater.service;

import hr.tvz.listenlater.model.Album;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.repository.AlbumRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    public ResponseEntity<CustomResponse<Object>> getAllEntities() {
        CustomResponse<Object> response;

        List<Album> albums = albumRepository.getAllEntities();

        response = CustomResponse.builder()
                .success(true)
                .message("Success getting data.")
                .data(albums)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> getEntityById(Long id) {
        CustomResponse<Object> response;

        Optional<Album> optionalAlbum = albumRepository.getEntityById(id);

        if (optionalAlbum.isEmpty()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Album doesn't exist.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Album album = optionalAlbum.get();

        response = CustomResponse.builder()
                .success(true)
                .message("Success getting data.")
                .data(album)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> addNewEntity(Album album) {
        CustomResponse<Object> response;

        Album newAlbum = albumRepository.addNewEntity(album);

        response = CustomResponse.builder()
                .success(true)
                .message("New album added.")
                .data(newAlbum)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> updateEntity(Long id, Album album) {
        CustomResponse<Object> response;

        boolean isAlbumUpdated = albumRepository.updateEntity(id, album);
        if (!isAlbumUpdated) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Album not found.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response = CustomResponse.builder()
                .success(true)
                .message("Album updated.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> deleteEntity(Long id) {
        CustomResponse<Object> response;

        boolean isDeleted = albumRepository.deleteEntity(id);
        if (!isDeleted) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Album not found.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response = CustomResponse.builder()
                .success(true)
                .message("Album deleted.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
