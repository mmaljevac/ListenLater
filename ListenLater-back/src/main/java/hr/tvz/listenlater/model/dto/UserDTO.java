package hr.tvz.listenlater.model.dto;

import hr.tvz.listenlater.model.enums.Role;
import hr.tvz.listenlater.model.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private Role role;
    private Status status;
    private LocalDate dateCreated;
}
