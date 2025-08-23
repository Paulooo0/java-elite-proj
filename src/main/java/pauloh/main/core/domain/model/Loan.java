package pauloh.main.core.domain.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Loan {
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(nullable = false, updatable = false, columnDefinition = "UUID DEFAULT gen_random_uuid()")
  private UUID id;

  @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "fk_loan_book"), nullable = false)
  private UUID bookId;

  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_loan_user"), nullable = false)
  private UUID userId;

  @Column(nullable = false)
  private Instant takenAt;

  @Column(nullable = false)
  private Instant expectedDevolution;

  @Column(nullable = true)
  private Instant devolvedAt;

  public Loan(UUID bookId, UUID userId) {
    setBookId(bookId);
    setUserId(userId);
    this.takenAt = Instant.now();
    setExpectedDevolution(takenAt.plus(7, ChronoUnit.DAYS));
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
}
