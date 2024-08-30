package hr.tvz.listenlater.service;

import hr.tvz.listenlater.model.AppUser;
import hr.tvz.listenlater.model.dto.UserDTO;
import hr.tvz.listenlater.model.enums.Role;
import hr.tvz.listenlater.model.enums.Status;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<CustomResponse<Object>> changePassword(String username, String currentPassword, String newPassword) {
        CustomResponse<Object> response;

        Optional<AppUser> optionalUser = userRepository.findUserByUsername(username);

        if (optionalUser.isEmpty()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("User not found.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        AppUser user = optionalUser.get();
        if (!user.getPassword().equals(currentPassword)) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Current password is incorrect.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        boolean isChangedPassword = userRepository.changePassword(user.getId(), newPassword);
        if (!isChangedPassword) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Error changing password.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response = CustomResponse.builder()
                .success(true)
                .message("Password changed successfully. Please log in with new password.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> updateUserRole(Long id, Role newRole) {
        CustomResponse<Object> response;

        boolean isUpdated = userRepository.updateUserRole(id, newRole);
        if (!isUpdated) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("User not found.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response = CustomResponse.builder()
                .success(true)
                .message("User permissions updated.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> updateUserStatus(Long id, Status newStatus) {
        CustomResponse<Object> response;

        boolean isUserDeactivated = userRepository.updateUserStatus(id, newStatus);
        if (!isUserDeactivated) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Error deactivating user.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response = CustomResponse.builder()
                .success(true)
                .message("User deactivated.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> getAllEntities(String username) {
        CustomResponse<Object> response;

        List<AppUser> users;
        if (username == null) users = userRepository.getAllEntities();
        else users = userRepository.getUsersByUsername(username);

        List<UserDTO> usersDTO = users.stream()
                .map(this::mapUserToDTO)
                .toList();

        response = CustomResponse.builder()
                .success(true)
                .message("Success getting data.")
                .data(usersDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> getEntityById(Long id) {
        CustomResponse<Object> response;

        Optional<AppUser> optionalUser = userRepository.getEntityById(id);

        if (optionalUser.isEmpty()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("User doesn't exist.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        AppUser user = optionalUser.get();

        response = CustomResponse.builder()
                .success(true)
                .message("Success getting data.")
                .data(user)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> addNewEntity(AppUser user) {
        CustomResponse<Object> response;

        AppUser newUser = userRepository.addNewEntity(user);

        response = CustomResponse.builder()
                .success(true)
                .message("New user added.")
                .data(newUser)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> updateEntity(Long id, AppUser user) {
        CustomResponse<Object> response;

        boolean isUserUpdated = userRepository.updateEntity(id, user);
        if (!isUserUpdated) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("User not found.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response = CustomResponse.builder()
                .success(true)
                .message("User updated.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> deleteEntity(Long id) {
        CustomResponse<Object> response;

        boolean isDeleted = userRepository.deleteEntity(id);
        if (!isDeleted) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("User not found.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response = CustomResponse.builder()
                .success(true)
                .message("User deleted.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public UserDTO mapUserToDTO(AppUser user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .status(user.getStatus())
                .dateCreated(user.getDateCreated())
                .build();
    }

}
