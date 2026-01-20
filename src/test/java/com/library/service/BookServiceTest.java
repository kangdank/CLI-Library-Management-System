package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.exception.InvalidOperationException;
import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = new BookRepository();
        bookService = new BookService(bookRepository);
    }

    @Test
    void testAddBook_WithValidData_Success() throws InvalidOperationException {
        Book book = bookService.addBook("978-0123456789", "Test case", "Khang Dang", 2026);

        assertNotNull(book);
        assertEquals("978-0123456789", book.getIsbn());
        assertEquals("Test case", book.getTitle());
        assertEquals("Khang Dang", book.getAuthor());
        assertEquals(2026, book.getPublicationYear());
        assertEquals(BookStatus.AVAILABLE, book.getStatus());
    }

    @Test
    void testAddBook_WithInvalidIsbn_ThrowsException() {
        assertThrows(InvalidOperationException.class, () -> {
            bookService.addBook("khonghople-isbn", "Ten", "Tac gia", 2020);
        });
    }

    @Test
    void testAddBook_WithEmptyTitle_ThrowsException() {
        assertThrows(InvalidOperationException.class, () -> {
            bookService.addBook("978-0123456789", "", "Tac gia", 2020);
        });
    }

    @Test
    void testAddBook_WithInvalidYear_ThrowsException() {
        assertThrows(InvalidOperationException.class, () -> {
            bookService.addBook("978-0123456789", "Ten", "Tac gia", 2100);
        });
    }

    @Test
    void testAddBook_WithDuplicateIsbn_ThrowsException() throws InvalidOperationException {
        bookService.addBook("978-0123456789", "Test case", "Khang Dang", 2026);

        assertThrows(InvalidOperationException.class, () -> {
            bookService.addBook("978-0123456789", "abc", "abc", 2020);
        });
    }

    @Test
    void testFindBookByIsbn_ExistingBook_ReturnsBook() throws Exception {
        bookService.addBook("978-0123456789", "Test case", "Khang Dang", 2026);

        Book found = bookService.findBookByIsbn("978-0123456789");

        assertNotNull(found);
        assertEquals("Test case", found.getTitle());
    }

    @Test
    void testFindBookByIsbn_NonExistingBook_ThrowsException() {
        assertThrows(BookNotFoundException.class, () -> {
            bookService.findBookByIsbn("999-9999999999");
        });
    }

    @Test
    void testSearchBooksByTitle_PartialMatch_ReturnsBooks() throws InvalidOperationException {
        bookService.addBook("978-0123456789", "Test case", "Khang Dang", 2026);
        bookService.addBook("978-9876543210", "Test java", "abcxyz", 2017);

        List<Book> results = bookService.searchBooksByTitle("case");

        assertEquals(1, results.size());
        assertEquals("Test case", results.get(0).getTitle());
    }

    @Test
    void testSearchBooksByAuthor_PartialMatch_ReturnsBooks() throws InvalidOperationException {
        bookService.addBook("978-0123456789", "Test case", "Khang Dang", 2008);
        bookService.addBook("978-9876543210", "Test java", "abcxyz", 2017);

        List<Book> results = bookService.searchBooksByAuthor("khang");

        assertEquals(1, results.size());
        assertEquals("Khang Dang", results.get(0).getAuthor());
    }

    @Test
    void testGetAllBooks_ReturnsAllBooks() throws InvalidOperationException {
        bookService.addBook("978-0123456789", "Test case", "Khang Dang", 2026);
        bookService.addBook("978-9876543210", "Test java", "abcxyz", 2017);

        List<Book> books = bookService.getAllBooks();

        assertEquals(2, books.size());
    }

    @Test
    void testGetAvailableBooks_OnlyReturnsAvailable() throws Exception {
        bookService.addBook("978-0123456789", "Test case", "Khang Dang", 2026);
        bookService.addBook("978-9876543210", "Test java", "abcxyz", 2017);

        // Thay đổi trạng thái
        bookService.updateBookStatus("978-0123456789", BookStatus.BORROWED);

        List<Book> available = bookService.getAvailableBooks();

        assertEquals(1, available.size());
        assertEquals("978-9876543210", available.get(0).getIsbn());
    }

    @Test
    void testRemoveBook_AvailableBook_Success() throws Exception {
        bookService.addBook("978-0123456789", "Test case", "Khang Dang", 2026);

        bookService.removeBook("978-0123456789");

        assertThrows(BookNotFoundException.class, () -> {
            bookService.findBookByIsbn("978-0123456789");
        });
    }

    @Test
    void testRemoveBook_BorrowedBook_ThrowsException() throws Exception {
        bookService.addBook("978-0123456789", "Test case", "Khang Dang", 2026);
        bookService.updateBookStatus("978-0123456789", BookStatus.BORROWED);

        assertThrows(InvalidOperationException.class, () -> {
            bookService.removeBook("978-0123456789");
        });
    }

    @Test
    void testIsBookAvailable_AvailableBook_ReturnsTrue() throws InvalidOperationException {
        bookService.addBook("978-0123456789", "Test case", "Khang Dang", 2026);

        assertTrue(bookService.isBookAvailable("978-0123456789"));
    }

    @Test
    void testIsBookAvailable_BorrowedBook_ReturnsFalse() throws Exception {
        bookService.addBook("978-0123456789", "Test case", "Khang Dang", 2026);
        bookService.updateBookStatus("978-0123456789", BookStatus.BORROWED);

        assertFalse(bookService.isBookAvailable("978-0123456789"));
    }

    @Test
    void testGetTotalBookCount_ReturnsCorrectCount() throws InvalidOperationException {
        bookService.addBook("978-0123456789", "Test case", "Khang Dang", 2026);
        bookService.addBook("978-9876543210", "Test java", "abcxyz", 2017);

        assertEquals(2, bookService.getTotalBookCount());
    }
}