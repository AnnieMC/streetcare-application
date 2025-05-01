package app_reporting_api.controller;

import app_reporting_api.dto.FeedbackRequestDTO;
import app_reporting_api.model.FeedbackModel;
import app_reporting_api.model.PotholeModel;
import app_reporting_api.model.UserModel;
import app_reporting_api.repository.FeedbackRepository;
import app_reporting_api.repository.PotholeRepository;
import app_reporting_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackControllerTest {

    private FeedbackController feedbackController;
    private FeedbackRepository feedbackRepository;
    private UserRepository userRepository;
    private PotholeRepository potholeRepository;

    @BeforeEach
    void setUp() {
        feedbackRepository = mock(FeedbackRepository.class);
        userRepository = mock(UserRepository.class);
        potholeRepository = mock(PotholeRepository.class);
        feedbackController = new FeedbackController(feedbackRepository, userRepository, potholeRepository);
    }

    // Test methods here
    @DisplayName("Should save feedback successfully")
    @Test
    void testSaveFeedbackSuccess() {
        // Arrange
        FeedbackRequestDTO request = new FeedbackRequestDTO();
        request.setUserId(1L);
        request.setPotholeId(10L);
        request.setComment("This pothole is dangerous!");

        UserModel user = new UserModel();
        user.setId(1L);

        PotholeModel pothole = new PotholeModel();
        pothole.setId(10L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(potholeRepository.findById(10L)).thenReturn(Optional.of(pothole));

        // Act
        ResponseEntity<String> response = feedbackController.saveFeedback(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Feedback saved!", response.getBody());
        verify(feedbackRepository, times(1)).save(any(FeedbackModel.class));
    }

    @DisplayName("Should return 400 when user not found")
    @Test
    void testSaveFeedbackUserNotFound() {
        // Arrange
        FeedbackRequestDTO request = new FeedbackRequestDTO();
        request.setUserId(99L);
        request.setPotholeId(10L);
        request.setComment("Can't find user");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = feedbackController.saveFeedback(request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User or Pothole not found", response.getBody());
        verify(feedbackRepository, never()).save(any());
    }

    @DisplayName("Should return 400 when pothole not found")
    @Test
    void testSaveFeedbackPotholeNotFound() {
        // Arrange
        FeedbackRequestDTO request = new FeedbackRequestDTO();
        request.setUserId(1L);
        request.setPotholeId(999L);
        request.setComment("Can't find pothole");

        UserModel user = new UserModel();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(potholeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = feedbackController.saveFeedback(request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User or Pothole not found", response.getBody());
        verify(feedbackRepository, never()).save(any());
    }

}