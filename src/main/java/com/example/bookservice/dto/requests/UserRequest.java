package com.example.bookservice.dto.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserRequest {

  private String username;
  
  private String email;

  private String password;
}
