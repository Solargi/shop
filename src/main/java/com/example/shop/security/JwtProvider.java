package com.example.shop.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
//creates jwt tokens
@AllArgsConstructor
public class JwtProvider {
    private final JwtEncoder jwtEncoder;
    public String createToken(Authentication authentication){
        //get current time
        Instant now = Instant.now();
        //token expires after 2 hours
        long expiresIn = 2;

        //get autorities in a space separated string
        String authorities = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(" "));

        //get user principal
        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

        //create jwt claims
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self") //means that we are not using a deticated authorization server, everything is done here
                .issuedAt(now)
                .expiresAt(now.plus(expiresIn, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("authorities", authorities)// custom defined claim
                .claim("userId", userPrincipal.getUser().getId()) //add id attribute
                .build();
        //encode the claims
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();

    }
}
