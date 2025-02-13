package com.example.tut.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.tut.model.Clk;
import com.example.tut.repository.CklRepository;

@RestController
@RequestMapping("/api/auth")
public class ClkController {
    
    @Autowired
    CklRepository cklRepository;

    @PostMapping("/compute")
    public ResponseEntity<Clk> compute(@RequestBody Clk clk){
        clk.setTotal(((clk.getPhy()+clk.getChem())/2)+clk.getMaths());
        Clk saved = cklRepository.save(clk);
        return ResponseEntity.ok(saved);
    }
}
