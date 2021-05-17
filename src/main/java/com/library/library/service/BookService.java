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

    public void saveBook(Book book) throws InvalidInputException {
        if(book != null) {
            this.bookStore.addBook(book);
        }else{
            throw new InvalidInputException("Empty book is not allowed to add");
        }
    }

    public Integer getBookCount(String name) {
        if(name == null){
            throw new InvalidInputException("Empty book name is not allowed");
        }
        return bookStore.getBookCount(name);
    }
}
