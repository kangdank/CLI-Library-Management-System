package com.library.repository;

import com.library.model.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class TransactionRepository {
    private final Map<String, Transaction> transactions;
    private final List<Transaction> transactionHistory;

    public TransactionRepository() {
        this.transactions = new HashMap<>();
        this.transactionHistory = new ArrayList<>();
    }

    public void save(Transaction transaction) {
        if (transaction != null && transaction.getTransactionId() != null) {
            transactions.put(transaction.getTransactionId(), transaction);
            transactionHistory.add(transaction);
        }
    }

    public Optional<Transaction> findById(String transactionId) {
        return Optional.ofNullable(transactions.get(transactionId));
    }

    public List<Transaction> findByMemberId(String memberId) {
        return transactions.values().stream()
                .filter(t -> t.getMemberId().equals(memberId))
                .sorted(Comparator.comparing(Transaction::getBorrowDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Transaction> findByBookIsbn(String isbn) {
        return transactions.values().stream()
                .filter(t -> t.getBookIsbn().equals(isbn))
                .sorted(Comparator.comparing(Transaction::getBorrowDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Transaction> findActiveTransactionsByMemberId(String memberId) {
        return transactions.values().stream()
                .filter(t -> t.getMemberId().equals(memberId))
                .filter(t -> t.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    public Optional<Transaction> findActiveTransactionByBookIsbn(String isbn) {
        return transactions.values().stream()
                .filter(t -> t.getBookIsbn().equals(isbn))
                .filter(t -> t.getReturnDate() == null)
                .findFirst();
    }

    public List<Transaction> findOverdueTransactions() {
        return transactions.values().stream()
                .filter(Transaction::isOverdue)
                .sorted(Comparator.comparing(Transaction::getDueDate))
                .collect(Collectors.toList());
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactionHistory);
    }

    public List<Transaction> findRecentTransactions(int limit) {
        return transactionHistory.stream()
                .sorted(Comparator.comparing(Transaction::getBorrowDate).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void update(Transaction transaction) {
        if (transaction != null && transaction.getTransactionId() != null
                && transactions.containsKey(transaction.getTransactionId())) {
            transactions.put(transaction.getTransactionId(), transaction);

            for (int i = 0; i < transactionHistory.size(); i++) {
                if (transactionHistory.get(i).getTransactionId().equals(transaction.getTransactionId())) {
                    transactionHistory.set(i, transaction);
                    break;
                }
            }
        }
    }

    public boolean existsById(String transactionId) {
        return transactions.containsKey(transactionId);
    }

    public long count() {
        return transactions.size();
    }

    public long countActive() {
        return transactions.values().stream()
                .filter(t -> t.getReturnDate() == null)
                .count();
    }

    public double calculateTotalLateFees() {
        return transactions.values().stream()
                .filter(t -> t.getReturnDate() != null)
                .mapToDouble(Transaction::getLateFee)
                .sum();
    }

    public void clear() {
        transactions.clear();
        transactionHistory.clear();
    }
}