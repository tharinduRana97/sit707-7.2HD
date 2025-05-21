package com.tavisca.api.book.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tavisca.api.book.POJO.ApiResponse;
import com.tavisca.api.book.POJO.ApiResponseError;

@RestControllerAdvice
@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAllExceptions(Exception e) {
        return new ResponseEntity<>(
            new ApiResponseError("Internal Server Error: " + e.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(InvalidBorrowRequestException.class)
    public ResponseEntity<ApiResponse> handleBorrowException(InvalidBorrowRequestException e) {
        return new ResponseEntity<>(
            new ApiResponseError(e.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }
}
