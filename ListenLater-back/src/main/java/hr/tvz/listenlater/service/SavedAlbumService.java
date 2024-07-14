package hr.tvz.listenlater.service;

import hr.tvz.listenlater.model.SavedAlbum;
import hr.tvz.listenlater.model.dto.SavedAlbumDTO;
import hr.tvz.listenlater.model.enums.Action;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.repository.SavedAlbumRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SavedAlbumService {

    private final SavedAlbumRepository savedAlbumRepository;

    public ResponseEntity<CustomResponse<Object>> getSavedAlbum(Long userId, Long albumId) {
        CustomResponse<Object> response;

        Optional<SavedAlbum> optionalSavedAlbum = savedAlbumRepository.getSavedAlbum(userId, albumId);

        if (optionalSavedAlbum.isEmpty()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Saved album doesn't exist.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        SavedAlbum savedAlbum = optionalSavedAlbum.get();
        SavedAlbumDTO savedAlbumDTO = mapSavedAlbumToDTO(savedAlbum);

        response = CustomResponse.builder()
                .success(true)
                .message("Success getting data.")
                .data(savedAlbumDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> getSavedAlbumsByUserIdAndAction(Long userId, String actionString) {
        CustomResponse<Object> response;

        List<SavedAlbum> savedAlbumsByUser;
        if (actionString == null) {
            savedAlbumsByUser = savedAlbumRepository.getSavedAlbumsByUserId(userId);
        } else {
            try {
                Action action = Action.fromValue(actionString);
                savedAlbumsByUser = savedAlbumRepository.getSavedAlbumsByUserIdAndAction(userId, action.getValue());
            } catch (IllegalArgumentException e) {
                response = CustomResponse.builder()
                        .success(false)
                        .message("Unknown action.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        }

        List<SavedAlbumDTO> savedAlbumDTOs = savedAlbumsByUser.stream()
                .map(this::mapSavedAlbumToDTO)
                .toList();

        response = CustomResponse.builder()
                .success(true)
                .message("Success getting data.")
                .data(savedAlbumDTOs)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> saveAlbum(Long userId, Long albumId, String actionString) {
        CustomResponse<Object> response;

        try {
            Action action = Action.fromValue(actionString);
            boolean isAlbumSaved = savedAlbumRepository.saveAlbum(userId, albumId, action.getValue());
            if (!isAlbumSaved) {
                response = CustomResponse.builder()
                        .success(false)
                        .message("Error saving album.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response = CustomResponse.builder()
                    .success(true)
                    .message("Album saved.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Unknown action entered.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<CustomResponse<Object>> updateSavedAlbum(Long userId, Long albumId, String newActionString) {
        CustomResponse<Object> response;

        try {
            Action action = Action.fromValue(newActionString);
            boolean isAlbumUpdated = savedAlbumRepository.updateSavedAlbum(userId, albumId, action.getValue());
            if (!isAlbumUpdated) {
                response = CustomResponse.builder()
                        .success(false)
                        .message("Error updating album.")
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response = CustomResponse.builder()
                    .success(true)
                    .message("Album updated.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Unknown action entered.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<CustomResponse<Object>> deleteSavedAlbum(Long userId, Long albumId) {
        CustomResponse<Object> response;

        boolean isDeleted = savedAlbumRepository.deleteSavedAlbum(userId, albumId);
        if (!isDeleted) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Error removing saved album.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response = CustomResponse.builder()
                .success(true)
                .message("Album removed.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private SavedAlbumDTO mapSavedAlbumToDTO(SavedAlbum savedAlbum) {
        return SavedAlbumDTO.builder()
                .album(savedAlbum.getAlbum())
                .userId(savedAlbum.getUserId())
                .action(savedAlbum.getAction())
                .dateAdded(savedAlbum.getDateAdded())
                .build();
    }

}