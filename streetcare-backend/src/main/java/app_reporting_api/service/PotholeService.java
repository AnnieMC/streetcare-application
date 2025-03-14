//package app_reporting_api.service;
//
//import app_reporting_api.repository.PotholeRepository;
//import app_reporting_api.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class PotholeService {
//    private final PotholeRepository potholeRepository;
//    private final UserRepository userRepository;
//
//    public Pothole createPothole(double latitude, double longitude, Long userId) {
//        SecurityProperties.User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Pothole pothole = new Pothole();
//        pothole.setLatitude(latitude);
//        pothole.setLongitude(longitude);
//        pothole.setUser(user);
//
//        return potholeRepository.save(pothole);
//    }
//}