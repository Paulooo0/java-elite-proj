package pauloh.main.port.output;

import java.util.List;

import pauloh.main.adapter.input.dto.loan.LoanedBooksWithUsersReq;

public interface LoanQueryPort {

  List<LoanedBooksWithUsersReq> getAllActiveLoans();
}
