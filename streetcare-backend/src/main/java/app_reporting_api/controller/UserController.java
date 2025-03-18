package app_reporting_api.controller;

import app_reporting_api.model.UserModel;
import app_reporting_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

//    //setting the back end
//    @GetMapping(value = "/test") //First endpoint
//    public String getPage() {
//        return "Hello, I hit my endpoint for users";
//    }


    /// ///////here is where the application start

    //get all users
    @GetMapping(value = "/get.user")
    public List<UserModel> getUsers() {
        return userRepository.findAll();
    }

    //get user by Id
    @RequestMapping(value = "/get.user/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Integer id){
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //add user
    @PostMapping(value = "post.user")
    public String saveUser(@RequestBody UserModel user) {
        userRepository.save(user);
        return "User saved!";
    }

    //update user
    @PutMapping(value = "/update.user/{id}")
    public String updateUser(@PathVariable Integer id, @RequestBody UserModel user) {
        UserModel updateUser = userRepository.findById(id).get();
        updateUser.setName(user.getName());
        updateUser.setEmail(user.getEmail());
        updateUser.setPassword(user.getPassword());
        userRepository.save(updateUser);
        return  "User Updated!";
    }

    //delete user
    @DeleteMapping(value = "/delete.user/{id}")
    public String deleteUser(@PathVariable Integer id){
        UserModel deleteUser = userRepository.findById(id).get();
        userRepository.delete(deleteUser);
        return "Delete user with the id:" + id;
    }

}

