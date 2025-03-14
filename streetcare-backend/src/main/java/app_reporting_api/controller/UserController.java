package app_reporting_api.controller;

import app_reporting_api.model.UserModel;
import app_reporting_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //setting the back end
    @GetMapping(value = "/test") //First endpoint
    public String getPage() {
        return "Hello, I hit my endpoint for users";
    }


    //////////here is where the application start

    @GetMapping(value = "/users")
    public List<UserModel> getUsers(){
        return userRepository.findAll();
    }

}

