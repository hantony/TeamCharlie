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

import com.umgc.terminal.Terminal;
import com.umgc.terminal.TerminalRepository;
import com.umgc.user.User;

/**
 * Testing with TestRestTemplate and @Testcontainers (image mysql:8.0-debian)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

// activate automatic startup and stop of containers
@Testcontainers
// JPA drop and create table, good for testing

@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
public class TerminalControllerTest {

	@LocalServerPort
	private Integer port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String BASEURI;

	@Autowired
	TerminalRepository terminalRepository;

	// static, all tests share this mysql container
	@Container
	@ServiceConnection
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0-debian");

	@BeforeEach
	void testSetUp() {

		BASEURI = "http://localhost:" + port;

		terminalRepository.deleteAll();
		
		Terminal terminalA = new Terminal("locA");
		Terminal terminalB = new Terminal("locB");
		Terminal terminalC = new Terminal("locC");
		Terminal terminalD = new Terminal("locD");



		terminalRepository.saveAll(List.of(terminalA, terminalB, terminalC, terminalD));
	}

	@Test
	void testFindAll() {

		// find all Users and return List<User>
		ParameterizedTypeReference<List<User>> typeRef = new ParameterizedTypeReference<>() {
		};
		ResponseEntity<List<User>> response = restTemplate.exchange(BASEURI + "/Terminals", HttpMethod.GET, null, typeRef);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(4, response.getBody().size());

	}

	@Test
	public void testCreate() {

		// Create a new Terminal E

		Terminal terminalE = new Terminal("locE");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<Terminal> request = new HttpEntity<>(terminalE, headers);

		// test POST save
		ResponseEntity<Terminal> responseEntity = restTemplate.postForEntity(BASEURI + "/Terminals", request, Terminal.class);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

		// find User EEE
		List<Terminal> list = terminalRepository.findByLocation("locE");

		// Test Terminal E details
		Terminal myTerminalE = list.get(0);
		assertEquals("locE", myTerminalE.getLocation());
	}
	
	@Test
	public void testDeleteById() {

		// Create a new Terminal F

		Terminal newTermainF = new Terminal("locE" );
		
		terminalRepository.save(newTermainF);

		// find Terminal E
		Long newTerminalId = newTermainF.getId();
		Optional<Terminal> result = terminalRepository.findById(newTerminalId);
        assertTrue(!result.isEmpty());
		
        // Delete Terminal E
		terminalRepository.deleteById(newTermainF.getId());
		
        result = terminalRepository.findById(newTerminalId);
        assertTrue(result.isEmpty());
	}
	
}