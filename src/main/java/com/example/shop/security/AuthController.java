package com.example.shop.security;

import com.example.shop.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
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

    @Operation(
            summary = "Log in to obtain JWT token",
            description = "in order to get the JWT you must authorize yourself with a valid username and password  in the swagger ui" +
                    "using the authorize lock before sending the request otherwise it won't work. " +
                    "This token can be used to authorize yourself using the toke option in the swagger ui." +
                    "YOU HAVE TO POST A USER BEFORE TRYING TO LOGIN",

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "successfull login",
                                                    value = "{\n" +
                                                            "  \"userInfo\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"username\": \"u1\",\n" +
                                                            "    \"name\": \"a\",\n" +
                                                            "    \"surname\": \"b\",\n" +
                                                            "    \"email\": \"q@q.com\",\n" +
                                                            "    \"birthDate\": \"3.1.1991\",\n" +
                                                            "    \"roles\": \"admin\"\n" +
                                                            "  },\n" +
                                                            "  \"token\": \"eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoidTEiLCJleHAiOjE3MDY5NjU2NTIsImlhdCI6MTcwNjk1ODQ1MiwidXNlcklkIjoxLCJhdXRob3JpdGllcyI6IlJPTEVfYWRtaW4ifQ.Nz6htJiFEFizXhixCfgNWlq1sabiZr5PWmj-CZJ_szuWJClsX6VZZ2jtaP_ToGlXkMwjDV9GNawAcDDUOXsSU9Shesa-r6ebTXgrc4mxF_EfmvMqKTM6e0Sr_JYU6D2J8I3Q5Lv9ciPYL2NWd5OKQNZxBa6UYy7t3ufChLpvmvIEYg25LaJLsBGKIXC19kOiqHbmcciNvQQjFTJeLMLIdyQxm0ryVUa7__r81OeAaaewWgPu0szKTQ-XR53AddfaUurSjHk8pBaSfRu69h9n9Q6zvkyRNrXMpkUwr9HO139nrnwBgCyY8yMEWrhAdnvw7abR9iquRylKYb8MJfpPWA\"\n" +
                                                            "}"
                                            )

                                    }
                            )
                    )
            }
    )
    @SecurityRequirement(name = "basicAuth")
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

//    just for test purposes (retreives userID):
//    @Profile({"!prod"})
    @GetMapping("/info")
    public Object getInfo(Authentication auth, JwtAuthenticationToken principal, @RequestHeader(name="Authorization") String token) {

        return principal.getTokenAttributes().get("userId");

    }
}
