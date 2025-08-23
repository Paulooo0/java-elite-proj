package pauloh.main.core.usecase;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import pauloh.main.adapter.input.dto.loan.CreateLoanDto;
import pauloh.main.adapter.input.dto.loan.LoanResponseDto;
import pauloh.main.adapter.input.dto.loan.LoanedBooksWithUsersDto;
import pauloh.main.adapter.output.repository.impl.BookRepository;
import pauloh.main.adapter.output.repository.impl.LoanRepository;
import pauloh.main.adapter.output.repository.impl.UserRepository;
import pauloh.main.core.domain.model.Book;
import pauloh.main.core.domain.model.Loan;
import pauloh.main.core.domain.model.Users;

@Service
public class LoanService {
  private final LoanRepository repository;
  private final BookRepository bookRepository;
  private final UserRepository userRepository;

  public LoanService(LoanRepository repository, BookRepository bookRepository, UserRepository userRepository) {
    this.repository = repository;
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
  }

  public LoanResponseDto createLoan(CreateLoanDto dto) {
    if (isLoanActiveForUserAndBook(dto.userId(), dto.bookId())) {
      throw new IllegalStateException("User already has an active loan with this book");
    }

    Book book = bookRepository.findById(dto.bookId())
        .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    if (book.getStock() == 0) {
      throw new IllegalStateException("Book is out of stock");
    } else if (book.getIsActive() != true) {
      throw new IllegalStateException("Book is inactive");
    }

    Loan loan = new Loan(dto.bookId(), dto.userId());

    book.setStock(book.getStock() - 1);

    repository.save(loan);
    bookRepository.save(book);

    LoanResponseDto res = new LoanResponseDto(
        loan.getId(), loan.getTakenAt(), loan.getExpectedDevolution(), loan.getDevolvedAt());
    return res;
  }

  @Async
  public CompletableFuture<List<LoanResponseDto>> getLoansByUserId(UUID id) {
    List<Loan> loans = repository.findAllByUserId(id);

    List<LoanResponseDto> res = new ArrayList<>();
    loans.forEach(loan -> {
      res.add(new LoanResponseDto(
          loan.getId(), loan.getTakenAt(), loan.getExpectedDevolution(), loan.getDevolvedAt()));
    });

    return CompletableFuture.completedFuture(res);
  }

  @Async
  public CompletableFuture<List<LoanedBooksWithUsersDto>> getAllActiveLoans() {
    List<Loan> loans = repository.findAllByDevolvedAtIsNull();

    List<LoanedBooksWithUsersDto> res = new ArrayList<>();
    loans.forEach(loan -> {
      Book book = bookRepository.findById(loan.getBookId())
          .orElseThrow(() -> new EntityNotFoundException("Book not found"));
      String bookTitle = book.getTitle();
      String bookAuthor = book.getAuthor();

      Users user = userRepository.findById(loan.getUserId())
          .orElseThrow(() -> new EntityNotFoundException("User not found"));
      String userName = user.getName();
      String userEmail = user.getEmail();

      res.add(new LoanedBooksWithUsersDto(bookTitle, bookAuthor, userName, userEmail));
    });

    return CompletableFuture.completedFuture(res);
  }

  @Async
  public CompletableFuture<BigDecimal> getCurrentLoanDeadlinePenalty(UUID id) {
    Loan loan = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Loan not found"));

    if (loan.getDevolvedAt() != null) {
      throw new IllegalStateException("Loan already devolved");
    }

    Instant now = Instant.now();
    Instant expectedDevolution = loan.getExpectedDevolution();

    if (now.isBefore(expectedDevolution)) {
      return CompletableFuture.completedFuture(new BigDecimal("0.00"));
    }

    long daysLate = java.time.Duration.between(expectedDevolution, now).toDays();
    BigDecimal penaltyPerDay = new BigDecimal("2.00");
    BigDecimal penalty = penaltyPerDay.multiply(BigDecimal.valueOf(daysLate));

    return CompletableFuture.completedFuture(penalty);
  }

  @Async
  public CompletableFuture<LoanResponseDto> registerDevolution(UUID id) {
    Loan loan = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Loan not found"));

    if (loan.getDevolvedAt() != null) {
      throw new IllegalStateException("Loan already devolved");
    }

    Book book = bookRepository.findById(loan.getBookId())
        .orElseThrow(() -> new EntityNotFoundException("Book not found"));

    book.setStock(book.getStock() + 1);

    loan.setDevolvedAt(Instant.now());

    repository.save(loan);
    bookRepository.save(book);

    LoanResponseDto res = new LoanResponseDto(
        loan.getId(), loan.getTakenAt(), loan.getExpectedDevolution(), loan.getDevolvedAt());
    return CompletableFuture.completedFuture(res);
  }

  boolean isLoanActiveForUserAndBook(UUID userId, UUID bookId) {
    Optional<Loan> loan = repository.findActiveLoan(userId, bookId);
    if (loan.isPresent()) {
      throw new IllegalStateException("User has a active loan with this book");
    }

    return false;
  }
}
