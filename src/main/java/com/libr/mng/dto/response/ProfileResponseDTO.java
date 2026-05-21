package com.libr.mng.dto.response;

import lombok.Data;

@Data
public class ProfileResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private Long employeeId;
    private String profileImage;
}
