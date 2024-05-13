package com.example.shop.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

//looks for jwt token in the authorization header bearer or in the cookies
// this will be automatically picked up by spring security
//@Component
public class CustomBearerTokenResolver implements BearerTokenResolver {
    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @Override
    public String resolve(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        // Exclude token resolution for the login endpoint
        // don't know why this endpoint is not automatically exluded since it's set to
        //. permitall in security config
//        System.out.println(this.baseUrl +"/users/login");
//        System.out.println(requestURI);
        if ((this.baseUrl +"/users/login").equals(requestURI)) {
            return null; // Return null to indicate that token resolution should be skipped
        }
        // Check if the token is present in the Authorization header
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        // Check if the token is present in cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
