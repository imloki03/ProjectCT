package com.projectct.storageservice.model;


import com.projectct.storageservice.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String filename;
    private String size;
    private LocalDateTime uploadTime;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    private String link;

    @OneToOne
    @JoinColumn(name = "previous_version_id")
    private Media previousVersion;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;
}
