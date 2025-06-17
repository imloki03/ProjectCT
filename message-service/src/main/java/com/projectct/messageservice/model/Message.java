package com.projectct.messageservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime sentTime;
    private boolean isPinned;
    private boolean inStorage;
    private LocalDateTime pinTime;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MessageReader> readerList;

    private Long mediaId;

    @ManyToOne
    @JoinColumn(name = "chatbox_id")
    private Chatbox chatbox;
}
