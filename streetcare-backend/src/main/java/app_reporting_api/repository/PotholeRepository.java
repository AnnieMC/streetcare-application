package app_reporting_api.repository;

import app_reporting_api.model.PotholeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PotholeRepository extends JpaRepository<PotholeModel, Long> {
}
