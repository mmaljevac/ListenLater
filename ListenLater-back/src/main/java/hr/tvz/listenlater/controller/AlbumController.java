package hr.tvz.listenlater.controller;

import hr.tvz.listenlater.model.Album;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.service.AlbumService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping("/getAlbumsByUser/{id}")
    public List<Album> getAlbumsByUser(@PathVariable final int id) {
        return albumService.getAlbumsByUser(id);
    }

    @GetMapping
    public ResponseEntity<CustomResponse<Object>> getAllEntities() {
        return albumService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Object>> getEntityById(@PathVariable final int id) {
        return albumService.getEntityById(id);
    }

    @PostMapping
    public ResponseEntity<CustomResponse<Object>> addNewEntity(@RequestBody final Album album) {
        return albumService.addNewEntity(album);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Object>> updateEntity(@PathVariable final int id, @RequestBody final Album album) {
        return albumService.updateEntity(id, album);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Object>> deleteEntity(@PathVariable final int id) {
        return albumService.deleteEntity(id);
    }

}
