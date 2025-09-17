package pauloh.main.adapter.output.repository.impl;

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
    String sql = "SELECT * FROM fn_upsert_user(?, ?, ?)";

    UserEntity entity = jdbcTempl.queryForObject(sql, (rs, rowNum) -> new UserEntity(
        rs.getObject("id", UUID.class),
        rs.getString("name"),
        rs.getString("email"),
        rs.getTimestamp("created_at").toInstant(),
        rs.getTimestamp("updated_at").toInstant()),
        user.getId(), user.getName(), user.getEmail());

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<Users> findById(UUID id) {
    String sql = "SELECT * FROM fn_find_user_by_id(?)";

    try {
      UserEntity entity = jdbcTempl.queryForObject(sql, (rs, rowNum) -> new UserEntity(
          rs.getObject("id", UUID.class),
          rs.getString("name"),
          rs.getString("email"),
          rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toInstant() : null,
          rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toInstant() : null), id);

      return Optional.ofNullable(mapper.toDomain(entity));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Users> findAll() {
    String sql = "SELECT * FROM fn_get_all_users()";

    List<UserEntity> entities = jdbcTempl.query(sql, (rs, rowNum) -> new UserEntity(
        rs.getObject("id", UUID.class),
        rs.getString("name"),
        rs.getString("email"),
        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toInstant() : null,
        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toInstant() : null));

    return entities.stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public boolean existsByEmail(String email) {
    String sql = "SELECT * FROM fn_exists_user_by_email(?)";
    Boolean exists = jdbcTempl.queryForObject(sql, Boolean.class, email);
    return Boolean.TRUE.equals(exists);
  }

}