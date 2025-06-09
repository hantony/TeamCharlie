package com.umgc.attendancelog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {

   List<AttendanceLog> findByUserId(String name);

//   Custom query
//       @Query("SELECT b FROM Book b WHERE b.publishDate > :date")
//       List<User> findByPublishedDateAfter(@Param("date") LocalDate date);

}