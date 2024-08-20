package hr.tvz.listenlater.model;

import hr.tvz.listenlater.model.enums.InviteType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "INVITES")
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SENDER_ID", nullable = false)
    private AppUser sender;

    @Transient
    private Long senderId;

    @ManyToOne
    @JoinColumn(name = "RECEIVER_ID", nullable = false)
    private AppUser receiver;

    @Transient
    private Long receiverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "INVITE_TYPE", nullable = false)
    private InviteType inviteType;

    @ManyToOne
    @JoinColumn(name = "ALBUM_ID")
    private Album album;

    @Transient
    private Long albumId;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "TIMESTAMP_CREATED", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant timestampCreated;

}
