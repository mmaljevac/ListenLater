package hr.tvz.listenlater.controller;

import hr.tvz.listenlater.model.Album;
import hr.tvz.listenlater.service.AlbumService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return this.albumService.getAlbumsByUser(id);
    }

    @GetMapping
    public ResponseEntity<List<Album>> getAllEntities() {
        List<Album> entities = this.albumService.getAllEntities();
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getEntity(@PathVariable final int id) {
        Album entity = this.albumService.getEntity(id);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Album> addNewEntity(@RequestBody final Album album) {
        Album addedEntity = this.albumService.addNewEntity(album);
        return new ResponseEntity<>(addedEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateEntity(@PathVariable final int id, @RequestBody final Album album) {
        Album updatedEntity = this.albumService.updateEntity(id, album);
        return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEntity(@PathVariable final int id) {
        boolean deletedEntity = this.albumService.deleteEntity(id);
        return new ResponseEntity<>(deletedEntity, HttpStatus.OK);
    }

}
