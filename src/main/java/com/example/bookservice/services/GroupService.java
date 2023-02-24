package com.example.bookservice.services;

import com.example.bookservice.dto.requests.GroupRequest;
import com.example.bookservice.dto.responses.GroupResponse;
import com.example.bookservice.dto.responses.UserResponse;
import com.example.bookservice.entities.Group;
import com.example.bookservice.entities.User;
import com.example.bookservice.mappers.GroupMapper;
import com.example.bookservice.mappers.UserMapper;
import com.example.bookservice.repositories.GroupRepo;
import com.example.bookservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    GroupRepo groupRepo;

    @Autowired
    UserRepository userRepo;


    public List<GroupResponse> getGroups() {
        final int defaultLimit = 100;
        final int defaultOffset = 0;

        return this.getGroups(defaultLimit, defaultOffset);
    }

    public List<GroupResponse> getGroups(int limit, int offset) {
        Page<Group> groupsPage = groupRepo.findAll(
                PageRequest.of(offset, limit, Sort.by(Sort.Order.asc("id"))));

        List<Group> groups = groupsPage.toList();

        return GroupMapper.INSTANCE.toDtos(groups);
    }

    public GroupResponse getGroup(long id) {

        Group group = groupRepo.getReferenceById(id);

        return GroupMapper.INSTANCE.toDto(group);
    }

    public List<GroupResponse> getGroupsByName(String name) {

        List<Group> groups = groupRepo.getGroupsByName(name);

        return GroupMapper.INSTANCE.toDtos(groups);
    }

    public List<GroupResponse> getGroupByNameContaining(String name) {

        List<Group> groups = groupRepo.findByNameContaining(name);

        return GroupMapper.INSTANCE.toDtos(groups);
    }

    public List<UserResponse> getUsersForGroup(Long groupId) {
        final int defaultLimit = 100;
        final int defaultOffset = 0;

        return this.getUsersForGroup(groupId, defaultLimit, defaultOffset);
    }

    public List<UserResponse> getUsersForGroup(Long groupId, int limit, int offset) {

        Group group = groupRepo.getReferenceById(groupId);

        List<User> users = group.getUsers();

        return UserMapper.INSTANCE.toDtos(users);
    }

    public void joinGroup(Long groupId, User user) throws Exception {

        Group group = groupRepo.getReferenceById(groupId);

        if (group.containsUser(user))
            throw new Exception("user already in the group");

        addUserToGroup(group, user);
    }

    public void inviteUserToGroup(long groupId, long userId) throws Exception {

        User user = userRepo.getReferenceById(userId);

        Group group = groupRepo.getReferenceById(groupId);

        if (group.containsUser(user))
            throw new Exception("user already in the group");

        addUserToGroup(group, user);
    }

    public void createGroup(GroupRequest groupRequest, User owner) {

        String groupName = groupRequest.getName();

        Group group = new Group(groupName, owner);

        addUserToGroup(group, owner);
    }

    public void addUserToGroup(Group group, User user) {
        group.addUser(user);

        groupRepo.save(group);
        userRepo.save(user);
    }


}
