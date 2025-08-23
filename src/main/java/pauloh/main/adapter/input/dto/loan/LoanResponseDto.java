package pauloh.main.adapter.input.dto.loan;

import java.time.Instant;
import java.util.UUID;

public record LoanResponseDto(UUID id, Instant takenAt, Instant expectedDevolution, Instant devolvedAt) {

}
