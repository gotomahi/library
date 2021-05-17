package com.library.library.controller;

import com.library.library.model.Book;
import com.library.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity addBook(@RequestBody Book book) {
        this.bookService.saveBook(book);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{name}/count")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Integer> getBookCount(@PathVariable String name) {
        return ResponseEntity.ok().body(bookService.getBookCount(name));
    }

    @PutMapping("/{bookName}/rent")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Integer> rentBook(@PathVariable String bookName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok().body(bookService.rentBook(bookName, authentication.getName()));
    }

    @PutMapping("/{bookName}/rent/{user}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Integer> rentBook(@PathVariable String bookName, @PathVariable String user) {
        return ResponseEntity.ok().body(bookService.rentBook(bookName, user));
    }

    @PutMapping("/{bookName}/return")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity returnBook(@PathVariable String bookName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        bookService.returnBook(bookName, authentication.getName());
        return ResponseEntity.ok().build();
    }
}
