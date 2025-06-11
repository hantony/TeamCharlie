package com.umgc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.umgc.user.User;
import com.umgc.user.UserRepository;

/**
 * Testing with TestRestTemplate and @Testcontainers (image mysql:8.0-debian)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

// activate automatic startup and stop of containers
@Testcontainers
// JPA drop and create table, good for testing

@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
public class UserControllerTest {

	@LocalServerPort
	private Integer port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String BASEURI;

	@Autowired
	UserRepository userRepository;

	// static, all tests share this mysql container
	@Container
	@ServiceConnection
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0-debian");

	@BeforeEach
	void testSetUp() {

		BASEURI = "http://localhost:" + port;

		userRepository.deleteAll();

		User newUserAAA = new User("AAA", "RoleA", "CardA", "StatusA");
		User newUserBBB = new User("BBB", "RoleB", "CardB", "StatusB");
		User newUserCCC = new User("CCC", "RoleC", "CardC", "StatusC");
		User newUserDDD = new User("DDD", "RoleD", "CardD", "StatusD");

		userRepository.saveAll(List.of(newUserAAA, newUserBBB, newUserCCC, newUserDDD));
	}

	@Test
	void testFindAll() {

		// find all Users and return List<User>
		ParameterizedTypeReference<List<User>> typeRef = new ParameterizedTypeReference<>() {
		};
		ResponseEntity<List<User>> response = restTemplate.exchange(BASEURI + "/Users", HttpMethod.GET, null, typeRef);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(4, response.getBody().size());

	}

	@Test
	public void testCreate() {

		// Create a new User EEE

		User newUser = new User("EEE", "RoleE", "CardE", "StatusE");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<User> request = new HttpEntity<>(newUser, headers);

		// test POST save
		ResponseEntity<User> responseEntity = restTemplate.postForEntity(BASEURI + "/Users", request, User.class);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

		// find User EEE
		List<User> list = userRepository.findByName("EEE");

		// Test User EEE details
		User user = list.get(0);
		assertEquals("EEE", user.getName());
		assertEquals("RoleE", user.getRole());
		assertEquals("CardE", user.getCardId());
		assertEquals("StatusE", user.getStatus());

	}
	
	@Test
	public void testDeleteById() {

		// Create a new User EEE

		User newUser = new User("EEE", "RoleE", "CardE", "StatusE");
		
		userRepository.save(newUser);

		// find User EEE
		Long newUserId = newUser.getId();
		Optional<User> result = userRepository.findById(newUserId);
        assertTrue(!result.isEmpty());
		
        // Delete User EEE
		userRepository.deleteById(newUserId);
		
        result = userRepository.findById(newUserId);
        assertTrue(result.isEmpty());
	}
	
}