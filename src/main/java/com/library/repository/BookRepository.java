package com.library.repository;

import com.library.model.Book;
import com.library.model.BookStatus;

import java.util.*;
import java.util.stream.Collectors;

public class BookRepository {
    private final Map<String, Book> books;

    public BookRepository() {
        this.books = new HashMap<>();
    }

    public void save(Book book) {
        if (book != null && book.getIsbn() != null) {
            books.put(book.getIsbn(), book);
        }
    }

    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    public List<Book> findByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String searchTerm = title.toLowerCase();
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public List<Book> findByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String searchTerm = author.toLowerCase();
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public List<Book> findByStatus(BookStatus status) {
        return books.values().stream()
                .filter(book -> book.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public boolean deleteByIsbn(String isbn) {
        return books.remove(isbn) != null;
    }

    public void update(Book book) {
        if (book != null && book.getIsbn() != null && books.containsKey(book.getIsbn())) {
            books.put(book.getIsbn(), book);
        }
    }

    public boolean existsByIsbn(String isbn) {
        return books.containsKey(isbn);
    }

    public long count() {
        return books.size();
    }

    public long countAvailable() {
        return books.values().stream()
                .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
                .count();
    }

    public void clear() {
        books.clear();
    }

    public List<Book> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }

        String searchTerm = keyword.toLowerCase();
        return books.values().stream()
                .filter(book ->
                        book.getTitle().toLowerCase().contains(searchTerm) ||
                                book.getAuthor().toLowerCase().contains(searchTerm) ||
                                book.getIsbn().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
}