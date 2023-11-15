package com.example.shop.dtos.converters;

import com.example.shop.dtos.OrderRequestDTO;
import com.example.shop.models.Order;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderToOrderRequestDTOConverter implements Converter<Order, OrderRequestDTO> {
    AddressToAddressDTOConverter addressToAddressDTOConverter;

    @Override
    public OrderRequestDTO convert(Order source) {
        return new OrderRequestDTO(
                source.getId(),
                source.getStatus(),
                addressToAddressDTOConverter.convert(source.getShippingAddress()),
                source.getShippingCost(),
                source.getTotalCost(),
                source.getPaid());
    }
}
