package com.libr.mng.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequestDTO {

    private String name;

    // immutable fields
    private String email;

    private Long employeeId;

    private String role;
}