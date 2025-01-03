package com.example.tut.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tut.model.User;
import com.example.tut.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/signup")
    public User addData(@RequestBody User user) {
        return userRepository.save(user);
    }


    @PostMapping("/signin")
public ResponseEntity<String> verifyUser(@RequestBody User user) {
    User existingUser = userRepository.findById(user.getEmail()).orElse(null);

    if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
        return ResponseEntity.ok("Login successful!");
    } else {
        return ResponseEntity.ok("Invalid email or password!");
    }
}


}
