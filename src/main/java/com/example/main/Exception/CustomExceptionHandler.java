package com.example.main.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("msg", ex.getReason());
        response.put("statusCode", ex.getStatusCode().value());
        response.put("status", "failure");
        return new ResponseEntity<>(response, ex.getStatusCode());
    }
      @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("msg", ex.getMessage());
        response.put("status", "failure");
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Mismatch type of parameter");
        errorDetails.put("status", "failure");
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("statusCode", HttpStatus.NOT_FOUND.value());
        //    logger.error("error accured", errorDetails);
        return new ResponseEntity<>(errorDetails, HttpStatus.OK);
    }
}
