package org.hyoj.mysbbp.mapper;

import org.hyoj.mysbbp.dto.UserDto;
import org.hyoj.mysbbp.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    UserDto.UserInfoDto Users2UserInfoDto(Users users);
    List<UserDto.UserInfoDto> Users2UserInfoDto(List<Users> users);

}
