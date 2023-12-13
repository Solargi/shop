package com.example.shop.dtos.converters;

import com.example.shop.Embeddables.OrderItemId;
import com.example.shop.dtos.OrderItemRequestDTO;
import com.example.shop.models.OrderItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemRequestDTOToOrderItemConverter implements Converter<OrderItemRequestDTO, OrderItem> {
    @Override
    public OrderItem convert(OrderItemRequestDTO source) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(source.quantity());
        return orderItem;
    }

    public OrderItem convert (OrderItemRequestDTO source, Integer orderId, Integer itemId){
        OrderItem orderItem = new OrderItem();
        OrderItemId orderItemId = new OrderItemId(orderId,itemId);
        orderItem.setId(orderItemId);
        orderItem.setQuantity(source.quantity());
        return orderItem;
    }
}
