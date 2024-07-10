package hr.tvz.listenlater.controller;

import hr.tvz.listenlater.model.User;
import hr.tvz.listenlater.model.dto.ChangePasswordDTO;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    @PatchMapping("/changePassword")
    public ResponseEntity<CustomResponse<Object>> changePassword(@RequestBody final ChangePasswordDTO changePasswordDTO) {
        return userService.changePassword(changePasswordDTO.getEmail(), changePasswordDTO.getCurrentPassword(), changePasswordDTO.getNewPassword());
    }

    @PatchMapping("/updateUserRole/{id}")
    public ResponseEntity<CustomResponse<Object>> updateUserRole(@PathVariable final Long id, @RequestBody final String newRoleString) {
        return userService.updateUserRole(id, newRoleString);
    }

    @PatchMapping("/updateUserStatus/{newStatusString}")
    public ResponseEntity<CustomResponse<Object>> updateUserStatus(@PathVariable final String newStatusString, @RequestBody final String email) {
        return userService.updateUserStatus(email, newStatusString);
    }

    @GetMapping
    public ResponseEntity<CustomResponse<Object>> getAllEntities() {
        return userService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Object>> getEntityById(@PathVariable final Long id) {
        return userService.getEntityById(id);
    }

    @PostMapping
    public ResponseEntity<CustomResponse<Object>> addNewEntity(@RequestBody final User user) {
        return userService.addNewEntity(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Object>> updateEntity(@PathVariable final Long id, @RequestBody final User user) {
        return userService.updateEntity(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Object>> deleteEntity(@PathVariable final Long id) {
        return userService.deleteEntity(id);
    }

}
