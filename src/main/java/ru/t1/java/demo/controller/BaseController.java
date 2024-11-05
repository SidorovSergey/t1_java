package ru.t1.java.demo.controller;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON;

abstract class BaseController {

    @NonNull
    protected <T> ResponseEntity<T> getResponse(@NonNull T response) {
        return ResponseEntity
                .ok()
                .contentType(APPLICATION_JSON)
                .body(response);
    }

}
