package app_reporting_api.dto;

import lombok.*;
import java.time.LocalDate;

@Data
public class FeedbackRequestDTO {
    private Long id;
    private Long potholeId;
    private String comment;
    private LocalDate feedbackDate;
    private Long userId;
}
