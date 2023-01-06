package com.lalbrecht.mediasite.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Principal {
    private String id;
    private String username;
    private boolean mod;
}
