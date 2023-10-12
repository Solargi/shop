package com.example.shop.dtos.converters;

import com.example.shop.dtos.UserDTO;
import com.example.shop.models.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDTOConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(User source){
        return new UserDTO(source.getId(),
                source.getUsername(),
                source.getName(),
                source.getSurname(),
                source.getEmail(),
                source.getBirthDate(),
                source.getRoles());
    }

}
