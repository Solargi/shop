package com.example.shop.dtos.converters;

import com.example.shop.dtos.OrderItemDTO;
import com.example.shop.models.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class OrderItemToOrderItemDTOConverter implements Converter<OrderItem, OrderItemDTO> {
    OrderToOrderDTOConverter orderToOrderDTOConverter;
    ItemToItemDTOConverter itemToItemDTOConverter;
    @Override
    public OrderItemDTO convert(OrderItem source) {
        return new OrderItemDTO(source.getId(),
                this.orderToOrderDTOConverter.convert(source.getOrder()),
                this.itemToItemDTOConverter.convert(source.getItem()),
                source.getQuantity(),source.getTotalCost());
    }
}
