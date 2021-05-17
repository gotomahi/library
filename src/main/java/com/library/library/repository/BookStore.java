package com.library.library.repository;

import com.library.library.model.Book;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookStore {
    private static Map<String, Book> store = new HashMap<>();
    public void addBook(Book book) {
        if(book != null) {
            store.put(book.getName(), book);
        }
    }
}
