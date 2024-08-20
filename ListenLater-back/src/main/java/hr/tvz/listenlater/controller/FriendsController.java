package hr.tvz.listenlater.controller;

import hr.tvz.listenlater.model.response.CustomResponse;
import hr.tvz.listenlater.service.FriendsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friends")
@AllArgsConstructor()
@CrossOrigin(origins = "http://localhost:3000")
public class FriendsController {

    private final FriendsService friendsService;

    @GetMapping("/{username}")
    public ResponseEntity<CustomResponse<Object>> getFriendsByUsername(@PathVariable String username) {
        return friendsService.getFriendsByUsername(username);
    }

    @GetMapping("/friend-status")
    public ResponseEntity<CustomResponse<Object>> getFriendStatus(@RequestParam String curUserName, @RequestParam String friendUserName) {
        return friendsService.getFriendStatus(curUserName, friendUserName);
    }

    @PostMapping("/add")
    public ResponseEntity<CustomResponse<Object>> addFriend(@RequestParam String curUserName, @RequestParam String friendUserName) {
        return friendsService.addFriend(curUserName, friendUserName);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CustomResponse<Object>> removeFriend(@RequestParam String curUserName, @RequestParam String friendUserName) {
        return friendsService.removeFriend(curUserName, friendUserName);
    }

}
