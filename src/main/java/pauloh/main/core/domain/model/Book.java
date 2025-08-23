package pauloh.main.core.domain.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Book {
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(nullable = false, updatable = false, columnDefinition = "UUID DEFAULT gen_random_uuid()")
  private UUID id;

  @Column(nullable = false, length = 13, unique = true)
  private String isbn;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String author;

  @Column
  private Integer stock = 0;

  @Column
  private Boolean isActive = true;

  public Book(String isbn, String title, String author) {
    setIsbn(isbn);
    setTitle(title);
    setAuthor(author);
    setStock(0);
  }

  public void setIsbn(String isbn) {
    if (isbn == null || isbn.isEmpty()) {
      throw new IllegalArgumentException("ISBN cannot be null or empty");
    }
    if (isbn.length() != 13) {
      throw new IllegalArgumentException("ISBN must be exactly 13 characters long");
    }

    this.isbn = isbn;
  }

  public void setTitle(String title) {
    if (title == null || title.isEmpty()) {
      throw new IllegalArgumentException("Title cannot be null or empty");
    }

    this.title = title;
  }

  public void setAuthor(String author) {
    if (author == null || author.isEmpty()) {
      throw new IllegalArgumentException("Author cannot be null or empty");
    }

    this.author = author;
  }

  public void setStock(Integer stock) {
    if (stock == null || stock < 0) {
      throw new IllegalArgumentException("Stock cannot be null or negative");
    }

    this.stock = stock;
  }

  public void setIsActive(Boolean isActive) {
    if (isActive == null) {
      throw new IllegalArgumentException("isActive cannot be null");
    }

    this.isActive = isActive;
  }
}
