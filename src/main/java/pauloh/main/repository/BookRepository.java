package pauloh.main.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pauloh.main.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
  boolean existsByIsbn(String isbn);

  Optional<Book> findById(UUID id);

  Optional<Book> findByTitle(String title);

  List<Book> findAllByTitle(String title);
}
