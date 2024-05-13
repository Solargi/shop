package com.example.shop.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class SecurityConfiguration {
    @Value("${api.endpoint.base-url}")
    String baseUrl;

    //jwt public and private key
    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;
    private final UserSecurity userSecurity;
    private final OrderSecurity orderSecurity;
    private final AddressSecurity addressSecurity;
    private final AuthenticationEntryPointNoPopUp authenticationEntryPointNoPopUp;

    public SecurityConfiguration (UserSecurity userSecurity, OrderSecurity orderSecurity, AddressSecurity addressSecurity, AuthenticationEntryPointNoPopUp authenticationEntryPointNoPopUp) throws NoSuchAlgorithmException {
        this.userSecurity = userSecurity;
        this.orderSecurity = orderSecurity;
        this.addressSecurity = addressSecurity;
        this.authenticationEntryPointNoPopUp= authenticationEntryPointNoPopUp;
        //generate public and private key pair for jwt using RSA algorithm
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //set generated key size to 2048 bits
        keyPairGenerator.initialize(2048);
        //generate the pairs
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //set key pairs (since the returned type is key we need to downcast it to
        // our specific type of key which is RSA)
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //overwrite default authentication
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers("/api/v1/auth/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "configuration/ui",
                                        "configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui.html/**",
                                        "/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, this.baseUrl + "/users").permitAll()
                                .requestMatchers(HttpMethod.POST, this.baseUrl + "/users/login").permitAll()
                                .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/login").permitAll()
                                .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/login").permitAll()
                                .requestMatchers(HttpMethod.GET, this.baseUrl + "/users").hasAuthority("ROLE_admin")
                                .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/{userId}")
                                .access(this.userSecurity)
                                .requestMatchers(HttpMethod.PUT, this.baseUrl + "/users/{userId}")
                                .access(this.userSecurity)
                                .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/users/{userId}**")
                                .access(this.userSecurity)

                                .requestMatchers(HttpMethod.GET, this.baseUrl + "/addresses").hasAuthority("ROLE_admin")
                                .requestMatchers(HttpMethod.GET, this.baseUrl + "/addresses/{addressId}")
                                .access(this.addressSecurity)
                                .requestMatchers(HttpMethod.POST, this.baseUrl + "/addresses/{userId}")
                                .access(this.userSecurity)
//                      //let only admin modify or delete orderItem for now
                                .requestMatchers(HttpMethod.PUT, this.baseUrl + "/addresses/{addressId}**")
                                .access(this.addressSecurity)
                                .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/addresses/{addressId}**")
                                .access(this.addressSecurity)

                                .requestMatchers(HttpMethod.GET, this.baseUrl + "/items").permitAll()
                                .requestMatchers(HttpMethod.GET, this.baseUrl + "/items/{itemId}").permitAll()
                                .requestMatchers(HttpMethod.POST, this.baseUrl + "/items").hasAuthority("ROLE_admin")
                                .requestMatchers(HttpMethod.PUT, this.baseUrl + "/items/{itemId}").hasAuthority("ROLE_admin")
                                .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/items/{itemId}").hasAuthority("ROLE_admin")

                                .requestMatchers(HttpMethod.GET, this.baseUrl + "/cartItems").hasAuthority("ROLE_admin")
                                .requestMatchers(HttpMethod.GET, this.baseUrl + "/cartItems/{userId}/{itemId}")
                                .access(this.userSecurity)
                                .requestMatchers(HttpMethod.POST, this.baseUrl + "/cartItems/{userId}/{itemID}")
                                .access(this.userSecurity)
                                .requestMatchers(HttpMethod.PUT, this.baseUrl + "/cartItems//{userId}/{itemId}").access(this.userSecurity)
                                .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/cartItems//{userId}/{itemId}").access(this.userSecurity)

                                .requestMatchers(HttpMethod.GET, this.baseUrl + "/orders").hasAuthority("ROLE_admin")
                                .requestMatchers(HttpMethod.GET, this.baseUrl + "**/orders/{orderId}/**")
                                .access(this.orderSecurity)
                                .requestMatchers(HttpMethod.POST, this.baseUrl + "/orders/{userId}/**")
                                .access(this.userSecurity)
//                        //let only admin modify or delete order for now
                                .requestMatchers(HttpMethod.PUT, this.baseUrl + "**/orders/{orderId}/**").hasAuthority("ROLE_admin")
                                .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/orders/{orderId}").hasAuthority("ROLE_admin")

                                .requestMatchers(HttpMethod.GET, this.baseUrl + "/orderItems").hasAuthority("ROLE_admin")
                                .requestMatchers(HttpMethod.GET, this.baseUrl + "**/orderItems/{orderId}/{itemId}**")
                                .access(this.orderSecurity)
                                //let only admin add modify or delete orderItem for now
                                .requestMatchers(HttpMethod.POST, this.baseUrl + "/orderItems/{orderId}/{itemId}**").hasAuthority("ROLE_admin")
                                .requestMatchers(HttpMethod.PUT, this.baseUrl + "**/orderItems/{orderId}/{itemId}**").hasAuthority("ROLE_admin")
                                .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/orderItems/{orderId}/{itemId}").hasAuthority("ROLE_admin")


                                //everything else you need to be authenticated
                                .anyRequest().authenticated()

                )
                //enable when deploying in prod
                .csrf(AbstractHttpConfigurer::disable)
                //enable cors
                .cors(Customizer.withDefaults())
                //let spring security know the client is using basic
                //authorization (user, password)
                //customize basic auth to remove popup, but still allows login
                .httpBasic(customizer->customizer.authenticationEntryPoint(authenticationEntryPointNoPopUp))
                //enable jwt verification/login for authorization and authentication
                //jwt will help verify the authenticity of the token
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                //turn off sessions for requests, we don't need it anymore

                // since we use jwt
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //redirict to default login form
//                .formLogin(Customizer.withDefaults())
                //add social login with oauth2
//                .oauth2Login(Customizer.withDefaults())
                .build();
    }

    //returns password encoder
    // with 2^12 hashing iterations
    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder(12);
    }

    //create JWT Encoder
    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSet);
    }
    //create jwt decoder
    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        //tell spring security that the authorities are in the custom defined claim authorities
        // in the token creation in jwtProvider class
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        //removes prefix SCOPE_ from authorities that would be a built-in prefix
        // without we would get SCOPE_ROLE_USER as an authority instead of ROLE_USER
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        // a new authentication converter that knows how to interpret the token
        // this converter will replace the standard one in spring security during authentication
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean BearerTokenResolver bearerTokenResolver(){
        return new CustomBearerTokenResolver();
    }

}
