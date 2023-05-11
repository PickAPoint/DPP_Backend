package com.example.dpp_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDTO {

    private int packageId;
    private String newState;

    public boolean isValid() {
        return (
                List.of("Packed", "Shipped", "OutForDelivery", "Delivered", "Cancelled")
                        .contains(this.newState) && this.packageId > 0
        );
    }
}
