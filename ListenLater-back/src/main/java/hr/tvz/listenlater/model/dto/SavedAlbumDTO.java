package hr.tvz.listenlater.model.dto;

import hr.tvz.listenlater.model.Album;
import hr.tvz.listenlater.model.enums.Action;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class SavedAlbumDTO {
    private Album album;
    private Long userId;
    private Action action;
    private Instant dateAdded;
}
