package hr.tvz.listenlater.service;

import hr.tvz.listenlater.model.dto.CurUserDTO;
import hr.tvz.listenlater.model.dto.LoginDTO;
import hr.tvz.listenlater.model.User;
import hr.tvz.listenlater.model.dto.RegisterDTO;
import hr.tvz.listenlater.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private final UserRepository userRepository;

    public ResponseEntity<CurUserDTO> login(LoginDTO loginDTO) {
        User user = this.userRepository.findByEmail(loginDTO.getEmail());
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // 400
        }

        CurUserDTO curUser = CurUserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isAdmin(user.isAdmin())
                .build();

        return new ResponseEntity<>(curUser, HttpStatus.OK); // 200
    }

    public ResponseEntity<User> register(RegisterDTO registerDTO) {
        if (userRepository.findByEmail(registerDTO.getEmail()) != null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // 400
        }

        String hashedPassword = passwordEncoder.encode(registerDTO.getPassword());
        User hashedUser = User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(hashedPassword)
                .isAdmin(false)
                .build();

        User addedUser = this.userRepository.addNewEntity(hashedUser);
        return new ResponseEntity<>(addedUser, HttpStatus.OK);
    }

    public User changePassword(int id, String currentPassword, String newPassword) {
        User user = this.userRepository.getEntity(id);

        if(user != null && user.getPassword().equals(currentPassword)) {
            return this.userRepository.changePassword(id, newPassword);
        }
        return null;
    }

    public User updatePermissions(int id) {
        return this.userRepository.updatePermissions(id);
    }

    public List<User> getAllEntities() {
        return this.userRepository.getAllEntities();
    }

    public User getEntity(int id) {
        return this.userRepository.getEntity(id);
    }

    public User addNewEntity(User user) throws Exception {
        return this.userRepository.addNewEntity(user);
    }

    public User updateEntity(int id, User user) {
        return this.userRepository.updateEntity(id, user);
    }

    public boolean deleteEntity(int id) {
        return this.userRepository.deleteEntity(id);
    }

}
