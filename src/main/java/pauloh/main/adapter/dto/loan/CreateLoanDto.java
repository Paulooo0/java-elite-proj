package pauloh.main.adapter.dto.loan;

import java.util.UUID;

public record CreateLoanDto(UUID userId, UUID bookId) {

}
