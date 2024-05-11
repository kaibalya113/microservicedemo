package com.inventory.inventoryservice.service;

import com.inventory.inventoryservice.dto.InventoryResponse;
import com.inventory.inventoryservice.model.Inventory;
import com.inventory.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode){
        List<InventoryResponse> lst =  inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.get().getSkuCode())
                            .isInStock(inventory.get().getQuantity() > 0)
                            .build()
                ).toList();
        return lst;
    }
}
