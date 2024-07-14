package hr.tvz.listenlater.service;

import hr.tvz.listenlater.model.AppUser;
import hr.tvz.listenlater.model.dto.UserDTO;
import hr.tvz.listenlater.model.dto.LoginDTO;
import hr.tvz.listenlater.model.dto.RegisterDTO;
import hr.tvz.listenlater.model.enums.Role;
import hr.tvz.listenlater.model.enums.Status;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

//    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public ResponseEntity<CustomResponse<Object>> login(LoginDTO loginDTO) {
        CustomResponse<Object> response;

        Optional<AppUser> optionalUser = userRepository.findUserByEmail(loginDTO.getEmail());
        if (optionalUser.isEmpty()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Username or password incorrect.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        AppUser user = optionalUser.get();
        if (!loginDTO.getPassword().equals(user.getPassword())) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Username or password incorrect.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        UserDTO curUser = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .dateCreated(user.getDateCreated())
                .build();

        response = CustomResponse.builder()
                .success(true)
                .message("Logged in successfully.")
                .data(curUser)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> register(RegisterDTO registerDTO) {
        CustomResponse<Object> response;

        if (userRepository.findUserByUsername(registerDTO.getUsername()).isPresent()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Username already in use.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (userRepository.findUserByEmail(registerDTO.getEmail()).isPresent()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Email already in use.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String hashedPassword = registerDTO.getPassword(); // TODO hash password
        AppUser hashedUser = AppUser.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(hashedPassword)
                .role(Role.USER)
                .status(Status.ACTIVE)
                .dateCreated(LocalDate.now())
                .build();

        AppUser addedUser = userRepository.addNewEntity(hashedUser);
        if (addedUser == null) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Error adding new user.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response = CustomResponse.builder()
                .success(true)
                .message("Registration successful.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
