package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import software.engineering.lysep.entity.Participant;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    boolean existsByUserIdAndEventId(int userId, int eventId);

    List<Participant> findAllByEventId(int id);

    @Query("SELECT P FROM Participant P WHERE P.user.id = :userId AND P.event.deleted = FALSE")
    Stream<Participant> findAllByUserId(int id);
}
