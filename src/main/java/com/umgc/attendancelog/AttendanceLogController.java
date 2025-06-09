package com.umgc.attendancelog;

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
@RequestMapping("/Log")
public class AttendanceLogController {

    @Autowired
    private AttendanceLogService attendanceLogService;

    @GetMapping
    public List<AttendanceLog> findAll() {
        return attendanceLogService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<AttendanceLog> findById(@PathVariable Long id) {
        return attendanceLogService.findById(id);
    }

    // create a log entry
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping
    public AttendanceLog create(@RequestBody AttendanceLog log) {
        return attendanceLogService.save(log);
    }

    // update a log entry
    @PutMapping
    public AttendanceLog update(@RequestBody AttendanceLog log) {
        return attendanceLogService.save(log);
    }

    // delete a log entry
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        attendanceLogService.deleteById(id);
    }


}

