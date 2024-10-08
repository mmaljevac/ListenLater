package hr.tvz.listenlater.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ALBUMS")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FULL_NAME", nullable = false, unique = true)
    private String fullName;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "ARTIST", nullable = false)
    private String artist;

    @Column(name = "IMG_URL", nullable = false)
    private String imgUrl;

}
