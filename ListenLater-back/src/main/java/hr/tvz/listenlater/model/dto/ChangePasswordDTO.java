package hr.tvz.listenlater.model.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String email;
    private String currentPassword;
    private String newPassword;
}
