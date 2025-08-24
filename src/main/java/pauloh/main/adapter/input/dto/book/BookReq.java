package pauloh.main.adapter.input.dto.book;

import java.util.UUID;

public record BookReq(UUID bookId, String isbn, String title, String author) {

}
