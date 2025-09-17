package pauloh.main.adapter.output.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pauloh.main.adapter.output.repository.entity.BookEntity;
import pauloh.main.adapter.output.repository.mapper.BookPersistenceMapper;
import pauloh.main.core.domain.model.Book;
import pauloh.main.port.output.BookOutputPort;

@Repository
public class BookRepositoryImpl implements BookOutputPort {
  private final JdbcTemplate jdbcTempl;
  private final BookPersistenceMapper mapper;

  public BookRepositoryImpl(JdbcTemplate jdbcTempl, BookPersistenceMapper mapper) {
    this.jdbcTempl = jdbcTempl;
    this.mapper = mapper;
  }

  @Override
  public Book save(Book book) {
    BookEntity entity = mapper.toEntity(book);

    String sql = "SELECT * FROM fn_upsert_book(?, ?, ?, ?)";

    BookEntity persisted = jdbcTempl.queryForObject(
        sql,
        (rs, rowNum) -> new BookEntity(
            rs.getObject("id", UUID.class),
            rs.getString("isbn"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getInt("stock"),
            rs.getBoolean("is_active"),
            rs.getTimestamp("created_at").toInstant(),
            rs.getTimestamp("updated_at").toInstant()),
        entity.getIsbn(),
        entity.getTitle(),
        entity.getAuthor(),
        entity.getStock());

    return mapper.toDomain(persisted);
  }

  @Override
  public boolean existsByIsbn(String isbn) {
    String sql = "SELECT fn_exists_book_by_isbn(?)";
    Boolean exists = jdbcTempl.queryForObject(sql, Boolean.class, isbn);
    return Boolean.TRUE.equals(exists);
  }

  @Override
  public Optional<Book> findById(UUID id) {
    String sql = "SELECT * FROM fn_find_book_by_id(?)";

    try {
      BookEntity entity = jdbcTempl.queryForObject(
          sql,
          (rs, rowNum) -> new BookEntity(
              rs.getObject("id", UUID.class),
              rs.getString("isbn"),
              rs.getString("title"),
              rs.getString("author"),
              rs.getInt("stock"),
              rs.getBoolean("is_active"),
              rs.getTimestamp("created_at").toInstant(),
              rs.getTimestamp("updated_at").toInstant()),
          id);

      return Optional.ofNullable(mapper.toDomain(entity));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Book> findByIsbn(String isbn) {
    String sql = "SELECT * FROM fn_find_book_by_isbn(?)";

    try {
      BookEntity entity = jdbcTempl.queryForObject(sql, (rs, rowNum) -> new BookEntity(
          rs.getObject("id", UUID.class),
          rs.getString("isbn"),
          rs.getString("title"),
          rs.getString("author"),
          rs.getInt("stock"),
          rs.getBoolean("is_active"),
          rs.getTimestamp("created_at").toInstant(),
          rs.getTimestamp("updated_at").toInstant()), isbn);

      return Optional.ofNullable(mapper.toDomain(entity));
    } catch (Exception e) {
      throw new IllegalArgumentException("Book not found with ISBN: " + isbn, e);
    }
  }

  @Override
  public List<Book> findAllByTitle(String title) {
    String sql = "SELECT * FROM fn_find_books_by_title(?)";

    List<BookEntity> entities = jdbcTempl.query(sql, (rs, rowNum) -> new BookEntity(
        rs.getObject("id", UUID.class),
        rs.getString("isbn"),
        rs.getString("title"),
        rs.getString("author"),
        rs.getInt("stock"),
        rs.getBoolean("is_active"),
        rs.getTimestamp("created_at").toInstant(),
        rs.getTimestamp("updated_at").toInstant()), title);

    return entities.stream().map(mapper::toDomain).toList();
  }

}
