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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "message_reader", joinColumns = @JoinColumn(name = "message_id"))
    @Column(name = "reader_username")
    private List<String> readerList;

    private Long mediaId;

    @ManyToOne
    @JoinColumn(name = "chatbox_id")
    private Chatbox chatbox;
}
