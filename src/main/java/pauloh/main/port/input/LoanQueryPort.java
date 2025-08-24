package pauloh.main.port.input;

import java.util.List;

import pauloh.main.adapter.input.dto.loan.LoanedBooksWithUsersReq;

public interface LoanQueryPort {

  List<LoanedBooksWithUsersReq> getAllActiveLoans();
}
