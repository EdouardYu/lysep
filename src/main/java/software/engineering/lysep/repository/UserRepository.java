package software.engineering.lysep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.engineering.lysep.entity.User;
import software.engineering.lysep.entity.enumeration.Role;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmailAndEnabled(String email, Boolean enabled);

    Optional<User> findByEmail(String email);

    List<User> findAllByIdInAndRole(List<Integer> ids, Role role);
}

