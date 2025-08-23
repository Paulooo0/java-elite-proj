package pauloh.main.port.output;

import pauloh.main.core.domain.model.Users;

public interface UserOutputPort {
  Users save(Users user);
}
