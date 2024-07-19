package hr.tvz.listenlater.security.controller;

import hr.tvz.listenlater.model.dto.LoginDTO;
import hr.tvz.listenlater.model.dto.RegisterDTO;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.security.entity.AuthRequest;
import hr.tvz.listenlater.security.entity.AuthUserInfo;
import hr.tvz.listenlater.security.service.JwtService;
import hr.tvz.listenlater.security.service.AuthUserInfoService;
import hr.tvz.listenlater.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthUserController {

    @Autowired
    private AuthUserInfoService authUserInfoService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<Object>> login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<CustomResponse<Object>> register(@RequestBody RegisterDTO registerDto) {
        return authService.register(registerDto);
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody AuthUserInfo authUserInfo) {
        return authUserInfoService.addUser(authUserInfo);
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public String generateToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

}
