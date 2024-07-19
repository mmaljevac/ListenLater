package hr.tvz.listenlater.security.service;

import hr.tvz.listenlater.security.entity.AuthUserInfo;
import hr.tvz.listenlater.security.repository.AuthUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserInfoService implements UserDetailsService {

    @Autowired
    private AuthUserInfoRepository authUserInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AuthUserInfo> userDetail = authUserInfoRepository.findByName(username);

        // Converting userDetail to UserDetails
        return userDetail.map(AuthUserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    public String addUser(AuthUserInfo authUserInfo) {
        authUserInfo.setPassword(passwordEncoder.encode(authUserInfo.getPassword()));
        authUserInfoRepository.save(authUserInfo);
        return "User added successfully.";
    }


}
