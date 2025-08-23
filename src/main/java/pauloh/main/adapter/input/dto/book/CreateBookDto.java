package pauloh.main.adapter.input.dto.book;

import java.util.UUID;

public record CreateBookDto(UUID userID, String isbn, String title, String author) {

}
