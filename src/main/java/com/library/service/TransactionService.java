package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.exception.InvalidOperationException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.repository.TransactionRepository;

import java.util.List;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BookService bookService;
    private final MemberService memberService;

    public TransactionService(TransactionRepository transactionRepository,
                              BookService bookService,
                              MemberService memberService) {
        this.transactionRepository = transactionRepository;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    public Transaction borrowBook(String memberId, String isbn)
            throws MemberNotFoundException, BookNotFoundException, InvalidOperationException {

        Member member = memberService.findMemberById(memberId);
        if (!member.isActive()) {
            throw InvalidOperationException.memberInactive(memberId);
        }

        if (!memberService.canBorrowBooks(memberId)) {
            throw InvalidOperationException.borrowLimitExceeded(
                    memberId, memberService.getMaxBorrowLimit());
        }

        Book book = bookService.findBookByIsbn(isbn);
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw InvalidOperationException.bookAlreadyBorrowed(isbn);
        }

        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(transactionId, memberId, isbn);

        bookService.updateBookStatus(isbn, BookStatus.BORROWED);

        member.borrowBook(isbn);

        transactionRepository.save(transaction);

        return transaction;
    }

    public Transaction returnBook(String memberId, String isbn)
            throws MemberNotFoundException, BookNotFoundException, InvalidOperationException {

        Member member = memberService.findMemberById(memberId);

        Book book = bookService.findBookByIsbn(isbn);

        if (!member.hasBorrowedBook(isbn)) {
            throw new InvalidOperationException(
                    "Thành viên " + memberId + " chưa mượn cuốn sách với ISBN: " + isbn);
        }

        Transaction transaction = transactionRepository.findActiveTransactionByBookIsbn(isbn)
                .orElseThrow(() -> InvalidOperationException.bookNotBorrowed(isbn));

        if (!transaction.getMemberId().equals(memberId)) {
            throw new InvalidOperationException(
                    "Sách đã được mượn bởi một thành viên khác. ISBN: " + isbn);
        }

        transaction.completeReturn();

        bookService.updateBookStatus(isbn, BookStatus.AVAILABLE);

        member.returnBook(isbn);

        transactionRepository.update(transaction);

        return transaction;
    }

    public List<Transaction> getMemberTransactions(String memberId) {
        return transactionRepository.findByMemberId(memberId);
    }

    public List<Transaction> getBookTransactions(String isbn) {
        return transactionRepository.findByBookIsbn(isbn);
    }

    public List<Transaction> getMemberActiveTransactions(String memberId) {
        return transactionRepository.findActiveTransactionsByMemberId(memberId);
    }

    public List<Transaction> getOverdueTransactions() {
        return transactionRepository.findOverdueTransactions();
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getRecentTransactions(int limit) {
        return transactionRepository.findRecentTransactions(limit);
    }

    public double calculateLateFee(String transactionId) throws InvalidOperationException {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new InvalidOperationException("Không tìm thấy giao dịch: " + transactionId));

        return transaction.calculateLateFee();
    }

    public double getTotalLateFees() {
        return transactionRepository.calculateTotalLateFees();
    }

    public long getTotalTransactionCount() {
        return transactionRepository.count();
    }

    public long getActiveTransactionCount() {
        return transactionRepository.countActive();
    }

    private String generateTransactionId() {
        String transactionId;
        do {
            transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
        } while (transactionRepository.existsById(transactionId));
        return transactionId;
    }
}