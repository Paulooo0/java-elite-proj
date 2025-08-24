package pauloh.main.port.input;

import java.util.List;
import java.util.UUID;

import pauloh.main.core.domain.model.Book;

public interface BookInputPort {
  Book createBook(Book book);

  List<Book> getAllBooksByTitle(String title);

  Book updateBook(UUID id, Book book);

  Book updateBookStock(UUID id, Integer quantity);

  String deleteBook(UUID id);
}
