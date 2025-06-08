package com.umgc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.umgc.user.User;
import com.umgc.user.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing with TestRestTemplate and @Testcontainers (image mysql:8.0-debian)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// activate automatic startup and stop of containers
@Testcontainers
// JPA drop and create table, good for testing
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class UserControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String BASEURI;

    @Autowired
    UserRepository UserRepository;

    // static, all tests share this postgres container
    @Container
    @ServiceConnection
    static MySQLContainer<?> postgres = new MySQLContainer<>(
            "mysql:8.0-debian"
    );

    @BeforeEach
    void testSetUp() {

        BASEURI = "http://localhost:" + port;

        UserRepository.deleteAll();

//        User b1 = new User("User A",
//                BigDecimal.valueOf(9.99),
//                LocalDate.of(2023, 8, 31));
//        User b2 = new User("User B",
//                BigDecimal.valueOf(19.99),
//                LocalDate.of(2023, 7, 31));
//        User b3 = new User("User C",
//                BigDecimal.valueOf(29.99),
//                LocalDate.of(2023, 6, 10));
//        User b4 = new User("User D",
//                BigDecimal.valueOf(39.99),
//                LocalDate.of(2023, 5, 5));

//        UserRepository.saveAll(List.of(b1, b2, b3, b4));
    }

//   @Test
    void testFindAll() {

        // ResponseEntity<List> response = restTemplate.getForEntity(BASEURI + "/Users", List.class);

        // find all Users and return List<User>
        ParameterizedTypeReference<List<User>> typeRef = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<User>> response = restTemplate.exchange(
                BASEURI + "/Users",
                HttpMethod.GET,
                null,
                typeRef
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4, response.getBody().size());

    }



    @Test
    public void testCreate() {

        // Create a new User E
    	
   // 	public User(Long id, String name, String role, String cardId, String status) {
        User newUser = new User("AAA", "RoleA", "CardA", "StatusA");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<User> request = new HttpEntity<>(newUser, headers);

        // test POST save
        ResponseEntity<User> responseEntity =
                restTemplate.postForEntity(BASEURI + "/Users", request, User.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        // find User E
    //    List<User> list = UserRepository.findByTitle("User E");

        // Test User E details
  //      User User = list.get(0);
  //      assertEquals("AAA", User.getName());
        
   //     assertEquals(BigDecimal.valueOf(9.99), User.getPrice());
   //     assertEquals(LocalDate.of(2023, 9, 14), User.getPublishDate());

    }
}