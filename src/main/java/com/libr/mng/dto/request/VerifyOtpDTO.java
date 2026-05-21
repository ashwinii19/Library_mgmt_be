package com.libr.mng.dto.request;

import lombok.Data;

@Data
public class VerifyOtpDTO {
    private String email;
    private String otp;
}