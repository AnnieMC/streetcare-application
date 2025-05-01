package app_reporting_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "potholes")
public class PotholeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10 , scale = 8)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "report_date", insertable = false, updatable = false)
    private LocalDateTime reportDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-potholes")
    private UserModel user;

    @OneToMany(mappedBy = "pothole", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("pothole-feedbacks")
    @BatchSize(size = 10)
    private List<FeedbackModel> feedbacks;
}
