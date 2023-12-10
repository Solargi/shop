package com.example.shop.security;

import com.example.shop.models.Order;
import com.example.shop.repositories.OrderRepository;
import com.example.shop.system.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class OrderSecurity implements AuthorizationManager<RequestAuthorizationContext> {
    OrderRepository orderRepository;
    //returns true if authenticated user has role admin
    private boolean isAdmin(Authentication authentication) {
        //get list of granted authorities
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        //following line is only for debug:
//       authorities.stream().map(GrantedAuthority::getAuthority).forEach(thing->System.out.println("role:" + thing));
        //check autorities and returns true if at leas one matches "Role_admin"
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_admin"::equals);
    }
    //returns true if authenticated user is owner of order
    private boolean isUser(Authentication authentication, Integer orderOwnerId){
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            // Extracting user ID from the JWT token
            System.out.println("token object:" + jwtAuthenticationToken.getTokenAttributes().get("userId"));
            Integer jwtUserId = ((Long)jwtAuthenticationToken.getTokenAttributes().get("userId")).intValue();
            System.out.print("owener id: " + orderOwnerId);
            System.out.print("jwt id: " + jwtUserId);

            return jwtUserId.equals(orderOwnerId);}
        //if there is no jwt token return false
        return false;
    }

    //checks if the authenticated user is the owner of the order or is admin
//    alternatively this function can be implemented in the security package in a similar
//    fashion to userSecurity but it would require to load the order twice one here and one
//    during authorization in OrderSecurity class
    private boolean isOwnerOrAdmin(Authentication auth, Integer orderOwnerId){
        return (isAdmin(auth) || isUser(auth, orderOwnerId));
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        System.out.println("HELLO INSIDE ORDER SECURITYYYYYYYYYYYYY");
        //get authentication
        Authentication authentication = authenticationSupplier.get();
//        System.out.println("authentication: object " + authentication);
        //if user is admin then permission granted
        if(isAdmin(authentication)){
            return new AuthorizationDecision(true);
        }
        // get order id from context
        System.out.println("HELLO INSIDE ORDER IDDDDDDDDDDDDDDDDD: " + context.getVariables());
        Integer requestOrderId = Integer.parseInt(context.getVariables().get("orderId"));
        System.out.println("CONTEXT OBJECTTTTTTTTTT: " + context);

        //fetch the order to and get owner id
        Optional<Order> optOrder = this.orderRepository.findById(requestOrderId);
        if (optOrder.isPresent()){
            Integer orderOwnerId = optOrder.get().getUser().getId();
            return new AuthorizationDecision(isUser(authentication,orderOwnerId));
        }

        return new AuthorizationDecision(false);
    }
}
