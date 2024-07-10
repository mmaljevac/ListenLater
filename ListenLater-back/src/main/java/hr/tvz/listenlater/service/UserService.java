package hr.tvz.listenlater.service;

import hr.tvz.listenlater.model.User;
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

    public ResponseEntity<CustomResponse<Object>> changePassword(String email, String currentPassword, String newPassword) {
        CustomResponse<Object> response;

        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isEmpty()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("User not found.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = optionalUser.get();
        if (!user.getPassword().equals(currentPassword)) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Current password is incorrect.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        boolean isChangedPassword = userRepository.changePassword(user.getId(), newPassword);
        if (!isChangedPassword) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Error changing password.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        response = CustomResponse.builder()
                .success(true)
                .message("Password changed successfully.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> updateUserRole(Long id, String newRoleString) {
        CustomResponse<Object> response;

        Role newRole = Role.fromValue(newRoleString);
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

    public ResponseEntity<CustomResponse<Object>> updateUserStatus(String email, String newStatusString) {
        CustomResponse<Object> response;

        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isEmpty()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("User not found.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = optionalUser.get();
        Status newStatus = Status.fromValue(newStatusString);
        boolean isUserDeactivated = userRepository.updateUserStatus(user.getId(), newStatus);
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

    public ResponseEntity<CustomResponse<Object>> getAllEntities() {
        CustomResponse<Object> response;

        List<User> users = userRepository.getAllEntities();

        response = CustomResponse.builder()
                .success(true)
                .message("Success getting data.")
                .data(users)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> getEntityById(Long id) {
        CustomResponse<Object> response;

        Optional<User> optionalUser = userRepository.getEntityById(id);

        if (optionalUser.isEmpty()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("User doesn't exist.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = optionalUser.get();

        response = CustomResponse.builder()
                .success(true)
                .message("Success getting data.")
                .data(user)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> addNewEntity(User user) {
        CustomResponse<Object> response;

        User newUser = userRepository.addNewEntity(user);

        response = CustomResponse.builder()
                .success(true)
                .message("New user added.")
                .data(newUser)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> updateEntity(Long id, User user) {
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

}
