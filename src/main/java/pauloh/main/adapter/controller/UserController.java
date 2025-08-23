package pauloh.main.adapter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pauloh.main.adapter.dto.user.CreateUserDto;
import pauloh.main.adapter.dto.user.UserResponseDTO;
import pauloh.main.core.usecase.UserService;

@RestController
@RequestMapping("/usuarios")
public class UserController {
  private final UserService service;

  UserController(UserService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserDto dto) {
    UserResponseDTO res = service.createUser(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }
}
