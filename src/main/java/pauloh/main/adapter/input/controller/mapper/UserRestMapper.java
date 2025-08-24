package pauloh.main.adapter.input.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pauloh.main.adapter.input.dto.user.UserReq;
import pauloh.main.adapter.input.dto.user.UserRes;
import pauloh.main.core.domain.model.Users;

@Mapper(componentModel = "spring")
public interface UserRestMapper {
  UserRestMapper INSTANCE = Mappers.getMapper(UserRestMapper.class);

  Users toDomain(UserReq user);

  UserRes toResponse(Users user);
}
