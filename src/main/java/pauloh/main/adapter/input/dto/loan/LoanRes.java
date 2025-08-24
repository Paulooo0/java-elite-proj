package pauloh.main.adapter.input.dto.loan;

import java.time.Instant;
import java.util.UUID;

public record LoanRes(UUID id, Instant takenAt, Instant expectedDevolution, Instant devolvedAt) {

}
