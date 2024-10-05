package com.paras.FinMate.handler;

import com.paras.FinMate.common.Response;
import com.paras.FinMate.exceptions.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException (Exception exp) {
        log.error("Exception occurred: ", exp);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error("Internal server error", exp.getMessage()));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Response> handleException (CustomerNotFoundException exp) {
        log.error("Exception occurred: ", exp);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.error("Customer not found", exp.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response> handleException (DataIntegrityViolationException exp) {
//        log.error("Exception occurred: ", exp);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.error("Customer with email already exists.", "Email already exists"));
    }
}
