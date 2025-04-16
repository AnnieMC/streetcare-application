package app_reporting_api.repository;

import app_reporting_api.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository <UserModel, Long> {
    UserModel findByEmail(String Email);
    @Query("SELECT DISTINCT u FROM UserModel u LEFT JOIN FETCH u.potholes WHERE u.id = :id")
    Optional<UserModel> findByIdWithPotholesAndFeedbacks(@Param("id") Long id);
}
