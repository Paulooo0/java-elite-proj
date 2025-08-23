package pauloh.main.core.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import pauloh.main.adapter.input.dto.book.BookResponseDto;
import pauloh.main.adapter.input.dto.book.CreateBookDto;
import pauloh.main.adapter.input.dto.book.UpdateBookDto;
import pauloh.main.adapter.output.repository.impl.BookRepository;
import pauloh.main.core.domain.model.Book;

@Service
public class BookService {
  private final BookRepository repository;

  BookService(BookRepository repository) {
    this.repository = repository;
  }

  public BookResponseDto createBook(CreateBookDto dto) {
    Optional<Book> findedBook = repository.findByIsbn(dto.isbn());
    if (findedBook.isPresent() && findedBook.get().getIsActive()) {
      throw new IllegalArgumentException("Book with this ISBN already exists");
    } else if (findedBook.isPresent() && !findedBook.get().getIsActive()) {
      Book book = findedBook.get();
      book.setIsActive(true);
      book.setTitle(dto.title());
      book.setAuthor(dto.author());
      repository.save(book);
      return new BookResponseDto(
          book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.getStock());
    }

    Book book = new Book(dto.isbn(), dto.title(), dto.author());
    repository.save(book);

    BookResponseDto res = new BookResponseDto(
        book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.getStock());
    return res;
  }

  @Async
  public CompletableFuture<List<BookResponseDto>> getAllBooksByTitle(String title) {
    List<Book> books = repository.findAllByTitle(title);

    List<BookResponseDto> res = new ArrayList<>();
    books.forEach(book -> {
      if (book.getIsActive()) {
        res.add(
            new BookResponseDto(
                book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.getStock()));
      }
    });

    if (res.isEmpty()) {
      throw new EntityNotFoundException("No books found with the specified title");
    }

    return CompletableFuture.completedFuture(res);
  }

  @Async
  public CompletableFuture<BookResponseDto> updateBook(UUID id, UpdateBookDto dto) {
    Book book = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));

    Optional.ofNullable(dto.isbn()).ifPresent(book::setIsbn);
    Optional.ofNullable(dto.title()).ifPresent(book::setTitle);
    Optional.ofNullable(dto.author()).ifPresent(book::setAuthor);

    repository.save(book);

    BookResponseDto res = new BookResponseDto(
        book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.getStock());
    return CompletableFuture.completedFuture(res);
  }

  @Async
  public CompletableFuture<BookResponseDto> updateBookStock(UUID id, Integer quantity) {
    Book book = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));

    Integer stock = book.getStock();

    if (stock + quantity < 0) {
      throw new IllegalArgumentException("Insufficient stock to remove the specified quantity");
    }

    book.setStock(book.getStock() + quantity);
    repository.save(book);

    BookResponseDto res = new BookResponseDto(
        book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.getStock());
    return CompletableFuture.completedFuture(res);
  }

  @Async
  public CompletableFuture<String> deleteBook(UUID id) {
    Book book = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));

    book.setIsActive(false);
    repository.save(book);

    return CompletableFuture.completedFuture("Book with ID " + id + " deleted successfully");
  }
}