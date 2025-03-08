package com.projectct.authservice.DTO.Tag.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagResponse {
    Long id;
    String name;
    String type;
    String description;
}
