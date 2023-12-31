package com.example.shop.dtos.converters;

import com.example.shop.dtos.OrderResponseDTO;
import com.example.shop.models.Order;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderToOrderDTOConverter implements Converter <Order, OrderResponseDTO> {
    UserToUserDTOConverter userToUserDTOConverter;
    AddressToAddressDTOConverter addressToAddressDTOConverter;

    @Override
    public OrderResponseDTO convert(Order source) {
        return new OrderResponseDTO(source.getId(),
                this.userToUserDTOConverter.convert(source.getUser()),
                source.getStatus(),
                this.addressToAddressDTOConverter.convert(source.getShippingAddress()),
                source.getShippingCost(),
                source.getTotalCost(),
                source.getPaid());
    }
}
