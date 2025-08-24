package pauloh.main.port.input;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import pauloh.main.core.domain.model.Loan;

public interface LoanInputPort {
  Loan createLoan(Loan loan);

  List<Loan> getLoansByUserId(UUID userId);

  BigDecimal getCurrentLoanDeadlinePenalty(UUID loanId);


  Loan registerDevolution(UUID id);
}
