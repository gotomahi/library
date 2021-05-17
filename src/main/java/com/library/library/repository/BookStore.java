package com.library.library.repository;

import com.library.library.exception.BookNotAvailableException;
import com.library.library.exception.BookNotFoundException;
import com.library.library.exception.ExceedingLimitException;
import com.library.library.exception.MultipleCopiesNotAllowedException;
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

    public Integer rentBook(String bookName, String user) {
        Book book = store.get(bookName);
        if(book == null){
            throw new BookNotFoundException("Book not found in store");
        }else if(book.getCount() == 0){
            throw new BookNotAvailableException("Book not available in store");
        }else if(book.isAlreadyTaken(user)){
            throw new MultipleCopiesNotAllowedException("Multiple copies are not allowed to same user");
        }else if(isUserReachedLimit(user)){
            throw new ExceedingLimitException("User is not allowed more than 3 books");
        }
        book.decrementCount();
        book.addUser(user);
        return book.getCount();
    }

    public boolean isUserReachedLimit(String user){
        long count = store.keySet().stream()
                        .map(key -> store.get(key))
                        .filter(book -> book.isAlreadyTaken(user))
                        .count();
        return count == 3;
    }

    public void returnBook(String bookName, String user) {
        Book book = store.get(bookName);
        book.incrementCount();
        book.removeUser(user);
    }
}
