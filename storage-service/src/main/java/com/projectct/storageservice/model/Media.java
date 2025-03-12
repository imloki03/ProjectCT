package com.projectct.storageservice.model;


import com.projectct.storageservice.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime uploadTime;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    private String link;

    @ManyToOne
    @JoinColumn(name = "previous_version_id")
    private Media previousVersion;

    @OneToOne
    @JoinColumn(name = "newer_version_id")
    private Media newerVersion;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;
}
