package com.example.shop.dtos.converters;

import com.example.shop.dtos.AddressDTO;
import com.example.shop.models.Address;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AddressDTOToAddressConverter implements Converter <AddressDTO, Address> {
    private UserDTOToUserConverter userDTOToUserConverter;

    @Override
    public Address convert(AddressDTO source) {
        Address address = new Address();
        address.setId(source.id());
        address.setUser(this.userDTOToUserConverter.convert(source.userDTO()));
        address.setState(source.state());
        address.setCity(source.city());
        address.setStreet(source.street());
        address.setZipCode(source.zipCode());
        address.setCountry(source.country());
        return address;
    }
}
