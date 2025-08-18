package pauloh.main.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pauloh.main.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
  Optional<Users> findById(UUID id);

  Optional<Users> findByEmail(String email);

  Optional<Users> findByName(String name);

  List<Users> findAll();

  boolean existsByEmail(String email);
}