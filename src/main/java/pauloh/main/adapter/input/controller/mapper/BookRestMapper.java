package pauloh.main.adapter.input.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pauloh.main.adapter.input.dto.book.BookRes;
import pauloh.main.adapter.input.dto.book.BookReq;
import pauloh.main.core.domain.model.Book;

@Mapper(componentModel = "spring")
public interface BookRestMapper {
  BookRestMapper INSTANCE = Mappers.getMapper(BookRestMapper.class);

  Book toDomain(BookReq book);

  BookRes toResponse(Book book);
}
