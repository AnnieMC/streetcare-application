package app_reporting_api.controller;

import app_reporting_api.dto.UserDTO;
import app_reporting_api.model.UserModel;
import app_reporting_api.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /// ///////here is where the application start

    //Get all users
    @GetMapping(value = "/user")
    public List<UserModel> getUsers() {
        return userRepository.findAll();
    }

    //Get user by ID
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<UserModel> getUserByIdWithPotholes(@PathVariable Long id) {
        return userRepository.findByIdWithPotholesAndFeedbacks(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Register a new user
//    @PostMapping(value = "/user")
//    public ResponseEntity<String> saveUser(@Valid @RequestBody UserDTO userDTO) {
//        try {
//            if (userDTO.getName() == null || userDTO.getEmail() == null || userDTO.getPassword() == null){
//                return ResponseEntity.badRequest().body("Name, email, and password are required");
//            }
//
//            System.out.println("Password received: " + userDTO.getPassword());
//
//            if (userRepository.findByEmail(userDTO.getEmail()) != null){
//                return ResponseEntity.badRequest().body("Email already exists");
//            }
//
//            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//            userRepository.save(userDTO);
//            return ResponseEntity.ok("User saved!");
//        } catch (Exception e) {
//            e.printStackTrace(); // This will log the root cause in the terminal
//            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
//        }
//    }

    @PostMapping(value = "/user")
    public ResponseEntity<String> saveUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            if (userDTO.getName() == null || userDTO.getEmail() == null || userDTO.getPassword() == null) {
                return ResponseEntity.badRequest().body("Name, email, and password are required");
            }

            System.out.println("Password received: " + userDTO.getPassword());

            if (userRepository.findByEmail(userDTO.getEmail()) != null) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            // Map UserDTO to UserModel
            UserModel user = new UserModel();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            userRepository.save(user);
            return ResponseEntity.ok("User saved!");
        } catch (Exception e) {
            e.printStackTrace(); // This will log the root cause in the terminal
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }





    //Login user
    @PostMapping(value = "/user/login")
    public ResponseEntity<?> loginUser(@RequestBody UserModel loginRequest) {
        try {
            UserModel existingUser = userRepository.findByEmail(loginRequest.getEmail());

            if (existingUser == null) {
                System.out.println("User not found");
                return ResponseEntity.status(401).body("Invalid email or password");
            }

            System.out.println("Password from request: " + loginRequest.getPassword());
            System.out.println("Password from DB: " + existingUser.getPassword());

            if (passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
                existingUser.setPassword(null);
                return ResponseEntity.ok(existingUser);
            } else {
                System.out.println("Password mismatch");
                return ResponseEntity.status(401).body("Invalid email or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("an unexpected error occurred. Please try again");
        }

    }

    // Logout user
    @PostMapping("/user/logout")
    public ResponseEntity<String> logoutUser() {
        // If using tokens or sessions, you'd invalidate them here
        return ResponseEntity.ok("User logged out successfully");
    }


}

