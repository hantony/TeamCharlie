package com.umgc.attendancelog;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceLogService {

    @Autowired
    private AttendanceLogRepository userRepository;

    public List<AttendanceLog> findAll() {
        return userRepository.findAll();
    }

    public Optional<AttendanceLog> findById(Long id) {
        return userRepository.findById(id);
    }

    public AttendanceLog save(AttendanceLog user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

   
}
