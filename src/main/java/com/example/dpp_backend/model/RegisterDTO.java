package com.example.dpp_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private String email;
    private String password;
    private String name;
    private String address;
    private String contact;
}
