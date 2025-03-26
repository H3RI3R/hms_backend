package com.example.main.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseClass {

    public static ResponseEntity<Map<String, Object>> responseSuccess(String message) {
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("message", message);
        successResponse.put("status", "success");
        return ResponseEntity.ok(successResponse);
    }

    public static ResponseEntity<Map<String, Object>> responseSuccess(String msg, String inputType, Object inputdata) {
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("status", "success");
        successResponse.put("message", msg);
        successResponse.put(inputType, inputdata);
        return ResponseEntity.ok(successResponse);
    }


    public static ResponseEntity<Map<String, Object>> responseFailure(String message) {
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("message", message);
        successResponse.put("status", "failure");
        return ResponseEntity.ok(successResponse);
    }

    public static ResponseEntity<Map<String, Object>> responseFailure(String message,String inputType, Object inputdata) {
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("status", "failure");
        successResponse.put("message", message);
        successResponse.put(inputType, inputdata);
        return ResponseEntity.ok(successResponse);
    }

    public static ResponseEntity<Map<String, Object>> internalServer(String message) {
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("error", message);
        successResponse.put("success", false);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(successResponse);
    }
}