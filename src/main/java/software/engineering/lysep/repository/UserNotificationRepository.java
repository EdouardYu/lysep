package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.engineering.lysep.entity.UserNotification;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer> {
}
