package com.order.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OderResponse {
    private Long id;
    private String orderNumber;
    private List<OrderLineItemsDto> orderLineItems;
}
