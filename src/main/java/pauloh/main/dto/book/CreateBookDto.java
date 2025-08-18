package pauloh.main.dto.book;

import java.util.UUID;

public record CreateBookDto(UUID userID, String isbn, String title, String author) {

}
