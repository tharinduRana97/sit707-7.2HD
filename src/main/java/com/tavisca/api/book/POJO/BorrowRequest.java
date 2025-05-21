package com.tavisca.api.book.POJO;

public class BorrowRequest {
    private int bookId;

    public BorrowRequest() {
    }
    
    public BorrowRequest(int bookId) {
        this.bookId = bookId;
    }

    // Getter
    public int getBookId() {
        return bookId;
    }

    // Setter
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
