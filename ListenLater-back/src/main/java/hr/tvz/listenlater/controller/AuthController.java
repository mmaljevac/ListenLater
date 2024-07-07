package hr.tvz.listenlater.controller;

import hr.tvz.listenlater.model.dto.LoginDTO;
import hr.tvz.listenlater.model.dto.RegisterDTO;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<Object>> login(@RequestBody final LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<CustomResponse<Object>> register(@RequestBody final RegisterDTO registerDto) {
        return authService.register(registerDto);
    }

}
