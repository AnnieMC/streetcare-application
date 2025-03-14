package app_reporting_api.controller;

import app_reporting_api.model.FeedbackModel;
import app_reporting_api.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;
    //setting the back end
    @GetMapping("/feedb") //First endpoint
    public String getPage(){
        return "Hello, I hit my endpoint for feedback";
    }

    //////////here is where the application start

    @GetMapping(value = "/feedbacks")
    public List<FeedbackModel> getPotholes(){
        return feedbackRepository.findAll();
    }

}
