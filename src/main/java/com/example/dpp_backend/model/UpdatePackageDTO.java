package com.example.dpp_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePackageDTO {

    private int packageId;
    private String newState;
    private String token;

    public boolean isValid() {
        return (
                List.of("Delivered", "Collected")
                        .contains(this.newState) && this.packageId > 0
        );
    }
}
