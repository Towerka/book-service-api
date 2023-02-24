package com.example.bookservice.mappers;

import com.example.bookservice.dto.requests.UserRequest;
import com.example.bookservice.dto.responses.UserResponse;
import com.example.bookservice.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserMapper extends BaseDtoMapper<User, UserRequest, UserResponse> {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  default Long toId(User entity) {
    return entity.getId();
  }

  List<Long> toIds(List<User> entities);

}
