package com.example.dpp_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {

    private int id;
    private String email;
    private String name;
    private String address;
    private String type;

    public UserDetailsDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.address = user.getAddress();
        this.type = user.getType();
    }
}