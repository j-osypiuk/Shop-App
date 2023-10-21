package com.example.shopapp.error.advice;

import com.example.shopapp.error.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                error -> errorMap.put(error.getField(), error.getDefaultMessage())
        );
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public Map<String, String> handleObjectNotFound(ObjectNotFoundException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("ErrorMessage", e.getMessage());
        return errorMap;
    }

}
