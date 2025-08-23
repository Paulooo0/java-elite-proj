package pauloh.main.adapter.output.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pauloh.main.adapter.output.repository.entity.UserEntity;
import pauloh.main.core.domain.model.Users;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
  UserPersistenceMapper INSTANCE = Mappers.getMapper(UserPersistenceMapper.class);

  UserEntity toEntity(Users user);

  Users toDomain(UserEntity userEntity);
}
