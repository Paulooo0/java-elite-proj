package pauloh.main.adapter.input.dto.book;

import java.util.UUID;

public record BookResponseDto(UUID id, String isbn, String title, String author, Integer stock) {

}
