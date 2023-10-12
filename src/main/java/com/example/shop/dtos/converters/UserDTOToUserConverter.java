package com.example.shop.dtos.converters;

import com.example.shop.dtos.ItemDTO;
import com.example.shop.dtos.UserDTO;
import com.example.shop.models.Item;
import com.example.shop.models.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDTOToUserConverter implements Converter <UserDTO, User> {
    @Override
    public User convert(UserDTO source) {
        User user = new User();
        user.setId(source.id());
        user.setName(source.name());
        user.setRoles(source.roles());
        user.setBirthDate(source.birthDate());
        user.setEmail(source.email());
        user.setSurname(source.surname());
        user.setUsername(source.username());
        return user;
    }

}
