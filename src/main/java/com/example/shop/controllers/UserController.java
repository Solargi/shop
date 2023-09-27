package com.example.shop.controllers;

import com.example.shop.services.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController (UserService userService){
        this.userService = userService;
    }
}
