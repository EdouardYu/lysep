package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.engineering.lysep.entity.StudentModule;

public interface StudentModuleRepository extends JpaRepository<StudentModule, Integer> {
}
