package com.umgc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
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
import com.umgc.terminal.Terminal;
import com.umgc.terminal.TerminalRepository;

@DataJpaTest
// do not replace the testcontainer data source
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class TerminalRepositoryTest {

    @Autowired
    private TerminalRepository terminalRepository;

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0-debian");
    @Test
    public void testTerminalSaveAndFindById() {

        // Create a new terminal
    	Terminal terminalA = new Terminal();
    	terminalA.setLocation("TerminalLocA");
        
        // save terminal
        terminalRepository.save(terminalA);

        // find terminal
        Optional<Terminal> result = terminalRepository.findById(terminalA.getId());
        assertTrue(result.isPresent());

        Terminal terminalAFromGet = result.get();

        assertEquals("TerminalLocA", terminalAFromGet.getLocation());
        

    }

    @Test
    public void testTerminalCRUD() {

    	Terminal terminalB = new Terminal();
    	terminalB.setLocation("locB");
       
        // save terminal
        terminalRepository.save(terminalB);

        // find terminal by location
        // find attendance log entry by id
        Optional<Terminal> result = terminalRepository.findById(terminalB.getId());
        assertTrue(result.isPresent());

        Terminal terminalBFromGet = result.get();

        Long terminalBId = terminalBFromGet.getId();

        assertEquals("locB", terminalBFromGet.getLocation());
        
        // update book
        terminalBFromGet.setLocation("locBBB");
        terminalRepository.save(terminalBFromGet);

        // find book by id
        Optional<Terminal> result2 = terminalRepository.findById(terminalBId);
        assertTrue(result2.isPresent());

        Terminal terminalBFromGet2 = result2.get();

        assertEquals("locBBB", terminalBFromGet2.getLocation());
       

        // delete a book
        terminalRepository.delete(terminalB);

        // should be empty
        assertTrue(terminalRepository.findById(terminalBId).isEmpty());

    }


}