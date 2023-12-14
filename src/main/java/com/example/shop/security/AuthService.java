package com.example.shop.security;

import com.example.shop.dtos.UserDTO;
import com.example.shop.dtos.converters.UserToUserDTOConverter;
import com.example.shop.models.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserToUserDTOConverter userToUserDTOConverter;

    public Map<String, Object> createLoginInfo(Authentication authentication){
        //get principal (authenticated user from authentication) and
        // cast it to UserPrincipal object
        UserPrincipal userprincipal = (UserPrincipal)authentication.getPrincipal();
        //get user from principal
        User user = userprincipal.getUser();
        //convert user to dto
        UserDTO userDTO = this.userToUserDTOConverter.convert(user);
        //create JWT
        String token = this.jwtProvider.createToken(authentication);
        //put info into map object and return it
        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put("userInfo", userDTO);
        loginMap.put("token", token);

        return loginMap;
    }
}
