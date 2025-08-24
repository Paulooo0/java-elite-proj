package pauloh.main.adapter.output.repository.entity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class LoanEntity {
  private UUID id;

  private UUID bookId;

  private UUID userId;

  private Instant takenAt = Instant.now();

  private Instant expectedDevolution;

  private Instant devolvedAt;

  private Instant createdAt;

  private Instant updatedAt;

  public LoanEntity(UUID id, UUID bookId, UUID userId, Instant takenAt, Instant expectedDevolution,
      Instant devolvedAt, Instant updatedAt) {
    this.id = id;
    setBookId(bookId);
    setUserId(userId);
    this.takenAt = takenAt;
    setExpectedDevolution(takenAt.plus(7, ChronoUnit.DAYS));
    this.devolvedAt = devolvedAt;
    this.updatedAt = updatedAt;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setBookId(UUID bookId) {
    if (bookId == null) {
      throw new IllegalArgumentException("Book ID cannot be null");
    }

    this.bookId = bookId;
  }

  public void setUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User ID cannot be null");
    }

    this.userId = userId;
  }

  public void setExpectedDevolution(Instant expectedDevolution) {
    if (expectedDevolution == null || expectedDevolution.isBefore(takenAt)) {
      throw new IllegalArgumentException("Expected devolution must be after the taken date");
    }

    this.expectedDevolution = expectedDevolution;
  }

  public void setDevolvedAt(Instant devolvedAt) {
    if (devolvedAt != null && devolvedAt.isBefore(takenAt)) {
      throw new IllegalArgumentException("Devolved date cannot be before the taken date");
    }

    this.devolvedAt = devolvedAt;
  }

  public void setTakenAt(Instant takenAt) {
    this.takenAt = takenAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public UUID getId() {
    return id;
  }

  public UUID getBookId() {
    return bookId;
  }

  public UUID getUserId() {
    return userId;
  }

  public Instant getTakenAt() {
    return takenAt;
  }

  public Instant getExpectedDevolution() {
    return expectedDevolution;
  }

  public Instant getDevolvedAt() {
    return devolvedAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
