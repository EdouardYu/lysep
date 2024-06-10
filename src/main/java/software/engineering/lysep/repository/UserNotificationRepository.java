package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import software.engineering.lysep.entity.UserNotification;

import java.time.Instant;
import java.util.stream.Stream;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer> {
    @Query(
        "SELECT un FROM UserNotification un " +
        "WHERE un.user.id = :userId " +
        "AND un.notification.event.date >= :instant " +
        "AND un.notification.event.deleted = false " +
            "ORDER BY un.notification.event.date ASC")
    Stream<UserNotification> findAllByUserIdAndEventDateAfter(int userId, Instant instant);
}
