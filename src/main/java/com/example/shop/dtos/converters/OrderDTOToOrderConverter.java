package com.example.shop.dtos.converters;

import com.example.shop.dtos.OrderDTO;
import com.example.shop.dtos.UserDTO;
import com.example.shop.models.Order;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderDTOToOrderConverter implements Converter <OrderDTO, Order> {
    UserDTOToUserConverter userDTOToUserConverter;
    AddressDTOToAddressConverter addressDTOToAddressConverter;
    @Override
    public Order convert(OrderDTO source) {
        Order order = new Order();
        order.setId(source.id());
        order.setUser(this.userDTOToUserConverter.convert(source.user()));
        order.setShippingAddress(this.addressDTOToAddressConverter.convert(source.shippingAddress()));
        order.setStatus(source.status());
        //generate order item list using cart items?
//        order.setOrderItemList(source.);
        order.setTotalCost(source.totalCost());
        order.setPaid(source.paid());
        return order;
    }
}
