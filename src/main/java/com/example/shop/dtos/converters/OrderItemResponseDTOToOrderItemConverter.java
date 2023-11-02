package com.example.shop.dtos.converters;

import com.example.shop.dtos.OrderItemResponseDTO;
import com.example.shop.models.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderItemResponseDTOToOrderItemConverter implements Converter <OrderItemResponseDTO, OrderItem> {
    ItemDTOToItemConverter itemDTOToItemConverter;
    OrderDTOToOrderConverter orderDTOToOrderConverter;
    @Override
    public OrderItem convert(OrderItemResponseDTO source) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(source.id());
        orderItem.setItem(this.itemDTOToItemConverter.convert(source.itemDTO()));
        orderItem.setOrder(this.orderDTOToOrderConverter.convert(source.orderDTO()));
        orderItem.setQuantity(source.quantity());
        orderItem.setTotalCost(source.totalCost());
        return orderItem;
    }
}
