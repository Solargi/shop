package com.example.shop.dtos.converters;

import com.example.shop.dtos.OrderItemRequestDTO;
import com.example.shop.models.OrderItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemRequestDTOToOrderItemConverter implements Converter<OrderItemRequestDTO, OrderItem> {
    @Override
    public OrderItem convert(OrderItemRequestDTO source) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(source.id());
        orderItem.setQuantity(source.quantity());
        return orderItem;
    }
}
