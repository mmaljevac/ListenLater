package hr.tvz.listenlater.service;

import hr.tvz.listenlater.model.Album;
import hr.tvz.listenlater.model.AppUser;
import hr.tvz.listenlater.model.Invite;
import hr.tvz.listenlater.model.SavedAlbum;
import hr.tvz.listenlater.model.dto.InviteDTO;
import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.repository.AlbumRepository;
import hr.tvz.listenlater.repository.InviteRepository;
import hr.tvz.listenlater.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;

    public ResponseEntity<CustomResponse<Object>> getInvitesByReceiver(String username) {
        CustomResponse<Object> response;

        Optional<AppUser> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isEmpty()) {
            response = CustomResponse.builder().success(false).message("User not found.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        AppUser user = optionalUser.get();

        List<Invite> invitesByUser = inviteRepository.getInvitesByReceiverId(user.getId());

        List<InviteDTO> invitesByUserDTO = mapInvitesToDTO(invitesByUser);

        response = CustomResponse.builder().success(true).message("Success getting data.").data(invitesByUserDTO).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> sendFriendRequest(String curUserName, String friendUserName) {
        CustomResponse<Object> response;

        Optional<AppUser> optionalCurUser = userRepository.findUserByUsername(curUserName);
        Optional<AppUser> optionalFriendUser = userRepository.findUserByUsername(friendUserName);

        if (optionalCurUser.isEmpty() || optionalFriendUser.isEmpty()) {
            response = CustomResponse.builder().success(false).message("User not found.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        AppUser curUser = optionalCurUser.get();
        AppUser friendUser = optionalFriendUser.get();

        boolean success = inviteRepository.sendFriendRequest(curUserName, curUser.getId(), friendUser.getId());

        response = CustomResponse.builder().success(success).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> recommendAlbum(String curUserName, String friendUserName, String albumFullName, String message) {
        CustomResponse<Object> response;

        Optional<AppUser> optionalCurUser = userRepository.findUserByUsername(curUserName);
        Optional<AppUser> optionalFriendUser = userRepository.findUserByUsername(friendUserName);
        if (optionalCurUser.isEmpty() || optionalFriendUser.isEmpty()) {
            response = CustomResponse.builder().success(false).message("User not found.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        AppUser curUser = optionalCurUser.get();
        AppUser friendUser = optionalFriendUser.get();

        String finalAlbumFullName = albumFullName.replace("+", "%2B").replace(" ", "+");
        Optional<Album> optionalSavedAlbum = albumRepository.findByFullName(finalAlbumFullName);
        if (optionalSavedAlbum.isEmpty()) {
            response = CustomResponse.builder().success(false).data("Album not found by fullName").build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        Album album = optionalSavedAlbum.get();

        boolean success = inviteRepository.recommendAlbum(curUser.getId(), friendUser.getId(), album.getId(), message);

        response = CustomResponse.builder().success(success).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomResponse<Object>> deleteInvite(Long inviteId) {
        CustomResponse<Object> response;

        boolean success = inviteRepository.deleteInvite(inviteId);

        response = CustomResponse.builder().success(success).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private List<InviteDTO> mapInvitesToDTO(List<Invite> invites) {
        return invites.stream()
                .map(i -> new InviteDTO(
                        i.getId(),
                        userService.mapUserToDTO(i.getSender()),
                        userService.mapUserToDTO(i.getReceiver()),
                        i.getInviteType(),
                        i.getAlbum(),
                        i.getMessage(),
                        i.getTimestampCreated()
                ))
                .toList();
    }

}
