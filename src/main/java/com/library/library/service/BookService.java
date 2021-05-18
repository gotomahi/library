package com.library.library.service;

import com.library.library.model.Book;
import com.library.library.model.GenericResult;
import com.library.library.repository.BookStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookStore bookStore;

    public GenericResult saveBook(Book book) {
        this.bookStore.addBook(book);
        return new GenericResult(true);
    }

    public GenericResult<Integer> getBookCount(String name) {
        GenericResult<Integer> result = new GenericResult<>();
        if(name == null){
            result.addError("Book name is empty");
        }else{
            Book book = bookStore.getBook(name);
            if(book == null) {
                result.addError("Book not found in store");
            }else{
                result.setResult(book.getCount());
                result.setSuccess(true);
            }
        }
        return result;
    }

    public GenericResult<Integer> rentBook(String bookName, String user) {
        GenericResult<Integer> result = new GenericResult<>();
        Book book = bookStore.getBook(bookName);
        if(book == null){
            result.addError("Book not found in store");
        }else if(book.getCount() == 0){
            result.addError("Book not available in store");
        }else if(book.isAlreadyTaken(user)){
            result.addError("Multiple copies are not allowed to same user");
        }else if(bookStore.isUserReachedLimit(user)){
            result.addError("User is not allowed more than 3 books");
        }else{
            result.setSuccess(true);
            result.setResult(bookStore.rentBook(bookName, user));
        }
        return result;
    }

    public GenericResult returnBook(String bookName, String user) {
        bookStore.returnBook(bookName, user);
        return new GenericResult(true);
    }
}
