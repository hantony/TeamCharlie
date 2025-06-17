package com.umgc.userAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/UserAccess")
public class UserAccessController {

    @Autowired
    private UserAccessService userAccessService;

    @GetMapping
    public List<UserAccess> findAll() {
        return userAccessService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<UserAccess> findById(@PathVariable Long id) {
        return userAccessService.findById(id);
    }

    // create user access
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping
    public UserAccess create(@RequestBody UserAccess user) {
        return userAccessService.save(user);
    }

    // update user access
    @PutMapping
    public UserAccess update(@RequestBody UserAccess user) {
        return userAccessService.save(user);
    }

    // delete user access
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userAccessService.deleteById(id);
    }


}

