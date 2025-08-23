package pauloh.main.port.output;

import java.util.List;
import java.util.UUID;

import pauloh.main.core.domain.model.Users;

public interface UserOutputPort {
  Users save(Users user);

  Users findById(UUID id);

  List<Users> findAll();

  boolean existsByEmail(String email);
}
