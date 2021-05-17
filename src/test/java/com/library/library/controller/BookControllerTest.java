package com.library.library.controller;

import com.library.library.model.Book;
import com.library.library.repository.BookStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookControllerTest {
    @Autowired
    private BookController bookController;
    @Autowired
    private BookStore bookStore;

    @BeforeAll
    public void setup(){
        bookStore.addBook(new Book("Time To Kill"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void testAddBook(){
        Book book = new Book("Absalom Absalom");
        ResponseEntity<Book> response = bookController.addBook(book);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void test_add_book_by_non_admin(){
        assertThrows(AccessDeniedException.class, () -> {
            bookController.addBook(new Book("Absalom Absalom"));
        });
    }
}
