package pauloh.main.core.usecase;

import org.springframework.stereotype.Service;

import pauloh.main.core.domain.model.Users;
import pauloh.main.port.input.UserInputPort;
import pauloh.main.port.output.UserOutputPort;

@Service
public class UserService implements UserInputPort {
  private final UserOutputPort userOutputPort;

  UserService(UserOutputPort userOutputPort) {
    this.userOutputPort = userOutputPort;
  }

  @Override
  public Users createUser(Users user) {
    if (userOutputPort.existsByEmail(user.getEmail())) {
      throw new IllegalArgumentException("User with this email already exists");
    }

    return userOutputPort.save(user);
  }
}
