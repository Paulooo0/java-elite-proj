package pauloh.main.controller;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pauloh.main.dto.book.BookResponseDto;
import pauloh.main.dto.book.CreateBookDto;
import pauloh.main.dto.book.UpdateBookDto;
import pauloh.main.service.BookService;

@RestController
@RequestMapping("/livros")
public class BookController {
  private final BookService service;

  public BookController(BookService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<BookResponseDto> createBook(@RequestBody CreateBookDto dto) {
    BookResponseDto res = service.createBook(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }

  @GetMapping
  public CompletableFuture<ResponseEntity<List<BookResponseDto>>> getAllBooksByTitle(@RequestParam String title) {
    return service.getAllBooksByTitle(title)
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @PutMapping("/{id}")
  public CompletableFuture<ResponseEntity<BookResponseDto>> updateBook(@PathVariable UUID id,
      @RequestBody UpdateBookDto dto) {
    return service.updateBook(id, dto)
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
  }

  @PutMapping("/estoque/{id}")
  public CompletableFuture<ResponseEntity<BookResponseDto>> updateBookStock(@PathVariable UUID id,
      @RequestParam Integer quantity) {
    return service.updateBookStock(id, quantity)
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
  }

  @DeleteMapping("/{id}")
  public CompletableFuture<ResponseEntity<String>> deleteBook(@PathVariable UUID id) {
    return service.deleteBook(id)
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Livro não encontrado"));
  }
}
