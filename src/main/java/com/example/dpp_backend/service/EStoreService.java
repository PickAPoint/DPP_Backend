package com.example.dpp_backend.service;

import com.example.dpp_backend.model.*;
import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.repository.ClientRepository;
import com.example.dpp_backend.repository.PackageRepository;
import com.example.dpp_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EStoreService {
    private final ClientRepository clientRepository;
    private final PackageRepository packageRepository;
    private final UserRepository userRepository;

    public int addNewOrder(OrderDTO orderDTO) {

        User user = userRepository.findById(orderDTO.getPickUpId()).orElse(null);
        if (user == null) {
            log.error("User not found");
            return -1;
        }

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
            log.error("Invalid update order request");
            return false;
        }
        Package pkj = packageRepository.findById(updateOrderDTO.getPackageId()).orElse(null);
        if (pkj == null) {
            log.error("Package not found");
            return false;
        }
        if (!pkj.canBeUpdated()) {
            log.error("Package cannot be updated");
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


    public List<PickPointDTO> getPickPoints(){
        List<User> users = userRepository.findAll();
        List<PickPointDTO> pickPoints = new ArrayList<>();
        for (User user : users) {
            if (user.getType().equals("Partner")) {
                PickPointDTO pickPointDTO = new PickPointDTO();
                pickPointDTO.setId(user.getId());
                pickPointDTO.setName(user.getName());
                pickPoints.add(pickPointDTO);
            }
        }
        return pickPoints;
    }
}
