package com.example.shop.dtos.converters;

import com.example.shop.dtos.AddressDTO;
import com.example.shop.models.Address;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AddressToAddressDTOConverter implements Converter <Address, AddressDTO> {
    private final UserToUserDTOConverter userToUserDTOConverter;

    @Override
    public AddressDTO convert(Address source) {
        return new AddressDTO(
                source.getId(),
                userToUserDTOConverter.convert(source.getUser()),
                source.getCountry(),
                source.getState(),
                source.getCity(),
                source.getStreet(),
                source.getZipCode()
        );
    }
}
