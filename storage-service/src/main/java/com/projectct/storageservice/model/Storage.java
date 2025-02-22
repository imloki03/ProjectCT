package com.projectct.storageservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "storage", cascade = CascadeType.REMOVE)
    private List<Media> mediaList = new ArrayList<>();

    private Long projectId;

    public void addMedia(Media media) {
        if (this.mediaList == null)
            this.mediaList = new ArrayList<>();
        this.mediaList.add(media);
    }
}
