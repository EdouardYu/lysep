package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import software.engineering.lysep.entity.UserNotification;

import java.time.Instant;
import java.util.stream.Stream;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer> {
    @Query(
        "SELECT UN FROM UserNotification UN " +
        "WHERE UN.user.id = :userId " +
        "AND UN.notification.event.date >= :instant " +
        "AND UN.notification.event.deleted = false " +
            "ORDER BY UN.notification.event.date DESC")
    Stream<UserNotification> findAllByUserIdAndEventDateAfter(int userId, Instant instant);
}
