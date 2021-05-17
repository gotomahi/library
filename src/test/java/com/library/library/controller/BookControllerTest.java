package com.library.library.controller;

import com.library.library.exception.*;
import com.library.library.model.Book;
import com.library.library.repository.BookStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
public class BookControllerTest {
    @Autowired
    private BookController bookController;
    @Autowired
    private BookStore bookStore;

    @BeforeEach
    public void setup(){
        bookStore.clear();
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

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void test_available_book_count(){
        bookStore.addBook(new Book("Time To Kill"));
        ResponseEntity<Integer> response = bookController.getBookCount("Time To Kill");
        assertEquals(1, response.getBody());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void test_available_book_count_for_invalid_input(){
        assertThrows(InvalidInputException.class, ()->{
            bookController.getBookCount(null);
        });
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void test_available_book_count_that_doesnot_exists(){
        assertThrows(BookNotFoundException.class, () -> {
            bookController.getBookCount("Test Book");
        });
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void test_available_book_count_after_addition(){
        bookController.addBook(new Book("The Great Gatsby"));
        bookController.addBook(new Book("The Great Gatsby"));
        ResponseEntity<Integer> response = bookController.getBookCount("The Great Gatsby");
        assertEquals(2, response.getBody());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void test_rent_a_book(){
        bookStore.addBook(new Book("Time To Kill"));
        ResponseEntity<Integer> response = bookController.rentBook("Time To Kill");
        assertEquals(0, response.getBody());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void test_no_book_in_store_after_all_copies_rented(){
        bookStore.addBook(new Book("Beloved"));
        bookController.rentBook("Beloved");
        assertThrows(BookNotAvailableException.class, () -> {
            bookController.rentBook("Beloved");
        });
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void test_book_not_found_in_store(){
        assertThrows(BookNotFoundException.class, () -> {
            bookController.rentBook("Test Book");
        });
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void test_multiple_copies_not_allowed_to_user(){
        bookStore.addBook(new Book("Invisible Man"));
        bookStore.addBook(new Book("Invisible Man"));
        bookController.rentBook("Invisible Man");
        assertThrows(MultipleCopiesNotAllowedException.class, () -> {
            bookController.rentBook("Invisible Man");
        });
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void test_user_not_allowed_more_than_two_books(){
        bookController.addBook(new Book("Absalom Absalom"));
        bookController.addBook(new Book("Invisible Man"));
        bookController.addBook(new Book("Passage To India"));
        bookController.addBook(new Book("The Great Gatsby"));
        bookController.rentBook("The Great Gatsby", "user");
        bookController.rentBook("Absalom Absalom", "user");
        bookController.rentBook("Passage To India", "user");
        assertThrows(ExceedingLimitException.class, () -> {
            bookController.rentBook("Invisible Man", "user");
        });
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void test_return_book_to_store(){
        bookStore.addBook(new Book("Time To Kill"));
        bookController.rentBook("Time To Kill");
        ResponseEntity response = bookController.returnBook("Time To Kill");
        assertEquals(200, response.getStatusCode().value());
    }
}
