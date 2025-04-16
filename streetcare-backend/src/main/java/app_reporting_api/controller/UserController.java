package app_reporting_api.controller;

import app_reporting_api.model.UserModel;
import app_reporting_api.repository.UserRepository;
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
    @PostMapping(value = "/user")
    public ResponseEntity<String> saveUser(@RequestBody UserModel user) {
        //Validate fields
        if (user.getName() == null || user.getEmail() == null || user.getPassword() == null){
            return ResponseEntity.badRequest().body("Name, email, and password are required");
        }
        //Check if the email is already registered
        if (userRepository.findByEmail(user.getEmail()) != null){
            return ResponseEntity.badRequest().body("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User saved!");
    }

    //Login user
    @PostMapping(value = "/user/login")
    public ResponseEntity<?> loginUser(@RequestBody UserModel loginRequest) {
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
    }


}

