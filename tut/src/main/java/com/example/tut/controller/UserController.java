package com.example.tut.controller;


import java.io.IOException;

import java.util.List;
import java.util.Map;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.example.tut.model.User;
import com.example.tut.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/signup")
public ResponseEntity<?> addData(@RequestBody User user) {
    User existingUser = userRepository.findById(user.getEmail()).orElse(null);
    
    if (existingUser != null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
    }
    
    User savedUser = userRepository.save(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
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

@GetMapping("/user/data")
public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
    System.out.println("get data opened");
    
    Optional<User> user = userRepository.findById(email); // Use findByEmail
    
    if (user.isPresent()) {
        return ResponseEntity.ok(Map.of("user", user.get()));
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
    }
}



    

    @PostMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, Object> updates) {
        Optional<User> optionalUser = userRepository.findById((String) updates.get("email"));
        System.out.println("opened");
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
    
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        user.setName((String) value);
                        break;
                    case "dob":
                        user.setDob((String) value);
                        break;
                    case "address":
                        user.setAddress((String) value);
                        break;
                    case "gender":
                        user.setGender((String) value);
                        break;
                    case "group":
                        user.setGroup((String) value);
                        break;
                }
            });
            System.out.println("User stored");
            userRepository.save(user);
            return ResponseEntity.ok("User updated successfully");
        } else {
            System.out.println("User not stored");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
    
 @Autowired
    private GridFsTemplate gridFsTemplate;  // GridFS for file storage

    // Method to upload the PDF file for a user
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam String email, @RequestParam("file") MultipartFile file) {
        System.out.println("for file");
        try {
            // Store PDF in GridFS
            String fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType()).toString();

            // Update the user with the PDF fileId
            Optional<User> optionalUser = userRepository.findById(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPdfFileId(fileId);  // Store GridFS fileId in the user's record
                userRepository.save(user);  // Save updated user with PDF fileId
                return ResponseEntity.ok("PDF uploaded successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload PDF.");
        }
    }

    // Method to retrieve the PDF file
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadPdf(@RequestParam String email) throws IllegalStateException, IOException {
        Optional<User> optionalUser = userRepository.findById(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String fileId = user.getPdfFileId();  // Assuming you have a field to store the GridFS file ID.
    
            if (fileId != null) {
                // Retrieve the GridFS resource using the fileId
                GridFsResource resource = gridFsTemplate.getResource(fileId);
                
                return ResponseEntity.ok()
                        .header("Content-Type", "application/pdf")  // Set content type to PDF
                        .header("Content-Disposition", "inline; filename=\"" + resource.getFilename() + "\"")  // Display in iframe
                        .body(new InputStreamResource(resource.getInputStream()));  // Return the PDF file content
            } else {
                // Return an error if no PDF is found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else {
            // Return an error if user not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/studentsid")
public ResponseEntity<?> studentsid() {
    try {
        List<User> user = userRepository.findAll();

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No colleges found.");
        }

        return ResponseEntity.ok(user);
    } catch (Exception e) {
        e.printStackTrace(); // Log the error for debugging
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch college codes.");
    }
}

@PostMapping("/addFrd")
public ResponseEntity<String> addFrd(@RequestParam String email, @RequestParam String frd) {
    Optional<User> optionalUser = userRepository.findById(email);
    
    if (!optionalUser.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    
    User user = optionalUser.get();
    
    // Ensure that the friend is not already in the user's friend list
    if (user.getFriends().contains(frd)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Friend already added");
    }
    
    // Add friend
    user.getFriends().add(frd);
    
    // Save updated user back to the database
    userRepository.save(user);
    
    return ResponseEntity.ok("Friend added successfully");
}

    
}
