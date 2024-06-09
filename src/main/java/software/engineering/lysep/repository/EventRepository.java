package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.engineering.lysep.entity.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
