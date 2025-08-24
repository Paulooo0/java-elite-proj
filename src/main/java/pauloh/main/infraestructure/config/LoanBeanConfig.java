package pauloh.main.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pauloh.main.core.usecase.LoanService;
import pauloh.main.port.input.LoanQueryPort;
import pauloh.main.port.output.BookOutputPort;
import pauloh.main.port.output.LoanOutputPort;

@Configuration
public class LoanBeanConfig {

  @Bean
  public LoanService loanService(LoanOutputPort loanOutputPort, BookOutputPort bookOutputPort,
      LoanQueryPort loanQueryPort) {
    return new LoanService(loanOutputPort, bookOutputPort, loanQueryPort);
  }
}
