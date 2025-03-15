package app_reporting_api.controller;

import app_reporting_api.model.FeedbackModel;
import app_reporting_api.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;
    //setting the back end
//    @GetMapping("/feedb") //First endpoint
//    public String getPage(){
//        return "Hello, I hit my endpoint for feedback";
//    }

    //////////here is where the application start

    @GetMapping(value = "/get.feedback")
    public List<FeedbackModel> getPotholes(){
        return feedbackRepository.findAll();
    }

    @PostMapping(value = "post.feedback")
    public String saveUser(@RequestBody FeedbackModel feedback){
        feedbackRepository.save(feedback);
        return "Feedback saved!";
    }

    @PutMapping(value = "/update.feedback/{id}")
    public String updateFeedback(@PathVariable Integer id, @RequestBody FeedbackModel feedback) {
        FeedbackModel updateFeedback = feedbackRepository.findById(id).get();
        updateFeedback.setComment(feedback.getComment());
        feedbackRepository.save(updateFeedback);
        return  "Feedback Updated!";
    }

    @DeleteMapping(value = "/delete.feedback/{id}")
    public String deleteFeedback(@PathVariable Integer id){
        FeedbackModel deleteFeedback = feedbackRepository.findById(id).get();
        feedbackRepository.delete(deleteFeedback);
        return "Delete feedback with the id:" + id;
    }

}
