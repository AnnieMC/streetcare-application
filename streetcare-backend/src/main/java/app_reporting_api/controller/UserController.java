package app_reporting_api.controller;

import app_reporting_api.model.UserModel;
import app_reporting_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "/get.user")
    public List<UserModel> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping(value = "post.user")
    public String saveUser(@RequestBody UserModel user) {
        userRepository.save(user);
        return "User saved!";
    }

//    @PutMapping(value = "/update.user/{id}")
//    public String updateUser(@PathVariable Integer id, @RequestBody UserModel user) {
//        UserModel updateUser = userRepository.findById(id).get();
//        updateUser.setName(user.getName());
//        updateUser.setEmail(user.getEmail());
//        updateUser.setPassword(user.getPassword());
//        userRepository.save(updateUser);
//        return  "User Updated!";
//    }

//    @DeleteMapping(value = "/delete.user/{id}")
//    public String deleteUser(@PathVariable Integer id){
//        UserModel deleteUser = userRepository.findById(id).get();
//        userRepository.delete(deleteUser);
//        return "Delete user with the id:" + id;
//    }

}

