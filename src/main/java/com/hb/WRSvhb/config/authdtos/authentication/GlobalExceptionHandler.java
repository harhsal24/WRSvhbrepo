package com.hb.WRSvhb.config.authdtos.authentication;

import com.hb.WRSvhb.config.authdtos.exceptions.AppException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<String> handleAppException(AppException ex) {
        // Customize the response as needed
        HttpStatus status = ex.getStatus();
        String errorMessage = ex.getMessage();

        return new ResponseEntity<>(errorMessage, status);
    }

    // Add more exception handlers for other exceptions...
}
