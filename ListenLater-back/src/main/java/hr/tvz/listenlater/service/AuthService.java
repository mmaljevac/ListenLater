package hr.tvz.listenlater.service;

import hr.tvz.listenlater.model.User;
import hr.tvz.listenlater.model.dto.CurUserDTO;
import hr.tvz.listenlater.model.dto.LoginDTO;
import hr.tvz.listenlater.model.dto.RegisterDTO;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

//    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public ResponseEntity<CustomResponse<Object>> login(LoginDTO loginDTO) {
        CustomResponse<Object> response;

        Optional<User> optionalUser = userRepository.findUserByEmail(loginDTO.getEmail());
        if (optionalUser.isEmpty()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Username or password incorrect.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = optionalUser.get();
        if (!loginDTO.getPassword().equals(user.getPassword())) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Username or password incorrect.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        CurUserDTO curUser = CurUserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .isAdmin(user.isAdmin())
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

        if (userRepository.findUserByEmail(registerDTO.getEmail()).isPresent()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Email already in use.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (userRepository.findUserByUsername(registerDTO.getUsername()).isPresent()) {
            response = CustomResponse.builder()
                    .success(false)
                    .message("Username already in use.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String hashedPassword = registerDTO.getPassword();
        User hashedUser = User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(hashedPassword)
                .isAdmin(false)
                .build();

        User addedUser = userRepository.addNewEntity(hashedUser);

        response = CustomResponse.builder()
                .success(true)
                .message("Registration successful.")
                .data(addedUser)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
