package pauloh.main.adapter.input.dto.book;

import java.util.UUID;

public record BookStockReq(UUID bookId, Integer currentStock) {
}
