package app_reporting_api.repository;

import app_reporting_api.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <UserModel, Integer> {
    UserModel findByEmail(String Email);
}
