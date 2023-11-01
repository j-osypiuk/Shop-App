package com.example.shopapp.error.advice;

import com.example.shopapp.error.exception.InvalidPasswordException;
import com.example.shopapp.error.exception.InvalidStateException;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
        errorMap.put("Error Message", e.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, String> handleMessageNotReadable(HttpMessageNotReadableException e) {
        Map<String, String> errorMap = new HashMap<>();
        if (e.getMessage().contains("[FEMALE, MALE]"))
            errorMap.put("Error Message", "Gender must match pattern: MALE|FEMALE");
        else
            errorMap.put("Error Message", e.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidStateException.class)
    public Map<String, String> handleInvalidState(InvalidStateException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error Message", e.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPasswordException.class)
    public Map<String, String> handleInvalidPassword(InvalidPasswordException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error Message", e.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Map<String, String> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error Message", e.getMessage());
        return errorMap;
    }

}
