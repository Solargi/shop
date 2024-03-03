# Shop Project Description
A basic shop rest api built with springboot.
The goal of this application was to implement the basics of springboot.

The source code can be found on github, where a CI/CD pipeline was set,
in order to set mandatory code reviews with approval, application building
and tests execution on all branches before merging them into the main dev branch.
This is done to avoid pushing broken code to the main repository For now there isn't a deployment of the
app from the github pipeline because I haven't found a free platform for app deployment
that doesn't require a credit card.

The project includes all basics elements of springboot such as Controllers, DTOS, converters, services,
models of entities both weak and strong having embedded ids (composite keys for database),
repositories, exception handling and SpringbootSecurity.

The access to resources is based on ownership
meaning that only the users that own a resource can access to it.
Admins can access to all resources.
Classes and Interfaces have been implemented using lombok to avoid a lot of boilerplate code.

This is a stateless session application that uses JWT tokens for authentication and authorization.

The Database is a dockerized instance of Postgress SQL,
the tables and relationship of the entities have been created using Hibernate.
The relationships are bidirectional and some weak entities use composite keys.
The Entity relation diagram can be found in the github repository.

Unit testing is made using a local h2 database, and Mockito for faster test execution.

Integration testing is made using test containers, this allows to create a container
running a fresh instance of the PostgreSQL database for each integration test class ensuring
a clean start for every class and the simulation of an environment which is very close to the real deployment case.
At the start of every instance of the database container a script automatically
populates it with some data. The api calls are made using MockMvc and JWT token authentication.

The documentation is made with Swagger (OpenAPI).
It includes everything you need to use this API, you must however create a user with role admin,
login and set the jwt token in the authorization section in order to be able to test all the apiCalls.
The documentation can be accessed at http://localhost:8080/swagger-ui/index.html

The whole application has been dockerized, you can either run it from intellij or
with the command docker compose up to launch the containers for the application and database.
Every time you launch the application the database data will be deleted,
to change this modify the ddl-auto: create-drop option in the application.yml file


# Technologies:
1. Springboot
2. SpringSecurity
3. Swagger Documentation (OpenAPI)
4. Lombok
5. Hibernate
6. PostgresSQL
7. H2 Database
7. GitHub CI/CD
8. JWT Token
9. Mockito
10. MockMvc
11. Test Containers
12. Maven

# How to run the project
To run the project you need to have docker and maven installed.
The following ports must be available: 8080,8090 and 5432

## Run the project with Intellij

1. Download the shop project:

    ```bash
    $ git clone https://github.com/Solargi/shop.git
    ```
2. open the project with intellij and run it, you can then access the documentation from:
   http://localhost:8080/swagger-ui/index.html

## Run the project from shell
1. Download the shop project:

    ```bash
    $ git clone https://github.com/Solargi/shop.git
    ```

1. Navigate to the shop folder

    ```bash
    $ cd yourpath/shop
    ```
1. Compile the project
2. ```bash
   $ mvn clean install
   ```
3. Run the docker container (it will use the ports 8090 and 5432):
   ```bash
    $ docker compose up
    ```
you can then access the documentation from:
http://localhost:8090/swagger-ui/index.html
4. To stop and remove containers, networks, volumes, and images use:
   ```bash
    $ docker compose down
   ```

# How to use Swagger documentation
## Documentation Access
The documentation is made with Swagger (OpenAPI).
It includes everything you need to use this API, you must however create a user with role admin,
login and set the jwt token in the authorization section in order to be able to test all the apiCalls.
The documentation can be accessed at http://localhost:8080/swagger-ui/index.html
## Documentation Base Workflow
if this is the first time here is how to use this documentation.

1. Start by creating an admin user using the default user-controller post request

2. use the authorize button on the top left to log in using username and password of the created user

3. make the default post request of the auth-controller to get the jwt token in the response body

4. copy the jwt token, click on the top right authorize button and paste the jwt token and click authorize to gain authorization to use all the other requests

5. make the following post requests (you can use the default requests):

    - address-controller post to add an address to the user,

    - item controller post to add an item,

    - cart-item post to add an item to the user's cart,

    - order-controller post to create an order for the current user, containing the cart items.

This is the basic workflow the rest is up to you. Good Luck!.