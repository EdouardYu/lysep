package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.engineering.lysep.entity.Module;

public interface ModuleRepository extends JpaRepository<Module, Integer> {
}
