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
                        name = "anon",
                        email = "mybeautifulemail@nicemail.com",
                        url = "mybeautifulwebsite.com"
                ),
                description =  "A basic shop rest api built with springboot. \n " +
                        "The goal of this application was to implement the basics of springboot. \n " +

                        "The source code can be found on github, where a CI/CD pipeline was set, in order to" +
                        "set mandatory code reviews with approval, application building and tests execution" +
                        "on all branches before merging them into the main dev branch." +
                        "This is done to avoid pushing broken code to the main repository" +
                        "For now there isn't a deployment of of the app from the github pipeline " +
                        "because I haven't found a free platform for app deployment that doesn't require a credit card." +

                        "The projects includes all basics elements of springboot such as Controllers, DTOS," +
                        "converters, services, models with embedded ids (composite keys for database)," +
                        "repositories exception handling and SpringbootSecurity." +

                        "The access to resources is based on ownership meaning that only the users that " +
                        "own a resource can access to it. Admins can access to all resources." +
                        "Classes and Interfaces have been implemented using lombok to avoid a lot" +
                        "of boilerplate code." +

                        "This is a stateless session application that uses JWT tokens for authentication and authorization" +
                        "Technologies and implementations : \n " +

                        "The Database is a dockerized instance of Postgress SQL, the tables and relationship of the entities have been created " +
                        "using Hibernate. The relationships are bidirectional and some weak entities use composite keys. " +
                        "The Entity relation diagram can be found in the git hub repository. " +


                        "Unit testing is made using a local h2 database, and Mockito for faster test execution." +

                        "Integration testing is made using test containers, this allows to create a container running" +
                        "a fresh instance of the PostgreSQL database for each integration test class ensuring a clean" +
                        "start for every class and the simulation of an environment which is very close to the real " +
                        "deployment case. At the start of every instance of the database container a script automatically " +
                        "populates it with some data. The api calls are made using MockMvc and JWT token authentication" +

                        "The documentation is made with Swagger (OpenAPI). It includes everything you need to use this API," +
                        "you must however create a user with role admin, login and set the jwt token in the authorization section in order" +
                        "to be able to test all the apiCalls. The documentation can be accessd at " +
                        "http://localhost:yourPortNumber/swagger-ui/index.html#/" +

                        "The whole application has been dockerized, you can either run it from intellij or with" +
                        "the command docker compose up to launch the containers for the application and database," +
                        "every time you launch the application the database will be empty, to change this modify the" +
                        "ddl-auto: create-drop option in the application.yml file",

                title = "Basic Shop Demo Application",
                version = "alpha 0.0.1",
                license = @License(
                        name = "Licence name",
                        url = "https://url.com"
                ),
                termsOfService = "My terms of service"
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
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
public class OpenAPIConfig {
}
