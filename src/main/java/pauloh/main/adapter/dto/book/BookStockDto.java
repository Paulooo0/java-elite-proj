package pauloh.main.adapter.dto.book;

import java.util.UUID;

public record BookStockDto(UUID bookId, Integer currentStock) {
}
