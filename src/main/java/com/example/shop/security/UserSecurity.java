package com.example.shop.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

@Component("userSecurity")
public class UserSecurity implements AuthorizationManager<RequestAuthorizationContext> {

    private static final String ADMIN_ROLE = "ROLE_admin";
    public boolean hasUserId(Integer jwtUserId, Integer requestUserId){
        return Objects.equals(jwtUserId, requestUserId);
    }

    private boolean isAdmin(Authentication authentication) {
        //get list of granted authorities
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        //following line is only for debug:
//       authorities.stream().map(GrantedAuthority::getAuthority).forEach(thing->System.out.println("role:" + thing));
        //check autorities and returns true if at leas one matches "Role_admin"
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ADMIN_ROLE::equals);
    }

    //checks if the user has role admin or if the user id of the request url is the same as the one in the jwt token
    // if one of these condition is satisfied it allows access otherwise denies it
    // this function is used in the security config class to check requests
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        //get authentication
        Authentication authentication = authenticationSupplier.get();
//        System.out.println("authentication: object " + authentication);
        //if user is admin then permission granted
        if(isAdmin(authentication)){
            return new AuthorizationDecision(true);
        }
        //if role not admin compare id of jwt token with id of the owner of the request
        // if they match then grant permit else refuse
        // if authentication is not jwt token refuse
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

            // Extracting user ID parameter from the context
            Integer requestUserId = Integer.parseInt(context.getVariables().get("userId"));

            // Extracting user ID from the JWT token
            System.out.println("token object:" + jwtAuthenticationToken.getTokenAttributes().get("userId"));
            Integer jwtUserId = (Integer) jwtAuthenticationToken.getTokenAttributes().get("userId");


            // Compare user ID parameter and user ID in the JWT token
            return new AuthorizationDecision(hasUserId(jwtUserId,requestUserId));
        }
        // if no jwt token don't allow
        return new AuthorizationDecision(false); // Return denied if authorization fails
    }

}

