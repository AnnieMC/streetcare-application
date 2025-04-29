package app_reporting_api.controller;

import app_reporting_api.dto.PotholeRequestDTO;
import app_reporting_api.model.PotholeModel;
import app_reporting_api.model.UserModel;
import app_reporting_api.repository.PotholeRepository;
import app_reporting_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class PotholeController {

    @Autowired
    private PotholeRepository potholeRepository;
    @Autowired
    private UserRepository userRepository;

    //////////here is where the application start

    //Add a Pothole
    @PostMapping(value = "/pothole")
    public ResponseEntity<Map<String, Object>> savePothole(@RequestBody PotholeRequestDTO request) {
        PotholeModel pothole = new PotholeModel();
        pothole.setLatitude(request.getLatitude());
        pothole.setLongitude(request.getLongitude());

        UserModel user = userRepository.findById(request.getUserId())
                .orElse(null);

        if (user == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        pothole.setUser(user);
        potholeRepository.save(pothole); // <-- ID will be generated after save!

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Pothole saved!");
        response.put("id", pothole.getId()); // <-- Get the generated ID

        return ResponseEntity.ok(response);
    }

    // Get All Potholes
    @GetMapping(value = "/pothole")
    public ResponseEntity<List<PotholeModel>> getAllPotholes() {
        try {
            List<PotholeModel> potholes = potholeRepository.findAll();
            return ResponseEntity.ok(potholes);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error fetching potholes");
            return ResponseEntity.status(500).body(null);
        }
    }


}

