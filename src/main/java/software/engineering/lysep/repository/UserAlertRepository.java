package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.engineering.lysep.entity.UserAlert;

public interface UserAlertRepository extends JpaRepository<UserAlert, Integer> {
}
