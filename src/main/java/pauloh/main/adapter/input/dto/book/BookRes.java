package pauloh.main.adapter.input.dto.book;

import java.time.Instant;
import java.util.UUID;

public record BookRes(UUID id, String isbn, String title, String author, Integer stock, Instant updatedAt) {

}
