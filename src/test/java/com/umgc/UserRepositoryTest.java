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

import com.umgc.user.User;
import com.umgc.user.UserRepository;

@DataJpaTest
// do not replace the testcontainer data source
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0-debian");
    
    @Test
    public void testUserSaveAndFindById() {

        // Create a new user
    	User newUserAAA = new User("AAA", "RoleA", "CardA", "StatusA");
    	
        
        // save user
        userRepository.save(newUserAAA);

        // find user
        Optional<User> result = userRepository.findById(newUserAAA.getId());
        assertTrue(result.isPresent());

        User userFromGet = result.get();

        assertEquals("AAA", userFromGet.getName());
        

    }

    
    @Test
    public void testUserCRUD() {
    	
    	User newUserAAA = new User("AAA", "RoleA", "CardA", "StatusA");
       
        // save user
        userRepository.save(newUserAAA);

        // find user by id
        Optional<User> result = userRepository.findById(newUserAAA.getId());
        assertTrue(result.isPresent());

        User userFromGet = result.get();

        Long userId = userFromGet.getId();

        assertEquals("AAA", userFromGet.getName());
        
        // update user
        userFromGet.setName("newNameA");
        userRepository.save(userFromGet);

        // find user by id
        Optional<User> result2 = userRepository.findById(userId);
        assertTrue(result2.isPresent());

        User user2 = result2.get();

        assertEquals("newNameA", user2.getName());
       

        // delete a user
        userRepository.delete(user2);

        // should be empty
        assertTrue(userRepository.findById(userId).isEmpty());

    }


}