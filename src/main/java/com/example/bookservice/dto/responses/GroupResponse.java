package com.example.bookservice.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class GroupResponse {

  private Long id;

  private String name;

  private List<Long> users;

}
