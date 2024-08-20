package hr.tvz.listenlater.model.dto;

import hr.tvz.listenlater.model.Album;
import hr.tvz.listenlater.model.enums.InviteType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class InviteDTO {

    private Long id;
    private UserDTO sender;
    private UserDTO receiver;
    private InviteType inviteType;
    private Album album;
    private String message;
    private Instant timestampCreated;

}
