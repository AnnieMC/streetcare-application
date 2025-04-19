package app_reporting_api.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

//@Data
@Getter
@Setter
public class PotholeRequestDTO {
    private Long id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDate reportDate;
    private Long userId;
    private List<FeedbackRequestDTO> feedbacks;
}
