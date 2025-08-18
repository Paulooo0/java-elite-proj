package pauloh.main.dto.loan;

import java.time.Instant;
import java.util.UUID;

public record CreateLoanDto(UUID userId, UUID bookId, Instant expectedDevolution) {

}
