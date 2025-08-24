package pauloh.main.adapter.output.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pauloh.main.adapter.output.repository.entity.LoanEntity;
import pauloh.main.core.domain.model.Loan;

@Mapper(componentModel = "spring")
public interface LoanPersistenceMapper {
  LoanPersistenceMapper INSTANCE = Mappers.getMapper(LoanPersistenceMapper.class);

  LoanEntity toEntity(Loan loan);

  Loan toDomain(LoanEntity loan);
}
