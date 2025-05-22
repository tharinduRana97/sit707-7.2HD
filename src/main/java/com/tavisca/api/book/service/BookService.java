package com.tavisca.api.book.service;

import com.tavisca.api.book.POJO.*;
import com.tavisca.api.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    public ApiResponse addBook(Book book) {
        if (book == null) return new ApiResponseError("Book cannot be null");
        if (book.getId() <= 0) return new ApiResponseError("Invalid book ID");
        if (bookRepository.existsById(book.getId())) return new ApiResponseError("Book with this ID already exists");
        if (isNullOrBlank(book.getTitle()))
            return new ApiResponseError("Title and author must not be empty");

        bookRepository.save(book);
        return new ApiResponseSuccess("Book added successfully");
    }

    public ApiResponse borrowBook(BorrowRequest request) {
        Optional<Book> bookOpt = bookRepository.findById(request.getBookId());
        if (bookOpt.isEmpty()) return new ApiResponseError("Book not found");

        Book book = bookOpt.get();
        if (book.isBorrowed()) return new ApiResponseError("Book is already borrowed");

        book.setBorrowed(true);
        bookRepository.save(book);
        return new ApiResponseSuccess("Book borrowed successfully");
    }

    public ApiResponse returnBook(BorrowRequest request) {
        Optional<Book> bookOpt = bookRepository.findById(request.getBookId());
        if (bookOpt.isEmpty()) return new ApiResponseError("Book not found");

        Book book = bookOpt.get();
        if (!book.isBorrowed()) return new ApiResponseError("Book was not borrowed");

        book.setBorrowed(false);
        bookRepository.save(book);
        return new ApiResponseSuccess("Book returned successfully");
    }

    public ApiResponse getBookInfo(int bookId) {
;     	List<Book> bopoks = bookRepository.findAll();
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) return new ApiResponseError("Book not found");
        return new ApiResponseSuccess(bookOpt.get());
    }

    private boolean isNullOrBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
