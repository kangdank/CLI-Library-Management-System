package com.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int BORROW_PERIOD_DAYS = 14;
    private static final double LATE_FEE_PER_DAY = 1.0;

    private String transactionId;
    private String memberId;
    private String bookIsbn;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private TransactionType type;
    private double lateFee;

    public enum TransactionType {
        BORROW, RETURN
    }

    public Transaction() {
    }

    public Transaction(String transactionId, String memberId, String bookIsbn) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.bookIsbn = bookIsbn;
        this.borrowDate = LocalDateTime.now();
        this.dueDate = borrowDate.plusDays(BORROW_PERIOD_DAYS);
        this.type = TransactionType.BORROW;
        this.lateFee = 0.0;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    public boolean isOverdue() {
        if (returnDate != null) {
            return false;
        }
        return LocalDateTime.now().isAfter(dueDate);
    }

    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDateTime.now());
    }

    public double calculateLateFee() {
        long daysOverdue = getDaysOverdue();
        return daysOverdue > 0 ? daysOverdue * LATE_FEE_PER_DAY : 0.0;
    }

    public void completeReturn() {
        this.returnDate = LocalDateTime.now();
        this.type = TransactionType.RETURN;
        this.lateFee = calculateLateFee();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return String.format("Giao dịch[ID=%s, Thành viên=%s, Sách=%s, Loại giao dịch=%s, Ngày mượn=%s, Ngày trả sách đã định=%s, Ngày trả sách thực tế=%s, Phí trả muộn=%.2f]",
                transactionId, memberId, bookIsbn, type, borrowDate, dueDate, returnDate, lateFee);
    }
}