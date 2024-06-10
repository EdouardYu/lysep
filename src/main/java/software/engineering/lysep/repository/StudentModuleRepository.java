package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.engineering.lysep.entity.StudentModule;

import java.util.List;

public interface StudentModuleRepository extends JpaRepository<StudentModule, Integer> {
    List<StudentModule> findAllByModuleId(int id);

    List<StudentModule> findAllByStudentId(int id);

    void deleteByModuleIdAndStudentIdIn(int moduleId, List<Integer> studentsToRemove);
}
