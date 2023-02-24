package com.example.bookservice.mappers;

import com.example.bookservice.dto.requests.GroupRequest;
import com.example.bookservice.dto.responses.GroupResponse;
import com.example.bookservice.entities.Group;
import com.example.bookservice.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface GroupMapper extends BaseDtoMapper<Group, GroupRequest, GroupResponse> {
  GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

  default Long toId(Group entity) {
    return entity.getId();
  }

  default List<Long> usersToIds(List<User> users) {
    ArrayList<Long> ids = new ArrayList<>();
    for(User user : users) {
      ids.add(user.getId());
    }
    return ids;
  }

  List<Long> toIds(List<Group> entities);

}
