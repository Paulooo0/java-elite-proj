package pauloh.main.adapter.dto.book;

import java.util.UUID;

public record UpdateBookDto(UUID bookId, String isbn, String title, String author) {

}
