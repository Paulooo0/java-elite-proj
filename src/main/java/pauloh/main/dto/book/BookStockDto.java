package pauloh.main.dto.book;

import java.util.UUID;

public record BookStockDto(UUID bookId, Integer currentStock) {
}
