package com.umgc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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

import com.umgc.attendancelog.AttendanceLog;
import com.umgc.attendancelog.AttendanceLogRepository;
import com.umgc.user.User;

/**
 * Testing with TestRestTemplate and @Testcontainers (image mysql:8.0-debian)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

// activate automatic startup and stop of containers
@Testcontainers
// JPA drop and create table, good for testing

@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
public class AttendanceLogTest {

	@LocalServerPort
	private Integer port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String BASEURI;

	@Autowired
	AttendanceLogRepository attendanceLogRepository;

	// static, all tests share this postgres container
	@Container
	@ServiceConnection
	static MySQLContainer<?> postgres = new MySQLContainer<>("mysql:8.0-debian");

	@BeforeEach
	void testSetUp() {

		BASEURI = "http://localhost:" + port;

		attendanceLogRepository.deleteAll();
		
	//	public AttendanceLog(Long id, String userId, String entryTime, String entryType, String location) {

		AttendanceLog newLogAAA = new AttendanceLog("userIdA", "entryTimeA", "entryTypeA", "locationA");
		AttendanceLog newLogBBB = new AttendanceLog("userIdB", "entryTimeB", "entryTypeB", "locationB");
		AttendanceLog newLogCCC = new AttendanceLog("userIdC", "entryTimeC", "entryTypeC", "locationC");
		AttendanceLog newLogDDD = new AttendanceLog("userIdD", "entryTimeD", "entryTypeD", "locationD");
		
		attendanceLogRepository.saveAll(List.of(newLogAAA, newLogBBB, newLogCCC, newLogDDD));
	}

	@Test
	void testFindAll() {

		// find all Log Entries and return List<AttendanceLog>
		ParameterizedTypeReference<List<AttendanceLog>> typeRef = new ParameterizedTypeReference<>() {
		};
		ResponseEntity<List<AttendanceLog>> response = restTemplate.exchange(BASEURI + "/Log", HttpMethod.GET, null, typeRef);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(4, response.getBody().size());

	}

	@Test
	public void testCreate() {

		// Create a new Log Entry EEE
		AttendanceLog newLogEEE = new AttendanceLog("userIdE", "entryTimeE", "entryTypeE", "locationE");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<AttendanceLog> request = new HttpEntity<>(newLogEEE, headers);

		// test POST save
		ResponseEntity<AttendanceLog> responseEntity = restTemplate.postForEntity(BASEURI + "/Log", request, AttendanceLog.class);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

		// find User EEE
		List<AttendanceLog> list = attendanceLogRepository.findByUserId("userIdE");

		// Test User EEE details
		AttendanceLog log = list.get(0);
		assertEquals("userIdE", log.getUserId());
		assertEquals("entryTimeE", log.getEntryTime());
		assertEquals("entryTypeE", log.getEntryType());
		assertEquals("locationE", log.getLocation());

	}

}