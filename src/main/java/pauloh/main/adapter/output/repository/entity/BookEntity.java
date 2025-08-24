package pauloh.main.adapter.output.repository.entity;

import java.time.Instant;
import java.util.UUID;

public class BookEntity {
  private UUID id;

  private String isbn;

  private String title;

  private String author;

  private Integer stock = 0;

  private Boolean isActive = true;

  private Instant createdAt = Instant.now();

  private Instant updatedAt;

  public BookEntity(UUID id, String isbn, String title, String author, Integer stock, Boolean isActive,
      Instant createdAt, Instant updatedAt) {
    this.id = id;
    this.isbn = isbn;
    this.title = title;
    this.author = author;
    this.stock = stock;
    this.isActive = isActive;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
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

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public UUID getId() {
    return id;
  }

  public String getIsbn() {
    return isbn;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public Integer getStock() {
    return stock;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
