package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.engineering.lysep.entity.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
}
