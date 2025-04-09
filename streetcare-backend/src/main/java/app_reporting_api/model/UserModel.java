package app_reporting_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
@Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "registration_date", insertable = false, updatable = false)
    private LocalDateTime registrationDate;
}