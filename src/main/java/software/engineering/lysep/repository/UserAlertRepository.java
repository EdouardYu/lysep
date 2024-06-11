package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import software.engineering.lysep.entity.UserAlert;

import java.time.Instant;
import java.util.stream.Stream;

public interface UserAlertRepository extends JpaRepository<UserAlert, Integer> {
    @Query(
        "SELECT UA FROM UserAlert UA " +
            "WHERE UA.user.id = :userId " +
            "AND UA.alert.event.date >= :instant " +
            "AND UA.alert.event.deleted = false " +
            "ORDER BY UA.alert.event.date DESC")
    Stream<UserAlert> findAllByUserIdAndEventDateAfter(int userId, Instant instant);
}
