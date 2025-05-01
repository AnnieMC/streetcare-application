package app_reporting_api.controller;

import app_reporting_api.dto.UserDTO;
import app_reporting_api.model.UserModel;
import app_reporting_api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/api") // Base path for all endpoints in this controller.
public class UserController {

    // Injects dependencies via constructor
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //////////here is where the application start

    //Get all users
    @GetMapping(value = "/user")
    public List<UserModel> getUsers() {
        return userRepository.findAll();// Fetches all users from the database
    }

    //Get user by ID
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<UserModel> getUserByIdWithPotholes(@PathVariable Long id) {
        // Retrieves user by ID along with associated pothole and feedback data
        return userRepository.findByIdWithPotholesAndFeedbacks(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Register a new user
    @PostMapping(value = "/user")
    public ResponseEntity<String> saveUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            //Validate the input
            if (userDTO.getName() == null || userDTO.getEmail() == null || userDTO.getPassword() == null) {
                return ResponseEntity.badRequest().body("Name, email, and password are required");
            }

            System.out.println("Password received: " + userDTO.getPassword());
            if (userRepository.findByEmail(userDTO.getEmail()) != null) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            //Map UserDTO to UserModel and encode the password
            UserModel user = new UserModel();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            //Save new user in the database
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
            //Retrieve user by email
            UserModel existingUser = userRepository.findByEmail(loginRequest.getEmail());

            if (existingUser == null) {
                System.out.println("User not found");
                return ResponseEntity.status(401).body("Invalid email or password");
            }

            //Debugging
            System.out.println("Password from request: " + loginRequest.getPassword());
            System.out.println("Password from DB: " + existingUser.getPassword());

            //Check if the password matches
            if (passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
                existingUser.setPassword(null);//Hide password before sending response
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
    public ResponseEntity<String> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Invalidate user session to log out
            request.getSession().invalidate();
            response.setStatus(HttpServletResponse.SC_OK);
            return ResponseEntity.ok("User logged out successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An unexpected error occurred. Please try again");
        }
    }
}

