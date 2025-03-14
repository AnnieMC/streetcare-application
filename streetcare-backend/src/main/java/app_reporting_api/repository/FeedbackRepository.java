package app_reporting_api.repository;

import app_reporting_api.model.FeedbackModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<FeedbackModel, Integer> {
}
