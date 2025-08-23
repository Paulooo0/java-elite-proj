package pauloh.main.adapter.output.repository.impl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;

import pauloh.main.core.domain.model.Users;
import pauloh.main.port.output.UserOutputPort;

public class UserRepositoryImpl implements UserOutputPort {
  private JdbcTemplate jdbcTempl;

  @Override
  public Users save(Users user) {
    String sql = """
        INSERT INTO users (id, email, name, created_at)
        VALUES (?, ?, ?, ?)
        ON CONFLICT (id) DO UPDATE
        SET email = EXCLUDED.email,
            name = EXCLUDED.name,
            updated_at = NOW()
        RETURNING id, email, name, created_at, updated_at
        """;

    return jdbcTempl.queryForObject(sql, (rs, rowNum) -> new Users(
        rs.getObject("id", UUID.class),
        rs.getString("email"),
        rs.getString("name"),
        rs.getObject("created_at", Instant.class),
        rs.getObject("updated_at", Instant.class)));
  }
}