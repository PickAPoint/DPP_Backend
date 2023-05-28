package com.example.dpp_backend.controller;

import com.example.dpp_backend.model.Package;
import com.example.dpp_backend.model.UpdatePackageDTO;
import com.example.dpp_backend.model.User;
import com.example.dpp_backend.repository.PackageRepository;
import com.example.dpp_backend.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.*;

// integration tests with RestAssured and hamcrest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;

    private User user1;

    private Package pkg1;
    private Package pkg2;
    private Package pkg3;

    @BeforeEach
    void setUp() {

        RestAssured.port = port;

        user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@test.com");
        user1.setPassword("test");
        user1.setType("Pending");

        pkg1 = new Package();
        pkg1.setEStore("PrintPlate");

        pkg2 = new Package();
        pkg2.setEStore("PrintPlate2");

        pkg3 = new Package();
        pkg3.setEStore("PrintPlate3");
    }


    @DisplayName("Get all users")
    @Test
    void testGetAllUsers() {

        userRepository.flush();
        userRepository.deleteAll();
        userRepository.save(user1);

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/admin/users")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].email", equalTo("user1@test.com"));
    }

    @DisplayName("Validate user (valid)")
    @Test
    void testValidateUserValid() {

        User user2 = new User();
        user2.setId(2);
        user2.setType("Pending");
        userRepository.save(user2);

        RestAssured.given()
                .contentType("application/json")
                .when()
                .post("/admin/validate/2")
                .then()
                .statusCode(200);
    }

    @DisplayName("Delete user (valid)")
    @Test
    void testDeleteUserValid() {

        User user2 = new User();
        user2.setId(2);
        user2.setType("Pending");
        userRepository.save(user2);

        RestAssured.given()
                .contentType("application/json")
                .when()
                .delete("/admin/users/2")
                .then()
                .statusCode(200);
    }

    @DisplayName("Get all packages")
    @Test
    void testGetAllPackages() {
            
            packageRepository.flush();
            packageRepository.deleteAll();
            packageRepository.save(pkg1);
            packageRepository.save(pkg2);
            packageRepository.save(pkg3);
    
            RestAssured.given()
                    .contentType("application/json")
                    .when()
                    .get("/admin/packages")
                    .then()
                    .statusCode(200)
                    .body("$", hasSize(3))
                    .body("[0].estore", equalTo("PrintPlate"))
                    .body("[1].estore", equalTo("PrintPlate2"))
                    .body("[2].estore", equalTo("PrintPlate3"));
    }

    @DisplayName("Get package by id (valid id)")
    @Test
    void testGetPackageByValidId() {

        packageRepository.flush();
        packageRepository.deleteAll();
        Package p = packageRepository.save(pkg1);

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/admin/package/" + p.getId())
                .then()
                .statusCode(200)
                .body("estore", equalTo("PrintPlate"));
    }

    @DisplayName("Get package by id (invalid id)")
    @Test
    void testGetPackageByInvalidId() {

        packageRepository.flush();
        packageRepository.deleteAll();
        Package p = packageRepository.save(pkg1);

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/admin/package/" + (p.getId() + 1))
                .then()
                .statusCode(404);
    }

    @DisplayName("Update package (valid)")
    @Test
    void testUpdatePackageValid() {

        packageRepository.flush();
        packageRepository.deleteAll();
        Package p = packageRepository.save(pkg1);

        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(p.getId());
        updatePackageDTO.setNewState("InTransit");

        RestAssured.given()
                .contentType("application/json")
                .body(updatePackageDTO)
                .when()
                .put("/admin/package")
                .then()
                .statusCode(200)
                .body(equalTo("true"));
    }


    @DisplayName("Update package (invalid)")
    @Test
    void testUpdatePackageInvalid() {

        packageRepository.flush();
        packageRepository.deleteAll();
        packageRepository.save(pkg1);

        UpdatePackageDTO updatePackageDTO = new UpdatePackageDTO();
        updatePackageDTO.setPackageId(-1);
        updatePackageDTO.setNewState("Invalid");

        RestAssured.given()
                .contentType("application/json")
                .body(updatePackageDTO)
                .when()
                .put("/admin/package")
                .then()
                .statusCode(200)
                .body(equalTo("false"));
    }


}
