package com.library.library.repository;

import com.library.library.exception.BookNotFoundException;
import com.library.library.model.Book;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookStore {
    private static Map<String, Book> store = new HashMap<>();
    public void addBook(Book book) {
        if(book != null) {
            if(store.get(book.getName()) == null) {
                store.put(book.getName(), book);
            }else{
                book = store.get(book.getName());
            }
            book.incrementCount();
        }
    }

    public Integer getBookCount(String name) {
        Book book = store.get(name);
        if(book == null){
            throw new BookNotFoundException("Book not found in store");
        }
        return book.getCount();
    }
}
