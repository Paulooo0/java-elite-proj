package pauloh.main.adapter.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pauloh.main.core.domain.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, UUID> {
  Optional<Loan> findById(UUID id);

  List<Loan> findAllByUserId(UUID userId);

  List<Loan> findAll();

  @Query("SELECT 1 FROM Loan l WHERE l.userId = :userId AND l.bookId = :bookId AND l.takenAt IS NOT NULL AND l.devolvedAt IS NULL")
  Optional<Loan> findActiveLoan(@Param("userId") UUID userId, @Param("bookId") UUID bookid);

  List<Loan> findAllByDevolvedAtIsNull();
}
