package hr.tvz.listenlater.controller;

import hr.tvz.listenlater.model.AppUser;
import hr.tvz.listenlater.model.dto.ChangePasswordDTO;
import hr.tvz.listenlater.model.dto.StringDTO;
import hr.tvz.listenlater.model.enums.Role;
import hr.tvz.listenlater.model.enums.Status;
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
    public ResponseEntity<CustomResponse<Object>> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return userService.changePassword(changePasswordDTO.getUsername(), changePasswordDTO.getCurrentPassword(), changePasswordDTO.getNewPassword());
    }

    @PatchMapping("/updateUserRole/{id}")
    public ResponseEntity<CustomResponse<Object>> updateUserRole(@PathVariable Long id, @RequestBody StringDTO stringDTO) {
        return userService.updateUserRole(id, Role.fromValue(stringDTO.getString()));
    }

    @PatchMapping("/updateUserStatus/{newStatusString}/{userId}")
    public ResponseEntity<CustomResponse<Object>> updateUserStatus(@PathVariable String newStatusString, @PathVariable Long userId) {
        return userService.updateUserStatus(userId, Status.fromValue(newStatusString));
    }

    @GetMapping
    public ResponseEntity<CustomResponse<Object>> getAllEntities(@RequestParam(name = "username", required = false) String username) {
        return userService.getAllEntities(username);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Object>> getEntityById(@PathVariable Long id) {
        return userService.getEntityById(id);
    }

    @PostMapping
    public ResponseEntity<CustomResponse<Object>> addNewEntity(@RequestBody AppUser user) {
        return userService.addNewEntity(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Object>> updateEntity(@PathVariable Long id, @RequestBody AppUser user) {
        return userService.updateEntity(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Object>> deleteEntity(@PathVariable Long id) {
        return userService.deleteEntity(id);
    }

}
