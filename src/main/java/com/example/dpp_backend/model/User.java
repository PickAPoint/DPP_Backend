package com.example.dpp_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String email;
    private String name;
    private String password;
    private String address;
    private String contact;
    private String type;

    public void fromRegister(RegisterDTO registerDTO) {
        this.email = registerDTO.getEmail();
        this.name = registerDTO.getName();
        this.address = registerDTO.getAddress();
        this.contact = registerDTO.getContact();
        this.type = "Pending";
        setPassword(registerDTO.getPassword());
    }

    public void setPassword(String password) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.password = encoder.encode(password);
    }

    public boolean checkPassword(String password) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder.matches(password, this.password);
    }
}
