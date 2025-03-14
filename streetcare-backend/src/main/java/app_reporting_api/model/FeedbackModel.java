package app_reporting_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "feedback")
@Data
public class FeedbackModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "feedback_date", insertable = false, updatable = false)
    private LocalDateTime feedbackDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

}
