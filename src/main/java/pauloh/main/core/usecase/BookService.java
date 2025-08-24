package pauloh.main.core.usecase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pauloh.main.core.domain.model.Book;
import pauloh.main.port.input.BookInputPort;
import pauloh.main.port.output.BookOutputPort;

public class BookService implements BookInputPort {
  private final BookOutputPort bookInputPort;

  public BookService(BookOutputPort bookInputPort) {
    this.bookInputPort = bookInputPort;
  }

  public Book createBook(Book book) {
    Optional<Book> findedBook = bookInputPort.findByIsbn(book.getIsbn());
    if (findedBook.isPresent() && findedBook.get().getIsActive()) {
      throw new IllegalArgumentException("Book with this ISBN already exists");
    } else if (findedBook.isPresent() && !findedBook.get().getIsActive()) {
      Book updateBook = findedBook.get();
      updateBook.setIsActive(true);
      updateBook.setTitle(updateBook.getTitle());
      updateBook.setAuthor(updateBook.getAuthor());
      return bookInputPort.save(updateBook);
    }

    return bookInputPort.save(book);
  }

  public List<Book> getAllBooksByTitle(String title) {
    List<Book> books = bookInputPort.findAllByTitle(title);

    if (books.isEmpty()) {
      throw new IllegalArgumentException("No books found with the specified title");
    }

    return books;
  }

  public Book updateBook(UUID id, Book book) {
    Optional<Book> findedBook = bookInputPort.findById(id);
    if (findedBook == null) {
      throw new IllegalArgumentException("Book with the specified ID does not exist");
    }

    Optional.ofNullable(book.getIsbn()).ifPresent(findedBook.get()::setIsbn);
    Optional.ofNullable(book.getTitle()).ifPresent(findedBook.get()::setTitle);
    Optional.ofNullable(book.getAuthor()).ifPresent(findedBook.get()::setAuthor);

    return bookInputPort.save(book);
  }

  public Book updateBookStock(UUID id, Integer quantity) {
    Optional<Book> findedBook = bookInputPort.findById(id);
    if (findedBook == null) {
      throw new IllegalArgumentException("Book with the specified ID does not exist");
    }
    Book book = findedBook.get();

    Integer stock = book.getStock();

    if (stock + quantity < 0) {
      throw new IllegalArgumentException("Insufficient stock to remove the specified quantity");
    }

    book.setStock(book.getStock() + quantity);
    return bookInputPort.save(book);
  }

  public String deleteBook(UUID id) {
    Optional<Book> findedBook = bookInputPort.findById(id);
    if (findedBook == null) {
      throw new IllegalArgumentException("Book with the specified ID does not exist");
    }
    Book book = findedBook.get();

    book.setIsActive(false);
    bookInputPort.save(book);

    return ("Book with ID " + id + " deleted successfully");
  }
}