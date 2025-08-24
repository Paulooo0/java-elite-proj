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
    UUID id = loan.getId() != null ? loan.getId() : UUID.randomUUID();
    loan.setId(id);

    LoanEntity entity = mapper.toEntity(loan);

    String sql = """
        INSERT INTO users (id, bookId, userId, takenAt, expectedDevolution, devolvedAt, created_at)
        VALUES (?, ?, ?, ?, ?, ?, NOW())
        ON CONFLICT (id) DO UPDATE
        SET bookId = EXCLUDED.bookId,
            userId = EXCLUDED.userId,
            takenAt = EXCLUDED.takenAt,
            expectedDevolution = EXCLUDED.expectedDevolution,
            devolvedAt = EXCLUDED.devolvedAt,
            updated_at = NOW()
        RETURNING id, bookId, userId, takenAt, expectedDevolution, devolvedAt, updated_at
        """;

    LoanEntity persist = jdbcTempl.queryForObject(sql, (rs, rowNum) -> new LoanEntity(
        rs.getObject("id", UUID.class),
        rs.getObject("bookId", UUID.class),
        rs.getObject("userId", UUID.class),
        rs.getTimestamp("takenAt").toInstant(),
        rs.getTimestamp("expectedDevolution").toInstant(),
        rs.getTimestamp("devolvedAt").toInstant(),
        rs.getTimestamp("updated_at").toInstant()), entity.getId(), entity.getBookId(),
        entity.getUserId(), entity.getTakenAt(), entity.getExpectedDevolution(), entity.getDevolvedAt(),
        entity.getUpdatedAt());

    return mapper.toDomain(persist);
  }

  @Override
  public Optional<Loan> findById(UUID id) {
    String sql = "SELECT * FROM loans WHERE id = ?";
    LoanEntity entity = jdbcTempl.queryForObject(sql, (rs, rowNum) -> new LoanEntity(
        rs.getObject("id", UUID.class),
        rs.getObject("bookId", UUID.class),
        rs.getObject("userId", UUID.class),
        rs.getTimestamp("takenAt").toInstant(),
        rs.getTimestamp("expectedDevolution").toInstant(),
        rs.getTimestamp("devolvedAt").toInstant(),
        rs.getTimestamp("updated_at").toInstant()), id);

    return Optional.ofNullable(entity).map(mapper::toDomain);
  }

  @Override
  public List<Loan> findAllByUserId(UUID userId) {
    String sql = "SELECT * FROM loans WHERE userId = ?";
    List<LoanEntity> entities = jdbcTempl.query(sql, (rs, rowNum) -> new LoanEntity(
        rs.getObject("id", UUID.class),
        rs.getObject("bookId", UUID.class),
        rs.getObject("userId", UUID.class),
        rs.getTimestamp("takenAt").toInstant(),
        rs.getTimestamp("expectedDevolution").toInstant(),
        rs.getTimestamp("devolvedAt").toInstant(),
        rs.getTimestamp("updated_at").toInstant()), userId);

    try {
      return entities.stream()
          .map(mapper::toDomain)
          .toList();
    } catch (Exception e) {
      throw new RuntimeException("Error mapping LoanEntity to Loan", e);
    }
  }

  @Override
  public List<Loan> findAll() {
    String sql = "SELECT * FROM loans";
    List<LoanEntity> entities = jdbcTempl.query(sql, (rs, rowNum) -> new LoanEntity(
        rs.getObject("id", UUID.class),
        rs.getObject("bookId", UUID.class),
        rs.getObject("userId", UUID.class),
        rs.getTimestamp("takenAt").toInstant(),
        rs.getTimestamp("expectedDevolution").toInstant(),
        rs.getTimestamp("devolvedAt").toInstant(),
        rs.getTimestamp("updated_at").toInstant()));

    return entities.stream().map(mapper::toDomain).toList();
  }

  @Override
  public Optional<Loan> findActiveLoan(UUID userId, UUID bookid) {
    String sql = """
        SELECT * FROM loans WHERE userId = ? AND bookId = ?
        AND takenAt IS NOT NULL AND devolvedAt IS NULL
        """;

    LoanEntity entity = jdbcTempl.queryForObject(sql, (rs, rowNum) -> new LoanEntity(
        rs.getObject("id", UUID.class),
        rs.getObject("bookId", UUID.class),
        rs.getObject("userId", UUID.class),
        rs.getTimestamp("takenAt").toInstant(),
        rs.getTimestamp("expectedDevolution").toInstant(),
        rs.getTimestamp("devolvedAt").toInstant(),
        rs.getTimestamp("updated_at").toInstant()), userId, bookid);

    return Optional.ofNullable(entity).map(mapper::toDomain);
  }

  @Override
  public List<Loan> findAllByDevolvedAtIsNull() {
    String sql = "SELECT * FROM loans WHERE devolvedAt IS NULL";
    List<LoanEntity> entities = jdbcTempl.query(sql, (rs, rowNum) -> new LoanEntity(
        rs.getObject("id", UUID.class),
        rs.getObject("bookId", UUID.class),
        rs.getObject("userId", UUID.class),
        rs.getTimestamp("takenAt").toInstant(),
        rs.getTimestamp("expectedDevolution").toInstant(),
        rs.getTimestamp("devolvedAt").toInstant(),
        rs.getTimestamp("updated_at").toInstant()));

    return entities.stream().map(mapper::toDomain).toList();
  }

  @Override
  public List<LoanedBooksWithUsersReq> getAllActiveLoans() {
    String sql = """
        SELECT l.id, l.bookId, l.userId, l.takenAt, l.expectedDevolution, l.devolvedAt,
               b.title AS bookTitle, b.author AS bookAuthor,
               u.name AS userName, u.email AS userEmail
        FROM loans l
        JOIN books b ON l.bookId = b.id
        JOIN users u ON l.userId = u.id
        WHERE l.devolvedAt IS NULL
        """;

    return jdbcTempl.query(sql, (rs, rowNum) -> new LoanedBooksWithUsersReq(
        rs.getString("bookTitle"),
        rs.getString("bookAuthor"),
        rs.getString("userName"),
        rs.getString("userEmail")));
  }
}
