package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.engineering.lysep.entity.ProfessorModule;

import java.util.List;

public interface ProfessorModuleRepository extends JpaRepository<ProfessorModule, Integer> {
    boolean existsByProfessorIdAndModuleId(int professorId, int moduleId);

    List<ProfessorModule> findAllByModuleId(int id);

    List<ProfessorModule> findAllByProfessorId(int id);

    void deleteByModuleIdAndProfessorIdIn(int moduleId, List<Integer> professorsToRemove);
}
