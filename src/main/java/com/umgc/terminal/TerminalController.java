package com.umgc.terminal;

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
@RequestMapping("/Terminals")
public class TerminalController {

    @Autowired
    private TerminalService terminalService;

    @GetMapping
    public List<Terminal> findAll() {
        return terminalService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Terminal> findById(@PathVariable Long id) {
        return terminalService.findById(id);
    }

    // create a terminal
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping
    public Terminal create(@RequestBody Terminal terminal) {
        return terminalService.save(terminal);
    }

    // update a terminal
    @PutMapping
    public Terminal update(@RequestBody Terminal terminal) {
        return terminalService.save(terminal);
    }

    // delete a terminal
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
    	terminalService.deleteById(id);
    }


}

