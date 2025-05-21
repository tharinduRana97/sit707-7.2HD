package com.tavisca.api.book.exception;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import com.tavisca.api.book.constants.BookErrors;

@Getter
public class InvalidBorrowRequestException extends RuntimeException {

    private final HttpStatus code;

    public InvalidBorrowRequestException(BookErrors message, HttpStatus code) {
        super(message.getErrorMessage());
        this.code = code;
    }
}

