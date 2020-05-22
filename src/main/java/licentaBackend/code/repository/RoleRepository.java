package licentaBackend.code.repository;

import java.util.Optional;

import licentaBackend.code.models.ERole;
import licentaBackend.code.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
