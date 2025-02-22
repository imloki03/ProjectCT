package com.projectct.storageservice.DTO.Media.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MediaRequest {
    String name;
    String description;
    String filename;
    String size;
    String link;
}
