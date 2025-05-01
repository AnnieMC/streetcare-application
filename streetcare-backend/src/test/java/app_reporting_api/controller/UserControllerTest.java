package app_reporting_api.controller;

import app_reporting_api.dto.UserDTO;
import app_reporting_api.model.UserModel;
import app_reporting_api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController userController;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userController = new UserController(userRepository, passwordEncoder);
    }

    // Test methods here
    @DisplayName("Should return a list of users with correct name")
    @Test
    void testGetUsersReturnsList() {
        // Arrange
        UserModel user = new UserModel();
        user.setName("Annie");
        when(userRepository.findAll()).thenReturn(List.of(user));

        // Act
        List<UserModel> result = userController.getUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Annie", result.get(0).getName());
    }

    @DisplayName("Should return a single user by ID")
    @Test
    void testGetUserByIdFound() {
        // Arrange
        UserModel user = new UserModel();
        user.setId(1L);
        when(userRepository.findByIdWithPotholesAndFeedbacks(1L)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<UserModel> response = userController.getUserByIdWithPotholes(1L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @DisplayName("Should return 404 when user not found by ID")
    @Test
    void testGetUserByIdNotFound() {
        // Arrange
        when(userRepository.findByIdWithPotholesAndFeedbacks(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<UserModel> response = userController.getUserByIdWithPotholes(99L);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
    }

    @DisplayName("Should save user successfully")
    @Test
    void testSaveUserSuccess() {
        // Arrange
        UserDTO dto = new UserDTO();
        dto.setName("Ana");
        dto.setEmail("ana@test.com");
        dto.setPassword("123456");

        when(userRepository.findByEmail("ana@test.com")).thenReturn(null);
        when(passwordEncoder.encode("123456")).thenReturn("hashed-password");

        // Act
        ResponseEntity<String> response = userController.saveUser(dto);
        verify(userRepository, times(1)).save(any(UserModel.class));

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User saved!", response.getBody());
    }

    @DisplayName("Should return 400 when a field is missing")
    @Test
    void testSaveUserMissingFields() {
        // Arrange
        UserDTO dto = new UserDTO(); // no fields set

        // Act

        ResponseEntity<String> response = userController.saveUser(dto);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Name, email, and password are required", response.getBody());
    }

    @DisplayName("Should return 200 when a user logs in successfully")
    @Test
    void testLoginUserSuccess() {
        // Arrange
        UserModel request = new UserModel();
        request.setEmail("test@test.com");
        request.setPassword("secret");

        UserModel saved = new UserModel();
        saved.setEmail("test@test.com");
        saved.setPassword("encoded");

        when(userRepository.findByEmail("test@test.com")).thenReturn(saved);
        when(passwordEncoder.matches("secret", "encoded")).thenReturn(true);

        // Act
        ResponseEntity<?> response = userController.loginUser(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof UserModel);
    }

    @DisplayName("Should return 401 when email is incorrect")
    @Test
    void testLoginUserInvalidEmail() {
        // Arrange
        when(userRepository.findByEmail("missing@test.com")).thenReturn(null);

        UserModel request = new UserModel();
        request.setEmail("missing@test.com");
        request.setPassword("any");

        // Act
        ResponseEntity<?> response = userController.loginUser(request);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
    }

    @DisplayName("Should return 401 when password is incorrect")
    @Test
    void testLoginUserWrongPassword() {
        // Arrange

        UserModel request = new UserModel();
        request.setEmail("ana@test.com");
        request.setPassword("wrong");

        UserModel saved = new UserModel();
        saved.setEmail("ana@test.com");
        saved.setPassword("hashed");

        when(userRepository.findByEmail("ana@test.com")).thenReturn(saved);
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        // Act
        ResponseEntity<?> response = userController.loginUser(request);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
    }

    @DisplayName("Should return 200 when user logs out successfully")
    @Test
    void testLogoutUserSuccess() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getSession()).thenReturn(mock(jakarta.servlet.http.HttpSession.class));

        // Act
        ResponseEntity<String> result = userController.logoutUser(request, response);

        verify(request.getSession(), times(1)).invalidate();
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("User logged out successfully", result.getBody());
    }

    @DisplayName("Should return 500 when an exception occurs during logout")
    @Test
    void testLogoutUserException() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Simulate an exception when getting the session
        when(request.getSession()).thenThrow(new RuntimeException("Session error"));

        // Act
        ResponseEntity<String> result = userController.logoutUser(request, response);

        // Assert
        assertEquals(500, result.getStatusCodeValue());
        assertTrue(result.getBody().contains("An unexpected error occurred"));
    }


}