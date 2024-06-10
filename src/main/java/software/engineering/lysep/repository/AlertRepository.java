package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.engineering.lysep.entity.Alert;

public interface AlertRepository extends JpaRepository<Alert, Integer> {
}
