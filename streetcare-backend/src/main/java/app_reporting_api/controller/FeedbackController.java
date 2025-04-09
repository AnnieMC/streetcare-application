package app_reporting_api.controller;

import app_reporting_api.model.FeedbackModel;
import app_reporting_api.model.UserModel;
import app_reporting_api.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
///api/feedback
///normal conventions
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;
    //setting the back end
//    @GetMapping("/feedb") //First endpoint
//    public String getPage(){
//        return "Hello, I hit my endpoint for feedback";
//    }

    //////////here is where the application start

    @GetMapping(value = "/feedback")
    public List<FeedbackModel> getPotholes(){
        return feedbackRepository.findAll();
    }

    //get feedback by ID
    @RequestMapping(value = "/feedback/{id}")
    public ResponseEntity<FeedbackModel> getFeedbackById(@PathVariable Integer id){
        return feedbackRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/feedback")
    public String saveUser(@RequestBody FeedbackModel feedback){
        feedbackRepository.save(feedback);
        return "Feedback saved!";
    }

    @PutMapping(value = "/feedback/{id}")
    public String updateFeedback(@PathVariable Integer id, @RequestBody FeedbackModel feedback) {
        FeedbackModel updateFeedback = feedbackRepository.findById(id).get();
        updateFeedback.setComment(feedback.getComment());
        feedbackRepository.save(updateFeedback);
        return  "Feedback Updated!";
    }

    @DeleteMapping(value = "/feedback/{id}")
    public String deleteFeedback(@PathVariable Integer id){
        FeedbackModel deleteFeedback = feedbackRepository.findById(id).get();
        feedbackRepository.delete(deleteFeedback);
        return "Delete feedback with the id:" + id;
    }

}
