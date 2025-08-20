package pauloh.main.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pauloh.main.dto.user.CreateUserDto;
import pauloh.main.dto.user.UserResponseDTO;
import pauloh.main.service.UserService;

@RestController
@RequestMapping("/usuarios")
public class UserController {
  private final UserService service;

  UserController(UserService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserDto dto) {
    System.out.println(dto);
    try {
      UserResponseDTO response = service.createUser(dto);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("Cannot create this user", ex);
    }
  }
}
