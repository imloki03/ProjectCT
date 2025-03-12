package com.projectct.projectservice.exception;

import com.projectct.projectservice.DTO.RespondData;
import com.projectct.projectservice.util.MessageUtil;
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