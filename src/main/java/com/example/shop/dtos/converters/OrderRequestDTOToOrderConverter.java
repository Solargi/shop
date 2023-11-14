package com.example.shop.dtos.converters;

import com.example.shop.dtos.OrderRequestDTO;
import com.example.shop.models.Order;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderRequestDTOToOrderConverter implements Converter<OrderRequestDTO, Order> {
    AddressDTOToAddressConverter addressDTOToAddressConverter;
    @Override
    public Order convert(OrderRequestDTO source) {
        Order order = new Order();
        order.setId(source.id());
        order.setStatus(source.status());
        order.setPaid(source.paid());
        order.setShippingAddress(this.addressDTOToAddressConverter.convert(source.shippingAddress()));
        order.setShippingCost(source.shippingCost());
        order.setTotalCost(source.totalCost());
        return order;
    }
}
