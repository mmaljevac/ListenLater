package hr.tvz.listenlater.model;

import hr.tvz.listenlater.model.enums.Role;
import hr.tvz.listenlater.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false, unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", columnDefinition = "ENUM('USER', 'ADMIN') default 'USER'")
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", columnDefinition = "ENUM('ACTIVE', 'DEACTIVATED', 'SUSPENDED') default 'ACTIVE'")
    private Status status = Status.ACTIVE;

    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.DATE)
    private LocalDate dateCreated = LocalDate.now();

}