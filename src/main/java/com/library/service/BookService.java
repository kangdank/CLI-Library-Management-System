package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.exception.InvalidOperationException;
import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.repository.BookRepository;
import com.library.util.InputValidator;

import java.util.List;

public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(String isbn, String title, String author, int publicationYear)
            throws InvalidOperationException {

        // Validation
        if (!InputValidator.isValidIsbn(isbn)) {
            throw new InvalidOperationException("ISBN không hợp lệ: " + isbn);
        }
        if (!InputValidator.isNotEmpty(title)) {
            throw new InvalidOperationException("Tên sách không được để trống");
        }
        if (!InputValidator.isNotEmpty(author)) {
            throw new InvalidOperationException("Tên tác giả không được để trống");
        }
        if (!InputValidator.isValidPublicationYear(publicationYear)) {
            throw new InvalidOperationException("Năm xuất bản không hợp lệ: " + publicationYear);
        }

        if (bookRepository.existsByIsbn(isbn)) {
            throw new InvalidOperationException("Sách với mã ISBN " + isbn + " đã tồn tại");
        }

        Book book = new Book(isbn, title, author, publicationYear);
        bookRepository.save(book);

        return book;
    }

    public void removeBook(String isbn) throws BookNotFoundException, InvalidOperationException {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> BookNotFoundException.forIsbn(isbn));

        if (book.getStatus() == BookStatus.BORROWED) {
            throw new InvalidOperationException("Không thể xóa một sách đang được mượn. ISBN: " + isbn);
        }

        bookRepository.deleteByIsbn(isbn);
    }

    public Book findBookByIsbn(String isbn) throws BookNotFoundException {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> BookNotFoundException.forIsbn(isbn));
    }


    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.search(keyword);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByStatus(BookStatus.AVAILABLE);
    }

    public List<Book> getBorrowedBooks() {
        return bookRepository.findByStatus(BookStatus.BORROWED);
    }

    public void updateBookStatus(String isbn, BookStatus status) throws BookNotFoundException {
        Book book = findBookByIsbn(isbn);
        book.setStatus(status);
        bookRepository.update(book);
    }

    public long getTotalBookCount() {
        return bookRepository.count();
    }

    public long getAvailableBookCount() {
        return bookRepository.countAvailable();
    }

    public boolean isBookAvailable(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(book -> book.getStatus() == BookStatus.AVAILABLE)
                .orElse(false);
    }
}