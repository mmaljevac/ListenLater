package hr.tvz.listenlater.controller;

import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.service.InviteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invites")
@AllArgsConstructor()
@CrossOrigin(origins = "http://localhost:3000")
public class InviteController {

    private final InviteService inviteService;

    @GetMapping("/{username}")
    public ResponseEntity<CustomResponse<Object>> getInvitesByReceiver(@PathVariable String username) {
        return inviteService.getInvitesByReceiver(username);
    }

    @PostMapping("/friend-request")
    public ResponseEntity<CustomResponse<Object>> sendFriendRequest(@RequestParam String curUserName, @RequestParam String friendUserName) {
        return inviteService.sendFriendRequest(curUserName, friendUserName);
    }

    @PostMapping("/recommend-album")
    public ResponseEntity<CustomResponse<Object>> recommendAlbum(@RequestParam String curUserName,
                                                                 @RequestParam String friendUserName,
                                                                 @RequestParam String albumFullName,
                                                                 @RequestParam String message) {
        return inviteService.recommendAlbum(curUserName, friendUserName, albumFullName, message);
    }

    @DeleteMapping("/remove/{inviteId}")
    public ResponseEntity<CustomResponse<Object>> deleteInvite(@PathVariable Long inviteId) {
        return inviteService.deleteInvite(inviteId);
    }

}
