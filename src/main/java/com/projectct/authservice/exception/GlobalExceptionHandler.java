package com.projectct.authservice.exception;

import com.projectct.authservice.DTO.RespondData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
//        var error = RespondData.builder()
//                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .desc("Internal server error!")
//                .build();
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException e) {
        var error = RespondData.builder()
                .status(e.getStatus().value())
                .desc(e.getMessage())
                .build();
        return new ResponseEntity<>(error, e.getStatus());
    }
}