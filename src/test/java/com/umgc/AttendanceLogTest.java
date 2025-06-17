package com.umgc;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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
public class AttendanceLogTest {

	@Autowired
	AttendanceLogRepository attendanceLogRepository;

	// static, all tests share this mysql container
	@Container
	@ServiceConnection
	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0-debian");

	@BeforeEach
	void testSetUp() {
	}
	
	@Test
	public void testDeleteById() {

		// Create a new Log Entry
		Date date = new Date();
		Long userId6 = Long.valueOf(6);
		AttendanceLog newLog = new AttendanceLog(userId6, date.getTime(), "entryType6", "location6");
		
		attendanceLogRepository.save(newLog);

		// find Log Entry By Id
		Long newLogId = newLog.getId();
		Optional<AttendanceLog> result = attendanceLogRepository.findById(newLogId);
        assertTrue(!result.isEmpty());
		
        // Delete Log Entry
		attendanceLogRepository.deleteById(newLogId);
		
        result = attendanceLogRepository.findById(newLogId);
        assertTrue(result.isEmpty());
	}

}