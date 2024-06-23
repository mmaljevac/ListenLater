package hr.tvz.listenlater.service;

import hr.tvz.listenlater.model.LoginDTO;
import hr.tvz.listenlater.model.User;
import hr.tvz.listenlater.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public User login(LoginDTO loginDTO) {
        User user = this.userRepository.findByEmail(loginDTO.getEmail());

        if(user != null && loginDTO.getPassword().equals(user.getPassword())) {
            return user;
        }
        return null;
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
