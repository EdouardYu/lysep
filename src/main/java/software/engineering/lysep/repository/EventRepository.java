package software.engineering.lysep.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import software.engineering.lysep.entity.Event;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("SELECT E FROM Event E WHERE E.id = :id AND E.deleted = FALSE")
    Optional<Event> findById(int id);

    @Nonnull
    @Query("SELECT E FROM Event E WHERE E.deleted = FALSE")
    List<Event> findAll();

    @Query("SELECT E FROM Event E WHERE E.date >= :start AND E.date <= :end AND E.deleted = FALSE")
    Stream<Event> findAllByDateBetween(Instant start, Instant end);
}
