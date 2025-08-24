package pauloh.main.adapter.output.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pauloh.main.adapter.output.repository.entity.BookEntity;
import pauloh.main.core.domain.model.Book;

@Mapper(componentModel = "spring")
public interface BookPersistenceMapper {
  BookPersistenceMapper INSTANCE = Mappers.getMapper(BookPersistenceMapper.class);

  BookEntity toEntity(Book book);

  Book toDomain(BookEntity bookEntity);

}
