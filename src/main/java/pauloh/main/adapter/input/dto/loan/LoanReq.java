package pauloh.main.adapter.input.dto.loan;

import java.util.UUID;

public record LoanReq(UUID userId, UUID bookId) {

}
