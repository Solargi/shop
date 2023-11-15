package com.example.shop.dtos.converters;

import com.example.shop.dtos.OrderItemResponseDTO;
import com.example.shop.models.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class OrderItemToOrderItemResponseDTOConverter implements Converter<OrderItem, OrderItemResponseDTO> {
    OrderToOrderDTOConverter orderToOrderDTOConverter;
    ItemToItemDTOConverter itemToItemDTOConverter;
    @Override
    public OrderItemResponseDTO convert(OrderItem source) {
        return new OrderItemResponseDTO(source.getId(),
                this.orderToOrderDTOConverter.convert(source.getOrder()),
                this.itemToItemDTOConverter.convert(source.getItem()),
                source.getQuantity(),source.getTotalCost());
    }
}
