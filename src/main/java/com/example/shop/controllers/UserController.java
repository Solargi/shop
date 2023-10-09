package com.example.shop.controllers;

import com.example.shop.models.User;
import com.example.shop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {
    private final UserService userService;

    public UserController (UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    private ResponseEntity<User> getUser (@PathVariable("userId") int userId){
        User foundUser = this.userService.getUserById(userId);
        return ResponseEntity.ok(foundUser);

    }
}
