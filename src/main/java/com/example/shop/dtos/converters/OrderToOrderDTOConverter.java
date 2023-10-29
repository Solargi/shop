package com.example.shop.dtos.converters;

import com.example.shop.dtos.OrderDTO;
import com.example.shop.models.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderToOrderDTOConverter implements Converter <Order, OrderDTO> {

    @Override
    public OrderDTO convert(Order source) {
        return new OrderDTO(source.getId(),
                source.getUser(),
                source.getStatus(),
                source.getShippingAddress(),
                source.getShippingCost(),
                source.getTotalCost(),
                source.getPaid());
    }
}
