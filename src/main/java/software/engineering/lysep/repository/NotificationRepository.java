package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.engineering.lysep.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
