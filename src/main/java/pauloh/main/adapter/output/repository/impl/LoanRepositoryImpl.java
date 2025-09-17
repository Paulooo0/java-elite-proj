package pauloh.main.adapter.output.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pauloh.main.adapter.input.dto.loan.LoanedBooksWithUsersReq;
import pauloh.main.adapter.output.repository.entity.LoanEntity;
import pauloh.main.adapter.output.repository.mapper.LoanPersistenceMapper;
import pauloh.main.core.domain.model.Loan;
import pauloh.main.port.output.LoanOutputPort;
import pauloh.main.port.output.LoanQueryPort;

@Repository
public class LoanRepositoryImpl implements LoanOutputPort, LoanQueryPort {
  private final JdbcTemplate jdbcTempl;
  private final LoanPersistenceMapper mapper;

  public LoanRepositoryImpl(JdbcTemplate jdbcTempl, LoanPersistenceMapper mapper) {
    this.jdbcTempl = jdbcTempl;
    this.mapper = mapper;
  }

  @Override
  public Loan save(Loan loan) {
    LoanEntity entity = mapper.toEntity(loan);

    String sql = "SELECT * FROM fn_upsert_loan(?, ?, ?, ?, ?)";

    LoanEntity persist = jdbcTempl.queryForObject(sql, (rs, rowNum) -> new LoanEntity(
        rs.getObject("id", UUID.class),
        rs.getObject("book_id", UUID.class),
        rs.getObject("user_id", UUID.class),
        rs.getTimestamp("taken_at").toInstant(),
        rs.getTimestamp("expected_devolution").toInstant(),
        rs.getTimestamp("devolved_at") != null ? rs.getTimestamp("devolved_at").toInstant() : null,
        rs.getTimestamp("updated_at").toInstant()),
        entity.getId(),
        entity.getBookId(),
        entity.getUserId(),
        entity.getTakenAt(),
        entity.getExpectedDevolution(),
        entity.getDevolvedAt());

    return mapper.toDomain(persist);
  }

  @Override
  public Optional<Loan> findById(UUID id) {
    String sql = "SELECT * FROM fn_find_loan_by_id(?)";

    List<LoanEntity> results = jdbcTempl.query(
        sql,
        (rs, rowNum) -> new LoanEntity(
            rs.getObject("id", UUID.class),
            rs.getObject("book_id", UUID.class),
            rs.getObject("user_id", UUID.class),
            rs.getTimestamp("taken_at").toInstant(),
            rs.getTimestamp("expected_devolution").toInstant(),
            rs.getTimestamp("devolved_at") != null ? rs.getTimestamp("devolved_at").toInstant() : null,
            rs.getTimestamp("updated_at").toInstant()),
        id);

    LoanEntity entity = results.isEmpty() ? null : results.get(0);
    return Optional.ofNullable(entity).map(mapper::toDomain);
  }

  @Override
  public List<Loan> findAllByUserId(UUID userId) {
    String sql = "SELECT * FROM fn_find_loans_by_user(?)";

    List<LoanEntity> entities = jdbcTempl.query(
        sql,
        (rs, rowNum) -> new LoanEntity(
            rs.getObject("id", UUID.class),
            rs.getObject("book_id", UUID.class),
            rs.getObject("user_id", UUID.class),
            rs.getTimestamp("taken_at").toInstant(),
            rs.getTimestamp("expected_devolution").toInstant(),
            rs.getTimestamp("devolved_at") != null ? rs.getTimestamp("devolved_at").toInstant() : null,
            rs.getTimestamp("updated_at").toInstant()),
        userId);

    return entities.stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public List<Loan> findAll() {
    String sql = "SELECT * FROM loans";
    List<LoanEntity> entities = jdbcTempl.query(sql, (rs, rowNum) -> new LoanEntity(
        rs.getObject("id", UUID.class),
        rs.getObject("book_id", UUID.class),
        rs.getObject("user_id", UUID.class),
        rs.getTimestamp("taken_at").toInstant(),
        rs.getTimestamp("expected_devolution").toInstant(),
        rs.getTimestamp("devolved_at").toInstant(),
        rs.getTimestamp("updated_at").toInstant()));

    return entities.stream().map(mapper::toDomain).toList();
  }

  @Override
  public Optional<Loan> findActiveLoan(UUID userId, UUID bookId) {
    String sql = "SELECT * FROM fn_find_active_loan(?, ?)";

    List<LoanEntity> entities = jdbcTempl.query(
        sql,
        (rs, rowNum) -> new LoanEntity(
            rs.getObject("id", UUID.class),
            rs.getObject("book_id", UUID.class),
            rs.getObject("user_id", UUID.class),
            rs.getTimestamp("taken_at") != null ? rs.getTimestamp("taken_at").toInstant() : null,
            rs.getTimestamp("expected_devolution") != null ? rs.getTimestamp("expected_devolution").toInstant() : null,
            rs.getTimestamp("devolved_at") != null ? rs.getTimestamp("devolved_at").toInstant() : null,
            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toInstant() : null),
        userId,
        bookId);

    return entities.stream().findFirst().map(mapper::toDomain);
  }

  @Override
  public List<Loan> findAllByDevolvedAtIsNull() {
    String sql = "SELECT * FROM fn_find_active_loans()";

    List<LoanEntity> entities = jdbcTempl.query(
        sql,
        (rs, rowNum) -> new LoanEntity(
            rs.getObject("id", UUID.class),
            rs.getObject("book_id", UUID.class),
            rs.getObject("user_id", UUID.class),
            rs.getTimestamp("taken_at").toInstant(),
            rs.getTimestamp("expected_devolution").toInstant(),
            rs.getTimestamp("devolved_at").toInstant(),
            rs.getTimestamp("updated_at").toInstant()));

    return entities.stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public List<LoanedBooksWithUsersReq> getAllActiveLoans() {
    String sql = "SELECT * FROM fn_get_all_active_loans()";

    return jdbcTempl.query(sql, (rs, rowNum) -> new LoanedBooksWithUsersReq(
        rs.getString("book_title"),
        rs.getString("book_author"),
        rs.getString("user_name"),
        rs.getString("user_email")));
  }

}
