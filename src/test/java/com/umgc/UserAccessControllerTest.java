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
import com.umgc.userAccess.UserAccess;
import com.umgc.userAccess.UserAccessRepository;

/**
 * Testing with TestRestTemplate and @Testcontainers (image mysql:8.0-debian)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

// activate automatic startup and stop of containers
@Testcontainers
// JPA drop and create table, good for testing

@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
public class UserAccessControllerTest {

	@LocalServerPort
	private Integer port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String BASEURI;

	@Autowired
	UserAccessRepository userAccessRepository;

	// static, all tests share this mysql container
	@Container
	@ServiceConnection
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0-debian");

	@BeforeEach
	void testSetUp() {

		BASEURI = "http://localhost:" + port;

		userAccessRepository.deleteAll();

		UserAccess userAccessA = new UserAccess("locA");
		UserAccess userAccessB = new UserAccess("locB");
		UserAccess userAccessC = new UserAccess("locC");
		UserAccess userAccessD = new UserAccess("locD");
		

		userAccessRepository.saveAll(List.of(userAccessA, userAccessB, userAccessC, userAccessD));
	}

	@Test
	void testFindAll() {

		// find all User Access and return List<UserAccess>
		ParameterizedTypeReference<List<User>> typeRef = new ParameterizedTypeReference<>() {
		};
		ResponseEntity<List<User>> response = restTemplate.exchange(BASEURI + "/UserAccess", HttpMethod.GET, null, typeRef);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(4, response.getBody().size());

	}

	@Test
	public void testCreate() {

		// Create a new UserAccess E

		UserAccess userAccessE = new UserAccess("locE");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<UserAccess> request = new HttpEntity<>(userAccessE, headers);

		// test POST save
		ResponseEntity<UserAccess> responseEntity = restTemplate.postForEntity(BASEURI + "/UserAccess", request, UserAccess.class);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

		// find User EEE
		List<UserAccess> list = userAccessRepository.findByLocation("locE");

		// Test User EEE details
		UserAccess myUserAccess = list.get(0);
		assertEquals("locE", myUserAccess.getLocation());
		
	}
	
	@Test
	public void testDeleteById() {

		// Create a new User Access F

		UserAccess userAccessF = new UserAccess ("locF");
		
		userAccessRepository.save(userAccessF);

		// find UserAccess F
		Long userAccessFId = userAccessF.getId();
		Optional<UserAccess> result = userAccessRepository.findById(userAccessFId);
        assertTrue(!result.isEmpty());
		
        // Delete User EEE
		userAccessRepository.deleteById(userAccessFId);
		
        result = userAccessRepository.findById(userAccessFId);
        assertTrue(result.isEmpty());
	}
	
}