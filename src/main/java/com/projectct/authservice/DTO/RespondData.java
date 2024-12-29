package com.projectct.authservice.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
public class RespondData<T> {
    private int status;
    private T data;
    private String desc;
}