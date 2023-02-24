package com.example.bookservice.dto.responses;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private final String jwttoken;

    private final String tip;

    public JwtResponse(String jwttoken, String tip) {
        this.jwttoken = jwttoken;
        this.tip = tip;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public String getTip() {
        return tip;
    }
}
