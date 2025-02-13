package com.example.tut.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.tut.model.College;

//import com.example.tut.model.User;
import com.example.tut.repository.CollegeRepository;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class CollegeController {

    @Autowired
    CollegeRepository collegeRepository;


    @GetMapping("/collegecodes")
public ResponseEntity<?> getCollegeCodes() {
    try {
        List<College> colleges = collegeRepository.findAll();

        if (colleges.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No colleges found.");
        }

        return ResponseEntity.ok(colleges);
    } catch (Exception e) {
        e.printStackTrace(); // Log the error for debugging
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch college codes.");
    }
}


    @GetMapping("/departments")
    public ResponseEntity<?> getDepartments(@RequestParam String collegeCode) {
        Optional<College> optionalCollege = collegeRepository.findById(collegeCode);

        if (optionalCollege.isPresent()) {
            College college = optionalCollege.get();
            return ResponseEntity.ok(college.getArr()); // Return the department map
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("College not found.");
        }
    }

    // Update reservation data for a specific college and department
    @PutMapping("/reservation-data")
    public ResponseEntity<?> updateReservationData(
            @RequestParam String collegeCode,
            @RequestParam String department,
            @RequestBody Map<String, Integer> updatedData) {

        College college = collegeRepository.findById(collegeCode).orElse(null);

        
            
        Map<String, Integer> departmentCommunity = college.getCommunity().getOrDefault(department, new HashMap<>());

        // Update the community counts
        updatedData.forEach(departmentCommunity::put);

        // Save the updated map back to the community field
        college.getCommunity().put(department, departmentCommunity);
        collegeRepository.save(college);

        // Return a JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Reservation data updated successfully");
        return ResponseEntity.ok(response);
        
    }


   

@GetMapping("/colleges-by-community")
public List<Map<String, Object>> getCollegesByCommunity(
        @RequestParam String[] dept,
        @RequestParam String community,
        @RequestParam Double cutoff) {

    List<College> colleges = collegeRepository.findAll();
    String[]  departments = {"CSE", "CSD", "ECE", "EEE", "EIE", "MECH", "IT", "CHEM", "MTS", "AIML", "AIDS", "AUTO", "CIVIL"};
    if(dept.length!=0){
        departments = dept;
    }
     
    List<Map<String, Object>> result = new ArrayList<>();

    for (String department : departments) {
        // Filter colleges based on department, community, and cutoff
        List<Map<String, Object>> departmentResult = colleges.stream()
                .filter(college -> college.getCommunity().containsKey(department) &&
                        college.getCommunity().get(department).containsKey(community) &&
                        college.getCommunity().get(department).get(community) <= cutoff)
                .sorted(Comparator.comparingInt(College::getNirf))
                .map(college -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("College Code", college.getCollegeCode());
                    map.put("College Name", college.getCollegeName());
                    map.put("Department", department);
                    map.put("Community Count", college.getCommunity().get(department).get(community));
                    map.put("NIRF Rank", college.getNirf());
                    map.put("Status", college.getStatus());
                    return map;
                })
                .collect(Collectors.toList());

        result.addAll(departmentResult);
    }

    return result;
}

   



    @PostMapping("/appendDept")
    public String appendDepartment(@RequestParam String CollegeCode, @RequestParam String DepartmentCode,@RequestParam Integer Intake) {
        // Use Optional to fetch the college
        College college = collegeRepository.findById(CollegeCode).orElse(null);

        

        // Get the College object
      

        // Add the department to the array
        college.getArr().put(DepartmentCode,Intake);

        // Save the updated College object
        collegeRepository.save(college);

        return "Department added successfully!";
    }


    @PatchMapping("/update")
public String updateCollege(@RequestBody College updates) {
    // Retrieve the college code
    String collegeCode = updates.getCollegeCode();
    if (collegeCode == null || collegeCode.isEmpty()) {
        return "College code is required!";
    }

    // Find the existing college document
    College existingCollege = collegeRepository.findById(collegeCode).orElse(null);
    if (existingCollege == null) {
        return "College not found!";
    }

    // Update fields only if they are not null
    if (updates.getHostel() != null) {
        existingCollege.setHostel(updates.getHostel());
    }
    if (updates.getLabs() != null) {
        existingCollege.setLabs(updates.getLabs());
    }
    if (updates.getInfrastructures() != null) {
        existingCollege.setInfrastructures(updates.getInfrastructures());
    }
    if (updates.getNba() != null) {
        existingCollege.setNba(updates.getNba());
    }
    if (updates.getStatus() != null) {
        existingCollege.setStatus(updates.getStatus());
    }
    
    if (updates.getNaac() != null) {
        existingCollege.setNaac(updates.getNaac());
    }
    if (updates.getNirf() != null) {
        existingCollege.setNirf(updates.getNirf());
    }
    if (updates.getLink() != null) {
        existingCollege.setLink(updates.getLink());
    }

    // Save updated document to the database
    collegeRepository.save(existingCollege);

    return "College information updated successfully!";
}



    @PostMapping("/colleges")
    public ResponseEntity<String> addCollege(@RequestBody College college){
       College flag = collegeRepository.save(college);
        if(flag != null){
            return ResponseEntity.ok("College is Added");
        }else{
            return ResponseEntity.ok("College is not Added");
        }
    }

    @GetMapping("/Colleges")
public ResponseEntity<List<College>> getColleges(@RequestParam Double Cutoff) {
    System.out.println("Cutoff: " + Cutoff);

    // Fetch all colleges from the database
    List<College> colleges = collegeRepository.findAll();

    // Filter colleges based on the OC condition
    List<College> filteredColleges = colleges.stream()
        .filter(college -> {
            // Check if any department has an OC count >= Cutoff
            return college.getCommunity().values().stream().anyMatch(departmentCommunity -> 
                departmentCommunity.get("OC") != null && departmentCommunity.get("OC") <= Cutoff
            );
        })
        .toList();

    // Return the filtered colleges
    return ResponseEntity.ok(filteredColleges);
}


    @GetMapping("/college")
    public ResponseEntity<College> DetailsOfCollege(@RequestParam String code){
        System.out.println(code);
        College college = collegeRepository.findById(code).orElse(null);
        return ResponseEntity.ok(college);
    }

    @PostMapping("/addstudent")
public ResponseEntity<String> AddingStudents(@RequestParam String code, @RequestParam String id) {
    Optional<College> optionalCollege = collegeRepository.findById(code);
    
    if (optionalCollege.isPresent()) {
        College clg = optionalCollege.get();
        
        // Ensure the students list is initialized
        if (clg.getStudents() == null) {
            clg.setStudents(new ArrayList<>());  // Initialize if null
        }
        
        if (!clg.getStudents().contains(id)) {
            clg.getStudents().add(id); // Add student ID directly to the list
            collegeRepository.save(clg); // Save updated college document
            return ResponseEntity.ok("Student added successfully");
        } else {
            return ResponseEntity.badRequest().body("Student already exists in this college");
        }
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("College not found");
    }
}




   /*  @PutMapping("/reservation-data")
public ResponseEntity<Map<String, String>> updateReservationData(
    @RequestParam String collegeCode, 
    @RequestParam String department, 
    @RequestBody Map<String, Integer> updatedData
) {
    College college = collegeRepository.findById(collegeCode).orElse(null);

    if (college != null) {
        // Retrieve or create the department's community map
        Map<String, Integer> departmentCommunity = college.getCommunity().getOrDefault(department, new HashMap<>());

        // Update the community counts
        updatedData.forEach(departmentCommunity::put);

        // Save the updated map back to the community field
        college.getCommunity().put(department, departmentCommunity);
        collegeRepository.save(college);

        // Return a JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Reservation data updated successfully");
        return ResponseEntity.ok(response);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "College not found"));
    }
}

*/
}
