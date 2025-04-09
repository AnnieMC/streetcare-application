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

//    //setting the back end
//    @GetMapping(value = "/test") //First endpoint
//    public String getPage() {
//        return "Hello, I hit my endpoint for users";
//    }


    /// ///////here is where the application start

    //get all users
    @GetMapping(value = "/user")
    public List<UserModel> getUsers() {
        return userRepository.findAll();
    }

    //get user by ID
    @RequestMapping(value = "/user/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Integer id){
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Register a new user
    @PostMapping(value = "/user")
    public String saveUser(@RequestBody UserModel user) {
        //Hash the password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "User saved!";
    }

    //Login user
//    @PostMapping(value = "/user/login")
//    public ResponseEntity<?> loginUser(@RequestBody UserModel loginRequest) {
//        UserModel existingUser = userRepository.findByEmail(loginRequest.getEmail());
//
//        if (existingUser != null && passwordEncoder.matches(loginRequest.getPassword(),
//                existingUser.getPassword())) {
//            //Return user data without password
//            existingUser.setPassword(null);
//            return ResponseEntity.ok(existingUser); // You could return a token or user info here
//        } else {
//            return ResponseEntity.status(401).body("Invalid email or password");
//        }
//    }
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



    //update user
    @PutMapping(value = "/user/{id}")
    public String updateUser(@PathVariable Integer id, @RequestBody UserModel user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (user.getPassword() !=null){
                        String encodePassword = passwordEncoder.encode(user.getPassword());
                        existingUser.setPassword(encodePassword);
                    }
                    if (user.getName() !=null) existingUser.setName(user.getName());
                    if (user.getEmail() !=null) existingUser.setEmail(user.getEmail());

                    userRepository.save(existingUser);
                    return "User Updated!";
                })
                .orElse("User not found");
//        UserModel updateUser = userRepository.findById(id).get();
//        updateUser.setName(user.getName());
//        updateUser.setEmail(user.getEmail());
//        updateUser.setPassword(user.getPassword());
//        userRepository.save(updateUser);
//        return  "User Updated!";
    }

    //delete user
    @DeleteMapping(value = "/user/{id}")
    public String deleteUser(@PathVariable Integer id){
        UserModel deleteUser = userRepository.findById(id).get();
        userRepository.delete(deleteUser);
        return "Delete user with the id:" + id;
    }

}

