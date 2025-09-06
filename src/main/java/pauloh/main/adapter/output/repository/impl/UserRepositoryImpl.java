package pauloh.main.adapter.output.repository.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pauloh.main.adapter.output.repository.entity.UserEntity;
import pauloh.main.adapter.output.repository.mapper.UserPersistenceMapper;
import pauloh.main.core.domain.model.Users;
import pauloh.main.port.output.UserOutputPort;

@Repository
public class UserRepositoryImpl implements UserOutputPort {
  private final JdbcTemplate jdbcTempl;
  private final UserPersistenceMapper mapper;

  public UserRepositoryImpl(JdbcTemplate jdbcTempl, UserPersistenceMapper mapper) {
    this.jdbcTempl = jdbcTempl;
    this.mapper = mapper;
  }

  @Override
  public Users save(Users user) {
    UUID id = user.getId() != null ? user.getId() : UUID.randomUUID();
    user.setId(id);

    UserEntity entity = mapper.toEntity(user);

    String sql = """
        INSERT INTO users (id, email, name, created_at)
        VALUES (?, ?, ?, NOW())
        ON CONFLICT (id) DO UPDATE
        SET email = EXCLUDED.email,
            name = EXCLUDED.name,
            updated_at = NOW()
        RETURNING id, email, name
        """;

    UserEntity persist = jdbcTempl.queryForObject(sql, (rs, rowNum) -> new UserEntity(
        rs.getObject("id", UUID.class),
        rs.getString("email"),
        rs.getString("name"),
        rs.getTimestamp("created_at").toInstant(),
        rs.getTimestamp("updated_at").toInstant()), entity.getId(), entity.getEmail(), entity.getName(),
        entity.getUpdatedAt());

    return mapper.toDomain(persist);
  }

  @Override
  public Optional<Users> findById(UUID id) {
    String sql = """
        SELECT id, name, email, created_at, updated_at
        FROM users
        WHERE id = ?
        """;

    try {
      UserEntity entity = jdbcTempl.queryForObject(sql, (rs, rowNum) -> new UserEntity(
          rs.getObject("id", UUID.class),
          rs.getString("name"),
          rs.getString("email"),
          rs.getTimestamp("created_at").toInstant(),
          rs.getTimestamp("updated_at").toInstant()), id);

      return Optional.ofNullable(mapper.toDomain(entity));
    } catch (Exception e) {
      throw new IllegalArgumentException("User not found with id: " + id, e);
    }
  }

  @Override
  public List<Users> findAll() {
    String sql = """
        SELECT id, email, name, created_at, updated_at
        FROM users
        """;

    return jdbcTempl.query(sql, (rs, rowNum) -> new UserEntity(
        rs.getObject("id", UUID.class),
        rs.getString("email"),
        rs.getString("name"),
        rs.getObject("created_at", Instant.class),
        rs.getObject("updated_at", Instant.class)))
        .stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public boolean existsByEmail(String email) {
    String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
    Integer count = jdbcTempl.queryForObject(sql, Integer.class);
    return count != null && count > 0;
  }

}