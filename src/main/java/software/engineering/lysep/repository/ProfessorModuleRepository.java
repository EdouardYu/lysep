package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.engineering.lysep.entity.ProfessorModule;

public interface ProfessorModuleRepository extends JpaRepository<ProfessorModule, Integer> {
}
