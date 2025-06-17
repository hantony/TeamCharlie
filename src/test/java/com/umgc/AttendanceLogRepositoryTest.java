package com.umgc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.umgc.attendancelog.AttendanceLog;
import com.umgc.attendancelog.AttendanceLogRepository;

@DataJpaTest
// do not replace the testcontainer data source
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class AttendanceLogRepositoryTest {

    @Autowired
    private AttendanceLogRepository attendanceLogRepository;

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0-debian");
    @Test
    public void testAttendanceLogSaveAndFindById() {

        // Create a new attendance log entry
        AttendanceLog alog = new AttendanceLog();
        alog.setLocation("LocA");
        

        // save attendance log entry
        attendanceLogRepository.save(alog);

        // find attendance log entry by id
        Optional<AttendanceLog> result = attendanceLogRepository.findById(alog.getId());
        assertTrue(result.isPresent());

        AttendanceLog alogFromGet = result.get();

        assertEquals("LocA", alogFromGet.getLocation());
        

    }

    @Test
    public void testBookCRUD() {

    	AttendanceLog alogB = new AttendanceLog();
    	alogB.setLocation("locB");
       

        // save book
        attendanceLogRepository.save(alogB);

        // find book by isbn
        Optional<AttendanceLog> result = attendanceLogRepository.findByLocation(alogB.getLocation());
        assertTrue(result.isPresent());

        AttendanceLog aLogFromGet = result.get();

        Long aLogId = aLogFromGet.getId();

        assertEquals("locB", aLogFromGet.getLocation());
        
        // update book
        alogB.setLocation("locBBB");
        attendanceLogRepository.save(alogB);

        // find book by id
        Optional<AttendanceLog> result2 = attendanceLogRepository.findById(aLogId);
        assertTrue(result2.isPresent());

        AttendanceLog alogFromGet2 = result2.get();

        assertEquals("locBBB", alogFromGet2.getLocation());
       

        // delete a book
        attendanceLogRepository.delete(alogB);

        // should be empty
        assertTrue(attendanceLogRepository.findById(aLogId).isEmpty());

    }


}