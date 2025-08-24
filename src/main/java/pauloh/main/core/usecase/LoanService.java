package pauloh.main.core.usecase;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

import pauloh.main.adapter.input.dto.loan.LoanedBooksWithUsersReq;
import pauloh.main.core.domain.model.Book;
import pauloh.main.core.domain.model.Loan;
import pauloh.main.port.input.LoanInputPort;
import pauloh.main.port.input.LoanQueryPort;
import pauloh.main.port.output.BookOutputPort;
import pauloh.main.port.output.LoanOutputPort;

@Service
public class LoanService implements LoanInputPort {
  private final LoanOutputPort loanOutputPort;
  private final BookOutputPort bookOutputPort;
  private final LoanQueryPort loanQueryPort;

  public LoanService(LoanOutputPort loanOutputPort, BookOutputPort bookOutputPort,
      LoanQueryPort loanQueryPort) {
    this.loanOutputPort = loanOutputPort;
    this.bookOutputPort = bookOutputPort;
    this.loanQueryPort = loanQueryPort;
  }

  public Loan createLoan(Loan loan) {
    if (isLoanActiveForUserAndBook(loan.getUserId(), loan.getBookId())) {
      throw new IllegalStateException("User already has an active loan with this book");
    }

    Book book = bookOutputPort.findById(loan.getBookId())
        .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    if (book.getStock() == 0) {
      throw new IllegalStateException("Book is out of stock");
    } else if (book.getIsActive() != true) {
      throw new IllegalStateException("Book is inactive");
    }

    book.setStock(book.getStock() - 1);

    bookOutputPort.save(book);
    return loanOutputPort.save(loan);
  }

  public List<Loan> getLoansByUserId(UUID id) {
    List<Loan> loans = loanOutputPort.findAllByUserId(id);

    return loans;
  }

  public List<LoanedBooksWithUsersReq> getAllActiveLoans() {
    List<LoanedBooksWithUsersReq> activeLoands = loanQueryPort.getAllActiveLoans();
    return activeLoands;
  }

  public BigDecimal getCurrentLoanDeadlinePenalty(UUID id) {
    Loan loan = loanOutputPort.findById(id).orElseThrow(() -> new IllegalArgumentException("Loan not found"));

    if (loan.getDevolvedAt() != null) {
      throw new IllegalStateException("Loan already devolved");
    }

    Instant now = Instant.now();
    Instant expectedDevolution = loan.getExpectedDevolution();

    if (now.isBefore(expectedDevolution)) {
      return new BigDecimal("0.00");
    }

    long daysLate = java.time.Duration.between(expectedDevolution, now).toDays();
    BigDecimal penaltyPerDay = new BigDecimal("2.00");
    BigDecimal penalty = penaltyPerDay.multiply(BigDecimal.valueOf(daysLate));

    return penalty;
  }

  public Loan registerDevolution(UUID id) {
    Loan loan = loanOutputPort.findById(id).orElseThrow(() -> new IllegalArgumentException("Loan not found"));

    if (loan.getDevolvedAt() != null) {
      throw new IllegalStateException("Loan already devolved");
    }

    Book book = bookOutputPort.findById(loan.getBookId())
        .orElseThrow(() -> new IllegalArgumentException("Book not found"));

    book.setStock(book.getStock() + 1);

    loan.setDevolvedAt(Instant.now());

    bookOutputPort.save(book);
    return loanOutputPort.save(loan);
  }

  boolean isLoanActiveForUserAndBook(UUID userId, UUID bookId) {
    Optional<Loan> loan = loanOutputPort.findActiveLoan(userId, bookId);
    if (loan.isPresent()) {
      throw new IllegalStateException("User has a active loan with this book");
    }

    return false;
  }
}
