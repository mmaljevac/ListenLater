package hr.tvz.listenlater.controller;

import hr.tvz.listenlater.model.Album;
import hr.tvz.listenlater.model.dto.AlbumDTO;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.service.AlbumService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/albums")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping
    public ResponseEntity<CustomResponse<Object>> getAllEntities() {
        return albumService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Object>> getEntityById(@PathVariable Long id) {
        return albumService.getEntityById(id);
    }

    @PostMapping
    public ResponseEntity<CustomResponse<Object>> addNewEntity(@RequestBody AlbumDTO albumDTO) {
        return albumService.addNewEntity(albumDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Object>> updateEntity(@PathVariable Long id, @RequestBody Album album) {
        return albumService.updateEntity(id, album);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Object>> deleteEntity(@PathVariable Long id) {
        return albumService.deleteEntity(id);
    }

}
