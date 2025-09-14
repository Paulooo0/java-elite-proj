package pauloh.main.adapter.input.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pauloh.main.core.usecase.UserService;
import pauloh.main.port.input.UserInputPort;

@ExtendWith(SpringExtension.class)
class UserControllerTest {
  @InjectMocks
  private UserController userController;

  @Mock
  private UserInputPort userInputPort;

  @BeforeEach
  void setup() {
    userInputPort = Mockito.mock(UserService.class);
  }

  @Test
  @DisplayName("Should create a new user successfully")
  void testCreateUser() {
    // given

    // when

    // then
  }
}
