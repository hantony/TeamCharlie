package com.umgc.userAccess;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface UserAccessRepository extends JpaRepository<UserAccess, Long> {

   List<UserAccess> findByLocation(String locaion);

//   Custom query
//       @Query("SELECT b FROM Book b WHERE b.publishDate > :date")
//       List<User> findByPublishedDateAfter(@Param("date") LocalDate date);

}