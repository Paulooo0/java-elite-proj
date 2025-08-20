package pauloh.main.service;

import org.springframework.stereotype.Service;

import pauloh.main.dto.user.CreateUserDto;
import pauloh.main.dto.user.UserResponseDTO;
import pauloh.main.model.Users;
import pauloh.main.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository repository;

  UserService(UserRepository repository) {
    this.repository = repository;
  }

  public UserResponseDTO createUser(CreateUserDto dto) {
    if (repository.existsByEmail(dto.email())) {
      throw new IllegalArgumentException("User with this email already exists");
    }

    Users user = new Users(dto.name(), dto.email());
    repository.save(user);

    UserResponseDTO res = new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    return res;
  }
}
