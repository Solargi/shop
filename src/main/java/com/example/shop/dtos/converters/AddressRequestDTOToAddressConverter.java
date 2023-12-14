package com.example.shop.dtos.converters;

import com.example.shop.dtos.AddressRequestDTO;
import com.example.shop.models.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressRequestDTOToAddressConverter implements Converter<AddressRequestDTO, Address> {
    private UserDTOToUserConverter userDTOToUserConverter;

    @Override
    public Address convert(AddressRequestDTO source) {
        Address address = new Address();
        address.setState(source.state());
        address.setCity(source.city());
        address.setStreet(source.street());
        address.setZipCode(source.zipCode());
        address.setCountry(source.country());
        return address;
    }
}