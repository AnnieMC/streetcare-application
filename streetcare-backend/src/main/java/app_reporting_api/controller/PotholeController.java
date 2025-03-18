package app_reporting_api.controller;

import app_reporting_api.model.PotholeModel;
import app_reporting_api.repository.PotholeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PotholeController {

    @Autowired
    private PotholeRepository potholeRepository;

    //setting the back end
//    @GetMapping("/poth") //First endpoint
//    public String getPage(){
//        return "Hello, I hit my endpoint for potholes";
//    }

    //////////here is where the application start

    @GetMapping(value = "/get.pothole")
    public List<PotholeModel> getPotholes(){
        return potholeRepository.findAll();
    }

    @PostMapping(value = "post.pothole")
    public String saveUser(@RequestBody PotholeModel pothole){
        potholeRepository.save(pothole);
        return "Pothole saved!";
    }

    @PutMapping(value = "/update.pothole/{id}")
    public String updatePothole(@PathVariable Integer id, @RequestBody PotholeModel pothole) {
        PotholeModel updatePothole = potholeRepository.findById(id).get();
        updatePothole.setLatitude(pothole.getLatitude());
        updatePothole.setLongitude(pothole.getLongitude());
        potholeRepository.save(updatePothole);
        return  "Pothole Updated!";
    }

    @DeleteMapping(value = "/delete.pothole/{id}")
    public String deletePothole(@PathVariable Integer id){
        PotholeModel deletePothole = potholeRepository.findById(id).get();
        potholeRepository.delete(deletePothole);
        return "Delete pothole with the id:" + id;
    }



}

