package com.example.shop.security;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("${api.endpoint.base-url}/users")
public class AuthController {
    private final AuthService authService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    // the authentication object is passed from springboot when login is successfull;
    public ResponseEntity<Object> getLoginInfo (Authentication authentication){
        LOGGER.debug("Authenticated user : '{}'", authentication.getName());
        return ResponseEntity.ok(this.authService.createLoginInfo(authentication));
    }
}
