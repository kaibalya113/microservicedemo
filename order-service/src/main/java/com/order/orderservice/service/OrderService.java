package com.order.orderservice.service;

import com.order.orderservice.dto.InventoryResponse;
import com.order.orderservice.dto.OrderLineItemsDto;
import com.order.orderservice.dto.OrderRequest;
import com.order.orderservice.model.Order;
import com.order.orderservice.model.OrderLineItems;
import com.order.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    private final WebClient.Builder webClient;


    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

       List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItems()
                .stream()
                .map(this :: mapToDto).toList();
       order.setOrderLineItems(orderLineItems);
       // call Inventory service and place if product is in stock
        // we will use webclient to communicate between services
      List<String> skuCodeLst = order.getOrderLineItems().stream()
              .map(orderLineItemslst -> orderLineItemslst.getSkuCode()).toList();
      InventoryResponse[] inventoryResponses = webClient.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodeLst).build())
                        .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                        .block();

      boolean allProductsInStock = Arrays.stream(inventoryResponses)
                .allMatch(InventoryResponse::isInStock);
      if(allProductsInStock){
          orderRepository.save(order);
      }else{
          throw  new IllegalArgumentException("Product is not in stock");
      }

    }


    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
