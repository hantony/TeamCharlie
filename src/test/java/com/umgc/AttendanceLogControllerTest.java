package com.umgc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
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

/**
 * Testing with TestRestTemplate and @Testcontainers (image mysql:8.0-debian)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

// activate automatic startup and stop of containers
@Testcontainers
// JPA drop and create table, good for testing

@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
public class AttendanceLogControllerTest {

	@LocalServerPort
	private Integer port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String BASEURI;

	@Autowired
	AttendanceLogRepository attendanceLogRepository;

	// static, all tests share this mysql container
	@Container
	@ServiceConnection
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0-debian");

	@BeforeEach
	void testSetUp() {

		BASEURI = "http://localhost:" + port;

		attendanceLogRepository.deleteAll();
		
		Date date = new Date();
		
		Long userId1 = Long.valueOf(1);
		Long userId2 = Long.valueOf(2);
		Long userId3 = Long.valueOf(3);
		Long userId4 = Long.valueOf(4);
		
		Long terminalId1 = Long.valueOf(1);
		Long terminalId2 = Long.valueOf(2);
		Long terminalId3 = Long.valueOf(3);
		Long terminalId4 = Long.valueOf(4);
		
		AttendanceLog newLog1 = new AttendanceLog(userId1, terminalId1, date.getTime(), "entryTypeA", "locationA");
		AttendanceLog newLog2 = new AttendanceLog(userId2, terminalId2, date.getTime()+1, "entryTypeB", "locationB");
		AttendanceLog newLog3 = new AttendanceLog(userId3, terminalId3, date.getTime()+2, "entryTypeC", "locationC");
		AttendanceLog newLog4 = new AttendanceLog(userId4, terminalId4, date.getTime()+3, "entryTypeD", "locationD");
		
		attendanceLogRepository.saveAll(List.of(newLog1, newLog2, newLog3, newLog4));
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

		// Create a new Log Entry
		Date date = new Date();
		Long userId5 = Long.valueOf(5);
		Long terminalId5 = Long.valueOf(5);
		
		AttendanceLog newLog = new AttendanceLog(userId5, terminalId5, date.getTime(), "entryType5", "location5");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<AttendanceLog> request = new HttpEntity<>(newLog, headers);

		// test POST save
		ResponseEntity<AttendanceLog> responseEntity = restTemplate.postForEntity(BASEURI + "/Log", request, AttendanceLog.class);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

		// find 
		List<AttendanceLog> list = attendanceLogRepository.findByUserId(userId5);

		// Test User EEE details
		AttendanceLog alog = list.get(0);
		assertEquals(5L, alog.getUserId());
		assertTrue(alog.getEntryTime() > 1000 );
		assertEquals("entryType5", alog.getEntryType());
		assertEquals("location5", alog.getLocation());

	}

}