package com.example.bookservice.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MakeResponse {
    public static <T> ResponseEntity<T> makeOkResponse(T body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public static <T> ResponseEntity<T> makeConflictResponse(T body) {
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}

