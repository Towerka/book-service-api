package com.example.bookservice.controller;

import com.example.bookservice.dto.requests.GroupRequest;
import com.example.bookservice.dto.responses.GroupResponse;
import com.example.bookservice.dto.responses.UserResponse;
import com.example.bookservice.entities.User;
import com.example.bookservice.services.GroupService;

import com.example.bookservice.services.JwtUserDetailsService;
import com.example.bookservice.utils.MakeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/groups")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class GroupController {

    private final long DEFAULT_GROUP_IMAGE_ID = 7L;

    @Autowired
    GroupService groupService;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @GetMapping("")
    public ResponseEntity<List<GroupResponse>> getGroups(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset) {

        final List<GroupResponse> groups = limit == null
                ? groupService.getGroups()
                : groupService.getGroups(limit, offset);

        return ResponseEntity.status(HttpStatus.OK).body(groups);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable ("groupId") Long groupId) {
        GroupResponse group = groupService.getGroup(groupId);
        return MakeResponse.makeOkResponse(group);
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<List<UserResponse>> getUsers(@PathVariable("groupId") Long groupId) {
        final List<UserResponse> users = groupService.getUsersForGroup(groupId);

        return MakeResponse.makeOkResponse(users);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GroupResponse>> getGroupsByName(@RequestParam(name = "name") String groupName){

        List<GroupResponse> groups = groupService.getGroupsByName(groupName);

        return MakeResponse.makeOkResponse(groups);
    }

    @GetMapping("/search/startsWith")
    public ResponseEntity<List<GroupResponse>> getGroupByNameContaining(@RequestParam(name = "letters") String letters){

        List<GroupResponse> groups = groupService.getGroupByNameContaining(letters);

        return MakeResponse.makeOkResponse(groups);
    }

    @PostMapping("")
    public ResponseEntity<String> createGroup(
            @RequestBody GroupRequest groupRequest,
            @RequestHeader HttpHeaders headers) {

        try {
            final User user = jwtUserDetailsService.getUserFromHeaders(headers);

            groupService.createGroup(groupRequest, user);

            return MakeResponse.makeOkResponse("successfully saved group " + groupRequest.getName());
        } catch (Exception e) {
            return MakeResponse.makeConflictResponse("failed saving group " + groupRequest.getName());
        }
    }

    @PostMapping(value = "/{group_id}/join")
    public ResponseEntity<String> joinGroup(
            @PathVariable("group_id") Long groupId,
            @RequestHeader HttpHeaders headers) {

        try {
            User user = jwtUserDetailsService.getUserFromHeaders(headers);

            groupService.joinGroup(groupId, user);

            return MakeResponse.makeOkResponse("successfully joined group");
        } catch (Exception e) {
            return MakeResponse.makeConflictResponse(e.getMessage());
        }
    }

    @PostMapping("/{group_id}/invite/{user_id}")
    public ResponseEntity<String> inviteUserToGroup(
            @PathVariable("group_id") Long groupId,
            @PathVariable("user_id") Long userId) {

        try {
            groupService.inviteUserToGroup(groupId, userId);

            return MakeResponse.makeOkResponse("invitation sent");
        } catch (Exception e) {
            return MakeResponse.makeConflictResponse(e.getMessage());
        }
    }
}
