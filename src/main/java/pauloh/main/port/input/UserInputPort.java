package pauloh.main.port.input;

import pauloh.main.core.domain.model.Users;

public interface UserInputPort {
  Users createUser(Users user);
}
