package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.OrderDTO;
import com.example.dpp_backend.model.PickPointDTO;
import com.example.dpp_backend.model.UpdateOrderDTO;
import com.example.dpp_backend.service.EStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*") // NOSONAR
@RestController
@RequiredArgsConstructor
@RequestMapping("/eStore")
public class EStoreController {

    private final EStoreService eStoreService;

    @PostMapping("/order")
    public ResponseEntity<Integer> order(
            @RequestBody OrderDTO orderDTO
    ){
        log.info("Ordering: {}", orderDTO);
        return ResponseEntity.ok(eStoreService.addNewOrder(orderDTO));
    }

    @PutMapping("/order")
    public ResponseEntity<Boolean> updateOrder(
            @RequestBody UpdateOrderDTO orderDTO
            ){
        log.info("Updating order: {}", orderDTO);
        return ResponseEntity.ok(eStoreService.updateOrder(orderDTO));
    }

    @GetMapping("/pickUps")
    public List<PickPointDTO> getPickPoints(){
        return eStoreService.getPickPoints();
    }
}
