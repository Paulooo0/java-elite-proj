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
    UUID id = book.getId() != null ? book.getId() : UUID.randomUUID();
    book.setId(id);

    BookEntity entity = mapper.toEntity(book);

    String sql = """
        INSERT INTO users (id, isbn, title, author, stock, is_active, created_at)
        VALUES (?, ?, ?, ?, ?, ?, NOW())
        ON CONFLICT (id) DO UPDATE
        SET isbn = EXCLUDED.isbn,
            title = EXCLUDED.title,
            author = EXCLUDED.author,
            stock = EXCLUDED.stock,
            is_active = EXCLUDED.is_active,
            updated_at = NOW()
        RETURNING id, email, name
        """;

    BookEntity persist = jdbcTempl.queryForObject(sql, (rs, rowNum) -> new BookEntity(
        rs.getObject("id", UUID.class),
        rs.getString("isbn"),
        rs.getString("title"),
        rs.getString("author"),
        rs.getInt("stock"),
        rs.getBoolean("is_active"),
        rs.getTimestamp("created_at").toInstant(),
        rs.getTimestamp("updated_at").toInstant()), entity.getId(), entity.getIsbn(), entity.getAuthor(),
        entity.getStock(), entity.getUpdatedAt());

    return mapper.toDomain(persist);
  }

  @Override
  public boolean existsByIsbn(String isbn) {
    String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
    Integer count = jdbcTempl.queryForObject(sql, Integer.class, isbn);
    return count != null && count > 0;
  }

  @Override
  public Optional<Book> findById(UUID id) {
    String sql = """
        SELECT id, isbn, title, author, stock, is_active, created_at, updated_at
        FROM books
        WHERE id = ?
        """;

    try {
      BookEntity entity = jdbcTempl.queryForObject(sql, (rs, rowNum) -> new BookEntity(
          rs.getObject("id", UUID.class),
          rs.getString("isbn"),
          rs.getString("title"),
          rs.getString("author"),
          rs.getInt("stock"),
          rs.getBoolean("is_active"),
          rs.getTimestamp("created_at").toInstant(),
          rs.getTimestamp("updated_at").toInstant()), id);

      return Optional.ofNullable(mapper.toDomain(entity));
    } catch (Exception e) {
      throw new IllegalArgumentException("Book not found with id: " + id, e);
    }
  }

  @Override
  public Optional<Book> findByTitle(String title) {
    String sql = """
        SELECT id, isbn, title, author, stock, is_active, created_at, updated_at
        FROM books
        WHERE title = ?
        """;

    try {
      BookEntity entity = jdbcTempl.queryForObject(sql, (rs, rowNum) -> new BookEntity(
          rs.getObject("id", UUID.class),
          rs.getString("isbn"),
          rs.getString("title"),
          rs.getString("author"),
          rs.getInt("stock"),
          rs.getBoolean("is_active"),
          rs.getTimestamp("created_at").toInstant(),
          rs.getTimestamp("updated_at").toInstant()), title);

      return Optional.ofNullable(mapper.toDomain(entity));
    } catch (Exception e) {
      throw new IllegalArgumentException("Book not found with title: " + title, e);
    }
  }

  @Override
  public Optional<Book> findByIsbn(String isbn) {
    String sql = """
        SELECT id, isbn, title, author, stock, is_active, created_at, updated_at
        FROM books
        WHERE isbn = ?
        """;

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
    String sql = """
        SELECT id, isbn, title, author, stock, is_active, created_at, updated_at
        FROM books
        WHERE title = ?
        """;

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
