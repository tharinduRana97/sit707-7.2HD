package com.tavisca.api.book.service;

import org.springframework.stereotype.Service;

import com.tavisca.api.book.POJO.ApiResponse;
import com.tavisca.api.book.POJO.ApiResponseError;
import com.tavisca.api.book.POJO.ApiResponseSuccess;
import com.tavisca.api.book.POJO.Book;
import com.tavisca.api.book.POJO.BorrowRequest;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookService {

    private final Map<Integer, Book> bookRepo = new HashMap<>();
    
    public ApiResponse addBook(Book book) {
        if (book == null) {
            return new ApiResponseError("Book cannot be null");
        }

        if (book.getId() <= 0) {
            return new ApiResponseError("Invalid book ID");
        }

        if (bookRepo.containsKey(book.getId())) {
            return new ApiResponseError("Book with this ID already exists");
        }

        if (isNullOrBlank(book.getTitle()) || isNullOrBlank(book.getAuthor())) {
            return new ApiResponseError("Title and author must not be empty");
        }

        bookRepo.put(book.getId(), book);
        return new ApiResponseSuccess("Book added successfully");
    }

    private boolean isNullOrBlank(String value) {
        return value == null || value.trim().isEmpty();
    }


    public ApiResponse borrowBook(BorrowRequest request) {
        Book book = bookRepo.get(request.getBookId());
        if (book == null) {
            return new ApiResponseError("Book not found");
        }
        if (book.isBorrowed()) {
            return new ApiResponseError("Book is already borrowed");
        }
        book.setBorrowed(true);
        return new ApiResponseSuccess("Book borrowed successfully");
    }

    public ApiResponse returnBook(BorrowRequest request) {
        Book book = bookRepo.get(request.getBookId());
        if (book == null) {
            return new ApiResponseError("Book not found");
        }
        if (!book.isBorrowed()) {
            return new ApiResponseError("Book was not borrowed");
        }
        book.setBorrowed(false);
        return new ApiResponseSuccess("Book returned successfully");
    }

    public ApiResponse getBookInfo(int bookId) {
        Book book = bookRepo.get(bookId);
        if (book == null) {
            return new ApiResponseError("Book not found");
        }
        return new ApiResponseSuccess(book);
    }
   
}
