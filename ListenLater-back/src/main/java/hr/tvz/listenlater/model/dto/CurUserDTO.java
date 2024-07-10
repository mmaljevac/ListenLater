package hr.tvz.listenlater.model.dto;

import hr.tvz.listenlater.model.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurUserDTO {
    private String username;
    private String email;
    private Role role;
}
