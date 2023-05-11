package com.example.dpp_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String fName;
    private String lName;
    private String email;

    public void fromOrder(OrderDTO orderDTO) {
        this.fName = orderDTO.getFName();
        this.lName = orderDTO.getLName();
        this.email = orderDTO.getEmail();
    }
}
