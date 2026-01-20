package com.library.exception;

public class BookNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static BookNotFoundException forIsbn(String isbn) {
        return new BookNotFoundException("Không tìm thấy sách với mã ISBN: " + isbn);
    }

    public static BookNotFoundException forTitle(String title) {
        return new BookNotFoundException("Không tìm thấy sách với tên: " + title);
    }
}
