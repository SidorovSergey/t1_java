package ru.t1.java.demo.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.t1.java.demo.dto.ErrorResDto;
import ru.t1.java.demo.exception.AccountException;
import ru.t1.java.demo.exception.TransactionException;

import java.util.Optional;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResDto> noHandlerFound(NoHandlerFoundException ex) {
        log.warn("No handler found: exceptionMsg=[{}]", ex.toString());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResDto(ex.getBody().getTitle()));
    }

    @ExceptionHandler
    public ResponseEntity<String> formatException(ConstraintViolationException ex) {
        log.warn(String.format("Bad request: exceptionMsg=[%s]", ex.getMessage()));
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> httpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn("Request method not supported: exceptionMsg=[{}]", ex.toString());

        HttpHeaders headers = new HttpHeaders();
        Optional.ofNullable(ex.getSupportedHttpMethods())
                .ifPresent(headers::setAllow);

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Request method not supported");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> httpRequestParamMissed(MissingServletRequestParameterException ex) {
        log.warn(String.format("Missing required request parameter: message=[%s]", ex.getMessage()));
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body("Missing required request parameter");
    }

    @ExceptionHandler({HttpMediaTypeException.class})
    public ResponseEntity<String> httpContentTypeNotSupported(HttpMediaTypeException ex) {
        log.warn("Content type not supported: exceptionMsg=[{}]", ex.toString());
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body("Media type not supported, use 'application/json'");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> httpMessageNotReadableHandler(HttpMessageNotReadableException ex) {
        log.warn("Failed to parse request: exceptionMsg=[{}]", ex.toString());
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body("Request should contain valid JSON in UTF-8 encoding");
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResDto> handeException(AccountException ex) {
        return getResponse(ex);
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ErrorResDto> handeException(TransactionException ex) {
        return getResponse(ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> anyOther(Exception ex) {
        log.error("Internal server error: exceptionMsg=[{}]", ex.toString(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Unexpected error");
    }

    @NonNull
    private ResponseEntity<ErrorResDto> getResponse(@NonNull RuntimeException ex) {
        log.warn("Unsuccessful result: exceptionMsg=[{}]", ex.toString());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResDto(ex.getMessage()));
    }
}
