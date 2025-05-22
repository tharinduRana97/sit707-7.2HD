package com.tavisca.api.book.repository;

import com.tavisca.api.book.POJO.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
