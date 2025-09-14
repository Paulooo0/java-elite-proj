package pauloh.main.port.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pauloh.main.core.domain.model.Book;

public interface BookOutputPort {
  Book save(Book book);

  boolean existsByIsbn(String isbn);

  Optional<Book> findById(UUID id);

  Optional<Book> findByIsbn(String isbn);

  List<Book> findAllByTitle(String title);
}
