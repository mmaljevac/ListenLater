package hr.tvz.listenlater.controller;

import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.service.SavedAlbumService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saved-albums")
@AllArgsConstructor()
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

    @PostMapping("/user/{userId}/album/{albumId}")
    public ResponseEntity<CustomResponse<Object>> saveAlbum(@PathVariable Long userId, @PathVariable Long albumId, @RequestParam(name = "action") String action) {
        return savedAlbumService.saveAlbum(userId, albumId, action);
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