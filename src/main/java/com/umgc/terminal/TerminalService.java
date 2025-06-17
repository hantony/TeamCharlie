package com.umgc.terminal;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalService {

    @Autowired
    private TerminalRepository terminalRepository;

    public List<Terminal> findAll() {
        return terminalRepository.findAll();
    }

    public Optional<Terminal> findById(Long id) {
        return terminalRepository.findById(id);
    }

    public Terminal save(Terminal terminal) {
        return terminalRepository.save(terminal);
    }

    public void deleteById(Long id) {
        terminalRepository.deleteById(id);
    }

   
}
