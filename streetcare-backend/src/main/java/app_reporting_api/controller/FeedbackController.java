package app_reporting_api.controller;

import app_reporting_api.dto.FeedbackRequestDTO;
import app_reporting_api.model.FeedbackModel;
import app_reporting_api.model.PotholeModel;
import app_reporting_api.model.UserModel;
import app_reporting_api.repository.FeedbackRepository;
import app_reporting_api.repository.PotholeRepository;
import app_reporting_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PotholeRepository potholeRepository;


    //////////here is where the application start

    //Add a feedback for the current pothole
    @PostMapping(value = "/feedback")
    public ResponseEntity<String> saveFeedback(@RequestBody FeedbackRequestDTO request) {
        UserModel user = userRepository.findById(request.getUserId()).orElse(null);
        PotholeModel pothole = potholeRepository.findById(request.getPotholeId()).orElse(null);

        if (user == null || pothole == null) {
            return ResponseEntity.badRequest().body("User or Pothole not found");
        }

        FeedbackModel feedback = new FeedbackModel();
        feedback.setUser(user);
        feedback.setPothole(pothole);
        feedback.setComment(request.getComment());

        feedbackRepository.save(feedback);

        return ResponseEntity.ok("Feedback saved!");
    }

}
