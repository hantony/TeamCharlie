package com.umgc.userAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccessService {

    @Autowired
    private UserAccessRepository userAccessRepository;

    public List<UserAccess> findAll() {
        return userAccessRepository.findAll();
    }

    public Optional<UserAccess> findById(Long id) {
        return userAccessRepository.findById(id);
    }

    public UserAccess save(UserAccess userAccess) {
        return userAccessRepository.save(userAccess);
    }

    public void deleteById(Long id) {
        userAccessRepository.deleteById(id);
    }

   
}
