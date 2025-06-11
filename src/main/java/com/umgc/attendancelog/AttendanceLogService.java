package com.umgc.attendancelog;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceLogService {

    @Autowired
    private AttendanceLogRepository attendanceLogRepository;

    public List<AttendanceLog> findAll() {
        return attendanceLogRepository.findAll();
    }

    public Optional<AttendanceLog> findById(Long id) {
        return attendanceLogRepository.findById(id);
    }

    public AttendanceLog save(AttendanceLog user) {
        return attendanceLogRepository.save(user);
    }

    public void deleteById(Long id) {
    	attendanceLogRepository.deleteById(id);
    }

   
}
