package com.umgc.terminal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface TerminalRepository extends JpaRepository<Terminal, Long> {

   List<Terminal> findByLocation(String location);

//   Custom query
//       @Query("SELECT b FROM Book b WHERE b.publishDate > :date")
//       List<User> findByPublishedDateAfter(@Param("date") LocalDate date);

}