package app_reporting_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

//@Data
@Getter
@Setter
@Entity
@Table(name = "feedback")
public class FeedbackModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "feedback_date", insertable = false, updatable = false)
    private LocalDateTime feedbackDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-feedback")
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "pothole_id")
    @JsonBackReference("pothole-feedbacks")
    private PotholeModel pothole;
}
