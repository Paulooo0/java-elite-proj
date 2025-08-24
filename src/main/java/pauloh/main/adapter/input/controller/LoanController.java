package pauloh.main.adapter.input.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pauloh.main.adapter.input.controller.mapper.LoanRestMapper;
import pauloh.main.adapter.input.dto.loan.LoanReq;
import pauloh.main.adapter.input.dto.loan.LoanRes;
import pauloh.main.adapter.input.dto.loan.LoanedBooksWithUsersReq;
import pauloh.main.core.domain.model.Loan;
import pauloh.main.port.input.LoanInputPort;
import pauloh.main.port.input.LoanQueryPort;

@RestController
@RequestMapping("/emprestimos")
public class LoanController {
  private final LoanInputPort loanInputPort;
  private final LoanQueryPort loanQueryPort;
  private final LoanRestMapper mapper;

  public LoanController(LoanInputPort loanInputPort, LoanRestMapper mapper, LoanQueryPort loanQueryPort) {
    this.loanInputPort = loanInputPort;
    this.mapper = mapper;
    this.loanQueryPort = loanQueryPort;
  }

  @PostMapping
  public ResponseEntity<LoanRes> createLoan(@RequestBody LoanReq req) {
    Loan loan = mapper.toDomain(req);
    Loan created = loanInputPort.createLoan(loan);
    LoanRes res = mapper.toRespose(created);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }

  @Async
  @GetMapping
  public CompletableFuture<ResponseEntity<List<LoanRes>>> getLoansByUserId(@RequestParam UUID userId) {
    List<LoanRes> loans = loanInputPort.getLoansByUserId(userId).stream()
        .map(mapper::toRespose)
        .toList();
    return CompletableFuture.completedFuture(ResponseEntity.ok(loans));
  }

  @Async
  @GetMapping("/multa")
  public CompletableFuture<ResponseEntity<BigDecimal>> getCurrentLoanDeadlinePenalty(@RequestParam UUID loanId) {
    BigDecimal penalty = loanInputPort.getCurrentLoanDeadlinePenalty(loanId);
    return CompletableFuture.completedFuture(ResponseEntity.ok(penalty));
  }

  @Async
  @GetMapping("/ativos")
  public CompletableFuture<ResponseEntity<List<LoanedBooksWithUsersReq>>> getAllActiveLoans() {
    List<LoanedBooksWithUsersReq> loans = loanQueryPort.getAllActiveLoans();
    return CompletableFuture.completedFuture(ResponseEntity.ok(loans));
  }

  @Async
  @PutMapping("/{id}")
  public CompletableFuture<ResponseEntity<LoanRes>> registerDevolution(@PathVariable UUID id) {
    Loan loan = loanInputPort.registerDevolution(id);
    LoanRes res = mapper.toRespose(loan);
    return CompletableFuture.completedFuture(ResponseEntity.ok(res));
  }
}
