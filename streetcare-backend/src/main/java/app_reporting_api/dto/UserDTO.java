package app_reporting_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must have 8+ chars, an uppercase, a lowercase, a number, and a special character"
    )
    private String password;
    private LocalDate registrationDate;
    private List<PotholeRequestDTO> potholes;
    private List<FeedbackRequestDTO> feedbacks;
}