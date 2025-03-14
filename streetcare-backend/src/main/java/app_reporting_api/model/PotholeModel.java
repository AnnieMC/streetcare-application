package app_reporting_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "potholes")
@Data
public class PotholeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 10 , scale = 8)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "report_date", insertable = false, updatable = false)
    private LocalDateTime reportDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;
}
