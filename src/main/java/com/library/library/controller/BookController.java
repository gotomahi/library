package com.library.library.controller;

import com.library.library.model.Book;
import com.library.library.model.GenericResult;
import com.library.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<GenericResult> addBook(@RequestBody Book book) {
        return getResponseEntity(this.bookService.saveBook(book));
    }

    @GetMapping("/{name}/count")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<GenericResult<Integer>> getBookCount(@PathVariable String name) {
        return getResponseEntity(bookService.getBookCount(name));
    }

    @PutMapping("/{bookName}/rent")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<GenericResult<Integer>> rentBook(@PathVariable String bookName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getResponseEntity(bookService.rentBook(bookName, authentication.getName()));
    }

    @PutMapping("/{bookName}/rent/{user}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<GenericResult<Integer>> rentBook(@PathVariable String bookName, @PathVariable String user) {
        return getResponseEntity(bookService.rentBook(bookName, user));
    }

    @PutMapping("/{bookName}/return")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<GenericResult> returnBook(@PathVariable String bookName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getResponseEntity(bookService.returnBook(bookName, authentication.getName()));
    }

    private ResponseEntity getResponseEntity(GenericResult result){
        return ResponseEntity.status(result.isSuccess()? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(result);
    }
}
