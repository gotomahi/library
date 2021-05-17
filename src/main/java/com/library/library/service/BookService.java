package com.library.library.service;

import com.library.library.exception.InvalidInputException;
import com.library.library.model.Book;
import com.library.library.repository.BookStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookStore bookStore;

    public void saveBook(Book book) {
        this.bookStore.addBook(book);
    }

    public Integer getBookCount(String name) {
        if(name == null){
            throw new InvalidInputException("Book name is empty");
        }
        return bookStore.getBookCount(name);
    }

    public Integer rentBook(String bookName, String user) {
        return bookStore.rentBook(bookName, user);
    }

    public void returnBook(String bookName, String user) {
        bookStore.returnBook(bookName, user);
    }
}
