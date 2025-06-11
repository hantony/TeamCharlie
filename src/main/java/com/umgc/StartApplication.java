package com.umgc;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.umgc.attendancelog.AttendanceLog;
import com.umgc.attendancelog.AttendanceLogRepository;
import com.umgc.user.User;
import com.umgc.user.UserRepository;

@SpringBootApplication
public class StartApplication {

	private static final Logger log = LoggerFactory.getLogger(StartApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}

	// Spring runs CommandLineRunner bean when Spring Boot App starts

	@Bean
	public CommandLineRunner initialUsers(UserRepository userRepository) {
		return (args) -> {
			
			User u1 = new User("Alice Johnson", "Student", "CARD1001", "Status01");
			User u2 = new User("Bob Smith", "Professor", "CARD1002", "Status02");
			User u3 = new User("Charlie Brown", "Maintenance", "CARD1003", "Status03");
			User u4 = new User("Dana White", "Technical Support", "CARD1004", "Status04");
			
			// save a few users, ID auto increase, expect 1, 2, 3, 4
			userRepository.saveAll(List.of(u1, u2, u3, u4));
			
			// find all users
			log.info("-------------------------------");
			log.info("findAll(), expect 4 users");
			log.info("-------------------------------");
			for (User user : userRepository.findAll()) {
				log.info(user.toString());
			}
		};
	}
	
    @Bean
    public CommandLineRunner initialAttendanceLogEntries(AttendanceLogRepository attendanceLogRepository) {
        return (args) -> {
        	Date date = new Date();
        	Long userId1 = Long.valueOf(1);
        	Long userId2 = Long.valueOf(2);
        	Long userId3 = Long.valueOf(3);
        	Long userId4 = Long.valueOf(4);
        	
            AttendanceLog alog1 = new AttendanceLog(userId1, date.getTime(), "IN", "Main Gate");
            AttendanceLog alog2 = new AttendanceLog(userId1, date.getTime()+1, "IN", "Main Gate");
            AttendanceLog alog3 = new AttendanceLog(userId2, date.getTime()+2, "IN", "Admin Entrance");
            AttendanceLog alog4 = new AttendanceLog(userId3, date.getTime()+3, "IN", "Service Door");
            AttendanceLog alog5 = new AttendanceLog(userId4, date.getTime()+4, "IN", "Main Gate");
           
            // save a few users, ID auto increase, expect 1, 2, 3, 4
            attendanceLogRepository.saveAll(List.of(alog1, alog2, alog3, alog4, alog5));
            // find all log entries
            log.info("-------------------------------");
            log.info("findAll(), expect 5 log entries");
            log.info("-------------------------------");
            for (AttendanceLog alog : attendanceLogRepository.findAll()) {
                log.info(alog.toString());
            }
        };
    }

}