package app_reporting_api.controller;

import app_reporting_api.dto.PotholeRequestDTO;
import app_reporting_api.model.PotholeModel;
import app_reporting_api.model.UserModel;
import app_reporting_api.repository.PotholeRepository;
import app_reporting_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PotholeControllerTest {

    private PotholeController potholeController;
    private PotholeRepository potholeRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        potholeRepository = mock(PotholeRepository.class);
        userRepository = mock(UserRepository.class);
        potholeController = new PotholeController(potholeRepository, userRepository);
    }

    // Test methods here
    @DisplayName("Should save pothole successfully")
    @Test
    void testSavePotholeSuccess() {
        // Arrange
        PotholeRequestDTO request = new PotholeRequestDTO();
        request.setLatitude(BigDecimal.valueOf(10.123));
        request.setLongitude(BigDecimal.valueOf(20.456));
        request.setUserId(1L);

        UserModel user = new UserModel();
        user.setId(1L);

        PotholeModel potholeWithId = new PotholeModel();
        potholeWithId.setId(42L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(potholeRepository.save(any(PotholeModel.class))).thenAnswer(invocation -> {
            PotholeModel p = invocation.getArgument(0);
            p.setId(42L); // Simulate ID generation
            return p;
        });

        // Act
        ResponseEntity<Map<String, Object>> response = potholeController.savePothole(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Pothole saved!", response.getBody().get("message"));
        assertEquals(42L, response.getBody().get("id"));
    }

    @DisplayName("Should return 400 when user not found")
    @Test
    void testSavePotholeUserNotFound() {
        // Arrange
        PotholeRequestDTO request = new PotholeRequestDTO();
        request.setLatitude(BigDecimal.valueOf(10.123));
        request.setLongitude(BigDecimal.valueOf(20.456));
        request.setUserId(99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Map<String, Object>> response = potholeController.savePothole(request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody().get("error"));
    }

//    @DisplayName("Should return list of potholes")
    @Test
    void testGetAllPotholesSuccess() {
        // Arrange
        PotholeModel pothole = new PotholeModel();
        pothole.setLatitude(BigDecimal.valueOf(10.123));
        pothole.setLongitude(BigDecimal.valueOf(20.456));
        when(potholeRepository.findAll()).thenReturn(List.of(pothole));

        // Act
        ResponseEntity<List<PotholeModel>> response = potholeController.getAllPotholes();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(new BigDecimal("10.123"), response.getBody().get(0).getLatitude());
    }

    @DisplayName("Should return 500 if repository throws exception")
    @Test
    void testGetAllPotholesException() {
        // Arrange
        when(potholeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<List<PotholeModel>> response = potholeController.getAllPotholes();

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}