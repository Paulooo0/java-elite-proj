package pauloh.main.service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import pauloh.main.dto.book.BookResponseDto;
import pauloh.main.dto.book.CreateBookDto;
import pauloh.main.dto.book.UpdateBookDto;
import pauloh.main.model.Book;
import pauloh.main.repository.BookRepository;

@Service
public class BookService {
  private final BookRepository repository;

  BookService(BookRepository repository) {
    this.repository = repository;
  }

  public BookResponseDto createBook(CreateBookDto dto) {
    if (repository.existsByIsbn(dto.isbn())) {
      throw new IllegalArgumentException("Book with this ISBN already exists");
    }

    Book book = new Book(dto.isbn(), dto.title(), dto.author());
    repository.save(book);

    BookResponseDto res = new BookResponseDto(
        book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.getStock());
    return res;
  }

  @Async
  public CompletableFuture<BookResponseDto> getBookByTitle(String title) {
    Book book = repository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException("Book not found"));

    BookResponseDto res = new BookResponseDto(
        book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.getStock());
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