package hr.tvz.listenlater.controller;

import hr.tvz.listenlater.model.dto.AlbumDTO;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.service.SavedAlbumService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saved-albums")
@AllArgsConstructor()
@CrossOrigin(origins = "http://localhost:3000")
public class SavedAlbumController {

    private final SavedAlbumService savedAlbumService;

    @GetMapping("/user/{userId}/album/{albumId}")
    public ResponseEntity<CustomResponse<Object>> getSavedAlbum(@PathVariable Long userId, @PathVariable Long albumId) {
        return savedAlbumService.getSavedAlbum(userId, albumId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomResponse<Object>> getSavedAlbumsByUserId(@PathVariable Long userId, @RequestParam(name = "action", required = false) String action) {
        return savedAlbumService.getSavedAlbumsByUserIdAndAction(userId, action);
    }

    @GetMapping("/album/user/{userId}")
    public ResponseEntity<CustomResponse<Object>> getIsAlbumSavedByUser(@PathVariable Long userId, @RequestParam(name = "fullName", required = true) String fullName) {
        return savedAlbumService.getIsAlbumSavedByUser(userId, fullName);
    }

    @PostMapping("/save/user/{userId}")
    public ResponseEntity<CustomResponse<Object>> saveAlbum(@PathVariable Long userId, @RequestBody AlbumDTO albumDTO, @RequestParam(name = "action") String action) {
        return savedAlbumService.saveAlbum(userId, albumDTO, action);
    }

    @PatchMapping("/user/{userId}/album/{albumId}")
    public ResponseEntity<CustomResponse<Object>> updateSavedAlbum(@PathVariable Long userId, @PathVariable Long albumId, @RequestParam(name = "action") String newAction) {
        return savedAlbumService.updateSavedAlbum(userId, albumId, newAction);
    }

    @DeleteMapping("/user/{userId}/album/{albumId}")
    public ResponseEntity<CustomResponse<Object>> deleteSavedAlbum(@PathVariable Long userId, @PathVariable Long albumId) {
        return savedAlbumService.deleteSavedAlbum(userId, albumId);
    }

}