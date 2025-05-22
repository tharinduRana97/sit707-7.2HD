package com.tavisca.api.book.controller;

import com.tavisca.api.book.POJO.*;
import com.tavisca.api.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookWebController {

    @Autowired
    private BookService bookService;
    
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
    	model.addAttribute("book", new Book());
        return "add";
    }
    
    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book, Model model) {
        ApiResponse response = bookService.addBook(book);
        model.addAttribute("message", response instanceof ApiResponseSuccess
            ? ((ApiResponseSuccess) response).getData()
            : ((ApiResponseError) response).getMessage());
        return "add";
    }

    @GetMapping("/borrow")
    public String showBorrowForm(Model model) {
        model.addAttribute("borrowRequest", new BorrowRequest());
        return "borrow";
    }

    @PostMapping("/borrow")
    public String borrowBook(@ModelAttribute BorrowRequest borrowRequest, Model model) {
        ApiResponse response = bookService.borrowBook(borrowRequest);
        model.addAttribute("message", (response instanceof ApiResponseSuccess)
                ? ((ApiResponseSuccess) response).getData()
                : ((ApiResponseError) response).getMessage());
        return "borrow";
    }

    @GetMapping("/return")
    public String showReturnForm(Model model) {
        model.addAttribute("borrowRequest", new BorrowRequest());
        return "return";
    }

    @PostMapping("/return")
    public String returnBook(@ModelAttribute BorrowRequest borrowRequest, Model model) {
        ApiResponse response = bookService.returnBook(borrowRequest);
        model.addAttribute("message", (response instanceof ApiResponseSuccess)
                ? ((ApiResponseSuccess) response).getData()
                : ((ApiResponseError) response).getMessage());
        return "return";
    }

    @GetMapping("/info")
    public String getBookInfo(@RequestParam(required = false) Integer bookId, Model model) {
        if (bookId != null) {
            ApiResponse response = bookService.getBookInfo(bookId);
            if (response instanceof ApiResponseSuccess) {
                model.addAttribute("book", (Book) ((ApiResponseSuccess) response).getData());
            } else {
                model.addAttribute("error", ((ApiResponseError) response).getMessage());
            }
        }
        return "info";
    }
}
