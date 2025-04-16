package app_reporting_api.controller;

import app_reporting_api.dto.PotholeRequestDTO;
import app_reporting_api.model.PotholeModel;
import app_reporting_api.model.UserModel;
import app_reporting_api.repository.PotholeRepository;
import app_reporting_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<String> savePothole(@RequestBody PotholeRequestDTO request){
        PotholeModel pothole = new PotholeModel();
        pothole.setLatitude(request.getLatitude());
        pothole.setLongitude(request.getLongitude());

        UserModel user = userRepository.findById(request.getUserId())
                .orElse(null);

        if (user == null){
            return ResponseEntity.badRequest().body("User not found");
        }
        pothole.setUser(user);
        potholeRepository.save(pothole);
        return  ResponseEntity.ok("Pothole saved!");
    }
}

