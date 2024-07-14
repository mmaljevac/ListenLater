package hr.tvz.listenlater.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class SavedAlbumId implements Serializable {

    private Long user;
    private Long album;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavedAlbumId that = (SavedAlbumId) o;
        return Objects.equals(user, that.user) && Objects.equals(album, that.album);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, album);
    }
}
