package pauloh.main.adapter.input.dto.book;

import java.util.UUID;

public record BookStockDto(UUID bookId, Integer currentStock) {
}
