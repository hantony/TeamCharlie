package com.umgc.attendancelog;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umgc.terminal.Terminal;

// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {

   List<AttendanceLog> findByUserId(Long userId);

Optional<AttendanceLog> findByLocation(String location);

//   Custom query
//       @Query("SELECT b FROM Book b WHERE b.publishDate > :date")
//       List<User> findByPublishedDateAfter(@Param("date") LocalDate date);

}