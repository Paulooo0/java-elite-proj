package pauloh.main.adapter.input.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pauloh.main.adapter.input.dto.user.CreateUserReq;
import pauloh.main.adapter.input.dto.user.UserRes;
import pauloh.main.core.domain.model.Users;

@Mapper(componentModel = "spring")
public interface UserRestMapper {
  UserRestMapper INSTANCE = Mappers.getMapper(UserRestMapper.class);

  Users toDomain(CreateUserReq user);

  UserRes toResponse(Users user);
}
