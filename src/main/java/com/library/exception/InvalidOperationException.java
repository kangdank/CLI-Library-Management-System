package com.library.exception;

public class InvalidOperationException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InvalidOperationException bookAlreadyBorrowed(String isbn) {
        return new InvalidOperationException("Sách đã được mượn: " + isbn);
    }

    public static InvalidOperationException bookNotBorrowed(String isbn) {
        return new InvalidOperationException("Sách hiện tại chưa được mượn: " + isbn);
    }

    public static InvalidOperationException memberInactive(String memberId) {
        return new InvalidOperationException("Thành viên này không hoạt động: " + memberId);
    }

    public static InvalidOperationException borrowLimitExceeded(String memberId, int limit) {
        return new InvalidOperationException(
                String.format("Thành viên %s đã đạt tới giới hạn %d sách mượn", memberId, limit));
    }
}