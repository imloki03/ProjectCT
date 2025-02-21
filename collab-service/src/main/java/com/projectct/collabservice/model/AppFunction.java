package com.projectct.collabservice.model;

import com.projectct.collabservice.enums.FunctionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String endpoint;

    @Enumerated(EnumType.STRING)
    private FunctionType functionType;

    @ManyToMany(mappedBy = "functionList")
    private List<Role> roleList;
}
