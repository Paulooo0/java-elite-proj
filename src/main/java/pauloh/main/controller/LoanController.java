package pauloh.main.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pauloh.main.dto.loan.CreateLoanDto;
import pauloh.main.dto.loan.LoanResponseDto;
import pauloh.main.dto.loan.LoanedBooksWithUsersDto;
import pauloh.main.service.LoanService;

@RestController
@RequestMapping("/emprestimos")
public class LoanController {
  private final LoanService service;

  public LoanController(LoanService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<LoanResponseDto> createLoan(@RequestBody CreateLoanDto dto) {
    LoanResponseDto res = service.createLoan(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }

  @GetMapping
  public CompletableFuture<ResponseEntity<List<LoanResponseDto>>> getLoansByUserId(@RequestParam UUID userId) {
    return service.getLoansByUserId(userId)
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @GetMapping("/multa")
  public CompletableFuture<ResponseEntity<BigDecimal>> getCurrentLoanDeadlinePenalty(@RequestParam UUID loanId) {
    return service.getCurrentLoanDeadlinePenalty(loanId)
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @GetMapping("/ativos")
  public CompletableFuture<ResponseEntity<List<LoanedBooksWithUsersDto>>> getAllActiveLoans() {
    return service.getAllActiveLoans()
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @PutMapping("/{id}")
  public CompletableFuture<ResponseEntity<LoanResponseDto>> registerDevolution(@PathVariable UUID id) {
    return service.registerDevolution(id)
        .thenApply(ResponseEntity::ok)
        .exceptionally(ex -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
  }
}
