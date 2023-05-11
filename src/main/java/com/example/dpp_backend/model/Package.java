package com.example.dpp_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_package")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String eStore;
    private Date orderDate;
    private String orderState;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<State> states = new ArrayList<>();

    @OneToOne
    private Client client;

}
