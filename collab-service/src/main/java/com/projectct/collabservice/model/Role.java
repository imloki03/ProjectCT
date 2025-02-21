package com.projectct.collabservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long projectId;


    @ManyToMany
    @JoinTable(
            name = "role_function",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "function_id")
    )
    private List<AppFunction> functionList;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<Collaborator> collabList;
}
