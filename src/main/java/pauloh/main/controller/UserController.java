package pauloh.main.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pauloh.main.dto.user.CreateUserDto;
import pauloh.main.service.UserService;

@RestController
@RequestMapping("/usuarios")
public class UserController {
  private final UserService service;

  UserController(UserService service) {
    this.service = service;
  }

  @PostMapping
  public void createUser(CreateUserDto dto) {
    service.createUser(dto);
  }
}
