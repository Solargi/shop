package com.example.shop.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;


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


//    @Profile({"!prod"})
//    jsut for debugging/ test purposes
    @Data
    @Accessors(chain = true)
    private static class Info {
        private String application;
        private Authentication auth;
    }

    //just for test purposes
//    @Profile({"!prod"})
    @GetMapping("/info")
    public Object getInfo(Authentication auth, JwtAuthenticationToken principal, @RequestHeader(name="Authorization") String token) {
        return principal.getTokenAttributes().get("userId");
        
    }
}
