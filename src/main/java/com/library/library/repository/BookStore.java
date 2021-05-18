package com.library.library.repository;

import com.library.library.model.Book;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookStore {
    private static Map<String, Book> store = new HashMap<>();

    public void clear(){
        store.clear();
    }

    public void addBook(Book book) {
        if(store.get(book.getName()) == null) {
            store.put(book.getName(), book);
        }else{
            book = store.get(book.getName());
        }
        book.incrementCount();
    }

    public Book getBook(String bookName){
        return store.get(bookName);
    }

    public Integer rentBook(String bookName, String user) {
        Book book = store.get(bookName);
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
