package com.example.shop.OpenAPI;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "name",
                        email = "email",
                        url = "url"
                ),
                description =  "app description",
                title = "title",
                version = "version",
                license = @License(
                        name = "Licence name",
                        url = "https://url.com"
                ),
                termsOfService = "terms of service"
        ),
        servers = {
                @Server(
                        description = "Local Env",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "prod env",
                        url = "http://myprodurl"
                )

        }
)
//use to define security requirements in the documentation
//used in tandem with @SecurityRequirement in controllers
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
        )
public class OpenAPIConfig {
}
