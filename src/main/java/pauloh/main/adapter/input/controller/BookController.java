package pauloh.main.adapter.input.controller;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pauloh.main.adapter.input.controller.mapper.BookRestMapper;
import pauloh.main.adapter.input.dto.book.BookRes;
import pauloh.main.adapter.input.dto.book.BookReq;
import pauloh.main.core.domain.model.Book;
import pauloh.main.port.input.BookInputPort;

@RestController
@RequestMapping("/livros")
public class BookController {
  private final BookInputPort bookInputPort;
  private final BookRestMapper mapper;

  public BookController(BookInputPort bookInputPort, BookRestMapper mapper) {
    this.bookInputPort = bookInputPort;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<BookRes> createBook(@RequestBody BookReq req) {
    Book book = mapper.toDomain(req);
    Book created = bookInputPort.createBook(book);
    BookRes res = mapper.toResponse(created);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }

  @Async
  @GetMapping
  public CompletableFuture<ResponseEntity<List<BookRes>>> getAllBooksByTitle(@RequestParam String title) {
    List<BookRes> books = bookInputPort.getAllBooksByTitle(title).stream()
        .map(mapper::toResponse)
        .toList();
    return CompletableFuture.completedFuture(ResponseEntity.ok(books));

  }

  @Async
  @PutMapping("/{id}")
  public CompletableFuture<ResponseEntity<BookRes>> updateBook(@PathVariable UUID id,
      @RequestBody BookReq req) {
    Book book = mapper.toDomain(req);
    Book res = bookInputPort.updateBook(id, book);
    return CompletableFuture.completedFuture(ResponseEntity.ok(mapper.toResponse(res)));
  }

  @Async
  @PutMapping("/estoque/{id}")
  public CompletableFuture<ResponseEntity<BookRes>> updateBookStock(@PathVariable UUID id,
      @RequestParam Integer quantity) {
    Book res = bookInputPort.updateBookStock(id, quantity);
    return CompletableFuture.completedFuture(ResponseEntity.ok(mapper.toResponse(res)));
  }

  @Async
  @DeleteMapping("/{id}")
  public CompletableFuture<ResponseEntity<String>> deleteBook(@PathVariable UUID id) {
    String res = bookInputPort.deleteBook(id);
    return CompletableFuture.completedFuture(
        ResponseEntity.ok(res));
  }
}
