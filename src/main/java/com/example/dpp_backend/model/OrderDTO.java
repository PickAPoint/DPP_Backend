package com.example.dpp_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String fName;
    private String lName;
    private String email;
    private String eStore;
    private Date orderDate;
}
