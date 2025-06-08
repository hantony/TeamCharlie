package com.umgc.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface UserRepository extends JpaRepository<User, Long> {

 //   List<User> findByTitle(String title);

    // Custom query
//    @Query("SELECT b FROM Book b WHERE b.publishDate > :date")
 //   List<User> findByPublishedDateAfter(@Param("date") LocalDate date);

}