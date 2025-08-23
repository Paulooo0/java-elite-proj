package pauloh.main.adapter.input.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pauloh.main.adapter.input.controller.mapper.UserRestMapper;
import pauloh.main.adapter.input.dto.user.CreateUserReq;
import pauloh.main.core.domain.model.Users;
import pauloh.main.port.input.UserInputPort;

@RestController
@RequestMapping("/usuarios")
public class UserController {
  private final UserInputPort userInputPort;
  private final UserRestMapper mapper;

  UserController(UserInputPort userInputPort, UserRestMapper mapper) {
    this.userInputPort = userInputPort;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<Users> createUser(@RequestBody CreateUserReq req) {
    Users user = mapper.toDomain(req);
    Users res = userInputPort.createUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }
}
