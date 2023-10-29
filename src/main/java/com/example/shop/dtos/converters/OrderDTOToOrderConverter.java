package com.example.shop.dtos.converters;

import com.example.shop.dtos.OrderDTO;
import com.example.shop.models.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderDTOToOrderConverter implements Converter <OrderDTO, Order> {
    @Override
    public Order convert(OrderDTO source) {
        Order order = new Order();
        order.setId(source.id());
        order.setUser(source.user());
        order.setShippingAddress(source.shippingAddress());
        order.setStatus(source.status());
        //generate order item list using cart items?
//        order.setOrderItemList(source.);
        order.setTotalCost(source.totalCost());
        order.setPaid(source.paid());
        return order;
    }
}
