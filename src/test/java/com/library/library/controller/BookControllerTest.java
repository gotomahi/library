package com.library.library.controller;

import com.library.library.model.Book;
import com.library.library.model.GenericResult;
import com.library.library.repository.BookStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        ResponseEntity<GenericResult> response = bookController.addBook(book);
        assertEquals(true, response.getBody().isSuccess());
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
        ResponseEntity<GenericResult<Integer>> response = bookController.getBookCount("Time To Kill");
        assertEquals(1, response.getBody().getResult().intValue());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void test_available_book_count_for_invalid_input(){
        ResponseEntity<GenericResult<Integer>> response = bookController.getBookCount(null);
        assertEquals(false, response.getBody().isSuccess());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals(true, response.getBody().hasError("Book name is empty"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void test_available_book_count_that_doesnot_exists(){
        ResponseEntity<GenericResult<Integer>> response = bookController.getBookCount("Test Book");
        assertEquals(false, response.getBody().isSuccess());
        assertEquals(true, response.getBody().hasError("Book not found in store"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void test_available_book_count_after_addition(){
        bookController.addBook(new Book("The Great Gatsby"));
        bookController.addBook(new Book("The Great Gatsby"));
        ResponseEntity<GenericResult<Integer>> response = bookController.getBookCount("The Great Gatsby");
        assertEquals(2, response.getBody().getResult().intValue());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void test_rent_a_book(){
        Book book = new Book();
        book.setName("Time To Kill");
        bookStore.addBook(book);
        ResponseEntity<GenericResult<Integer>> response = bookController.rentBook("Time To Kill");
        assertEquals(0, response.getBody().getResult().intValue());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void test_no_book_in_store_after_all_copies_rented(){
        bookStore.addBook(new Book("Beloved"));
        bookController.rentBook("Beloved");
        ResponseEntity<GenericResult<Integer>> response = bookController.rentBook("Beloved");
        assertEquals(false, response.getBody().isSuccess());
        assertEquals(true, response.getBody().hasError("Book not available in store"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void test_book_not_found_in_store(){
        ResponseEntity<GenericResult<Integer>> response = bookController.rentBook("Test Book");
        assertEquals(false, response.getBody().isSuccess());
        assertEquals(true, response.getBody().hasError("Book not found in store"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void test_multiple_copies_not_allowed_to_user(){
        bookStore.addBook(new Book("Invisible Man"));
        bookStore.addBook(new Book("Invisible Man"));
        bookController.rentBook("Invisible Man");
        ResponseEntity<GenericResult<Integer>> response = bookController.rentBook("Invisible Man");
        assertEquals(false, response.getBody().isSuccess());
        assertEquals(true, response.getBody().hasError("Multiple copies are not allowed to same user"));
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
        ResponseEntity<GenericResult<Integer>> response = bookController.rentBook("Invisible Man", "user");
        assertEquals(false, response.getBody().isSuccess());
        assertEquals(true, response.getBody().hasError("User is not allowed more than 3 books"));
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
