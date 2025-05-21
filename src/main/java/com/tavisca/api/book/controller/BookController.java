package com.tavisca.api.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tavisca.api.book.POJO.ApiResponse;
import com.tavisca.api.book.POJO.BorrowRequest;
import com.tavisca.api.book.service.BookService;


@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/borrow")
    public ResponseEntity<ApiResponse> borrowBook(@RequestBody BorrowRequest request) {
        return ResponseEntity.ok(bookService.borrowBook(request));
    }

    @PostMapping("/return")
    public ResponseEntity<ApiResponse> returnBook(@RequestBody BorrowRequest request) {
        return ResponseEntity.ok(bookService.returnBook(request));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse> getBook(@PathVariable int bookId) {
        return ResponseEntity.ok(bookService.getBookInfo(bookId));
    }
}
