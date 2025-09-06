package pauloh.main.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pauloh.main.core.usecase.BookService;
import pauloh.main.port.output.BookOutputPort;

@Configuration
public class BookBeanConfig {
  @Bean
  public BookService bookService(BookOutputPort bookOutputPort) {
    return new BookService(bookOutputPort);
  }
}
