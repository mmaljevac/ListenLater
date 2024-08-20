package hr.tvz.listenlater.model;

import hr.tvz.listenlater.model.enums.Action;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@IdClass(SavedAlbumId.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SAVED_ALBUMS")
public class SavedAlbum {

    @Id
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private AppUser user;

    @Id
    @ManyToOne
    @JoinColumn(name = "ALBUM_ID", nullable = false)
    private Album album;

    @Transient
    private Long userId;

    @Transient
    private Long albumId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTION", nullable = false)
    private Action action;

    @Column(name = "DATE_ADDED", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant dateAdded;

    public SavedAlbum(AppUser user, Album album, Action action) {
        this.user = user;
        this.album = album;
        this.action = action;
    }

}
