package hr.tvz.listenlater.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FRIENDS")
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER1_ID", nullable = false)
    private AppUser user1;

    @Transient
    private Long user1Id;

    @ManyToOne
    @JoinColumn(name = "USER2_ID", nullable = false)
    private AppUser user2;

    @Transient
    private Long user2Id;

    @Column(name = "TIMESTAMP_ADDED", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant timestampAdded;

}