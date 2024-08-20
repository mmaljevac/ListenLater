package hr.tvz.listenlater.service;

import hr.tvz.listenlater.model.AppUser;
import hr.tvz.listenlater.model.Friends;
import hr.tvz.listenlater.model.dto.UserDTO;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.repository.FriendsRepository;
import hr.tvz.listenlater.repository.InviteRepository;
import hr.tvz.listenlater.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final InviteRepository inviteRepository;

    public ResponseEntity<CustomResponse<Object>> getFriendsByUsername(String username) {
        CustomResponse<Object> response;

        Optional<AppUser> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isEmpty()) {
            response = CustomResponse.builder().success(false).message("User not found.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        AppUser user = optionalUser.get();

        List<AppUser> friendsByUser = friendsRepository.getFriendsByUserId(user.getId());

        List<UserDTO> friendsByUserDTO = new ArrayList<>();
        if (!friendsByUser.isEmpty()) {
            friendsByUserDTO = friendsByUser.stream()
                    .map(userService::mapUserToDTO)
                    .toList();
        }

        response = CustomResponse.builder().success(true).message("Success getting data.").data(friendsByUserDTO).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> getFriendStatus(String curUserName, String friendUserName) {
        CustomResponse<Object> response;

        Optional<AppUser> optionalCurUser = userRepository.findUserByUsername(curUserName);
        Optional<AppUser> optionalFriendUser = userRepository.findUserByUsername(friendUserName);

        if (optionalCurUser.isEmpty() || optionalFriendUser.isEmpty()) {
            response = CustomResponse.builder().success(false).message("User not found.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        AppUser curUser = optionalCurUser.get();
        AppUser friendUser = optionalFriendUser.get();

        String status = "";
        if (friendsRepository.isFriends(curUser.getId(), friendUser.getId())) {
            status = "friend";
        }
        else if (inviteRepository.isFriendRequestPending(curUser.getId(), friendUser.getId())) {
            status = "pending";
        }

        response = CustomResponse.builder().success(true).data(status).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> addFriend(String curUserName, String friendUserName) {
        CustomResponse<Object> response;

        Optional<AppUser> optionalCurUser = userRepository.findUserByUsername(curUserName);
        Optional<AppUser> optionalFriendUser = userRepository.findUserByUsername(friendUserName);

        if (optionalCurUser.isEmpty() || optionalFriendUser.isEmpty()) {
            response = CustomResponse.builder().success(false).message("User not found.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        AppUser curUser = optionalCurUser.get();
        AppUser friendUser = optionalFriendUser.get();

        boolean success = friendsRepository.addFriend(curUser.getId(), friendUser.getId());

        response = CustomResponse.builder().success(success).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> removeFriend(String curUserName, String friendUserName) {
        CustomResponse<Object> response;

        Optional<AppUser> optionalCurUser = userRepository.findUserByUsername(curUserName);
        Optional<AppUser> optionalFriendUser = userRepository.findUserByUsername(friendUserName);

        if (optionalCurUser.isEmpty() || optionalFriendUser.isEmpty()) {
            response = CustomResponse.builder().success(false).message("User not found.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        AppUser curUser = optionalCurUser.get();
        AppUser friendUser = optionalFriendUser.get();

        boolean success = friendsRepository.removeFriend(curUser.getId(), friendUser.getId());

        response = CustomResponse.builder().success(success).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
