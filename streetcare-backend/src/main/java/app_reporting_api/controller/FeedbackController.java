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
@RequestMapping("/api") // Base path for all endpoints in this controller.
public class FeedbackController {

    // Injects dependencies via constructor
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final PotholeRepository potholeRepository;

    @Autowired
    public FeedbackController(FeedbackRepository feedbackRepository,
                              UserRepository userRepository,
                              PotholeRepository potholeRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.potholeRepository = potholeRepository;
    }

    //////////here is where the application start

    //Add a feedback for the current pothole
    @PostMapping(value = "/feedback")
    public ResponseEntity<String> saveFeedback(@RequestBody FeedbackRequestDTO request) {
        //Retrieve the user and pothole by their IDs
        UserModel user = userRepository.findById(request.getUserId()).orElse(null);
        PotholeModel pothole = potholeRepository.findById(request.getPotholeId()).orElse(null);

        if (user == null || pothole == null) { //Return an error response if either is not found
            return ResponseEntity.badRequest().body("User or Pothole not found");
        }

        //Create a new feedbackModel and associate it with the user and pothole
        FeedbackModel feedback = new FeedbackModel();
        feedback.setUser(user);
        feedback.setPothole(pothole);
        feedback.setComment(request.getComment());

        feedbackRepository.save(feedback);

        return ResponseEntity.ok("Feedback saved!");
    }

}
