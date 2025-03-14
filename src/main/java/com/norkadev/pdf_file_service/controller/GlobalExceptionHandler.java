package com.norkadev.pdf_file_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Leverage Exception Handler framework for Input Output Exception.
     *
     * @param ex      IOException
     * @param request WebRequest
     * @return http response
     */
    @ExceptionHandler(IOException.class)
    public final ResponseEntity<Object> handleIOException(IOException ex, WebRequest request) {
        logException("You got a IOException",ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return  createResponseEntity(pd, null, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Leverage Exception Handler framework for issues processing the file.
     *
     * @param ex      IllegalArgumentException
     * @param request WebRequest
     * @return http response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        logException("You got a IllegalArgumentException",ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return  createResponseEntity(pd, null, HttpStatus.BAD_REQUEST, request);
    }

    private void logException(String message, Exception ex) {
        log.error(message, ex);
    }
}