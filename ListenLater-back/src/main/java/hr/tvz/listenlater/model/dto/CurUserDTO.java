package hr.tvz.listenlater.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurUserDTO {
    private int id;
    private String username;
    private String email;
    private boolean isAdmin;
}
