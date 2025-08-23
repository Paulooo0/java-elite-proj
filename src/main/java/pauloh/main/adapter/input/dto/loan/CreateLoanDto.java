package pauloh.main.adapter.input.dto.loan;

import java.util.UUID;

public record CreateLoanDto(UUID userId, UUID bookId) {

}
