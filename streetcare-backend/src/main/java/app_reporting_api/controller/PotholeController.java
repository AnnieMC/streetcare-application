package app_reporting_api.controller;

import app_reporting_api.model.PotholeModel;
import app_reporting_api.repository.PotholeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PotholeController {

    @Autowired
    private PotholeRepository potholeRepository;

    //setting the back end
    @GetMapping("/poth") //First endpoint
    public String getPage(){
        return "Hello, I hit my endpoint for potholes";
    }

    //////////here is where the application start

    @GetMapping(value = "/potholes")
    public List<PotholeModel> getPotholes(){
        return potholeRepository.findAll();
    }




}

