package hr.tvz.listenlater.controller;

import hr.tvz.listenlater.service.InviteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invites")
@AllArgsConstructor()
@CrossOrigin(origins = "http://localhost:3000")
public class InviteController {

    private final InviteService inviteService;

    // get invites by username

    // create invite

    // delete invite

}
