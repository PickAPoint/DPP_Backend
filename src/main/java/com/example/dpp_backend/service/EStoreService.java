package com.example.dpp_backend.service;

import com.example.dpp_backend.model.*;
import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.repository.ClientRepository;
import com.example.dpp_backend.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class EStoreService {
    private final ClientRepository clientRepository;
    private final PackageRepository packageRepository;

    public int addNewOrder(OrderDTO orderDTO) {
        Client client = new Client();
        client.fromOrder(orderDTO);
        clientRepository.save(client);

        State state = new State();
        state.setOrderState("OrderPlaced");
        state.setOrderDate(orderDTO.getOrderDate());

        Package pkj = new Package();
        pkj.setClient(client);
        pkj.getStates().add(state);
        pkj.fromOrder(orderDTO);
        pkj = packageRepository.save(pkj);

        return pkj.getId();
    }

    public boolean updateOrder(UpdateOrderDTO updateOrderDTO) {
        if (!updateOrderDTO.isValid()){
            return false;
        }
        Package pkj = packageRepository.findById(updateOrderDTO.getPackageId()).orElse(null);
        if (pkj == null) {
            return false;
        }
        if (!pkj.canBeUpdated()) {
            return false;
        }
        State state = new State();
        state.setOrderState(updateOrderDTO.getNewState());
        state.setOrderDate(new Date());
        pkj.getStates().add(state);
        pkj.setOrderState(updateOrderDTO.getNewState());
        packageRepository.save(pkj);
        return true;
    }
}
