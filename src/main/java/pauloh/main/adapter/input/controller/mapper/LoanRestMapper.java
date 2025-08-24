package pauloh.main.adapter.input.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pauloh.main.adapter.input.dto.loan.LoanReq;
import pauloh.main.adapter.input.dto.loan.LoanRes;
import pauloh.main.core.domain.model.Loan;

@Mapper(componentModel = "spring")
public interface LoanRestMapper {
  LoanRestMapper INSTANCE = Mappers.getMapper(LoanRestMapper.class);

  Loan toDomain(LoanReq loan);

  LoanRes toRespose(Loan loan);
}
