package com.lalbrecht.mediasite.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPasswordRequest {
    private String oldPassword;
    private String password1;
    private String password2;
}
