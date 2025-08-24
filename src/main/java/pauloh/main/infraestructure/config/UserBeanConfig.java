package pauloh.main.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pauloh.main.core.usecase.UserService;
import pauloh.main.port.output.UserOutputPort;

@Configuration
public class UserBeanConfig {
  @Bean
  public UserService userService(UserOutputPort userOutputPort) {
    return new UserService(userOutputPort);
  }
}
