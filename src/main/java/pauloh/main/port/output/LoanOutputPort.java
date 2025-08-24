package pauloh.main.port.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pauloh.main.core.domain.model.Loan;

public interface LoanOutputPort {
  Loan save(Loan loan);

  Optional<Loan> findById(UUID id);

  List<Loan> findAllByUserId(UUID userId);

  List<Loan> findAll();

  Optional<Loan> findActiveLoan(UUID userId, UUID bookid);

  List<Loan> findAllByDevolvedAtIsNull();
}
