package com.library.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String memberId;
    private String name;
    private String email;
    private String phone;
    private LocalDate registrationDate;
    private List<String> borrowedBookIsbns;
    private boolean isActive;

    public Member() {
        this.borrowedBookIsbns = new ArrayList<>();
        this.registrationDate = LocalDate.now();
        this.isActive = true;
    }

    public Member(String memberId, String name, String email, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.borrowedBookIsbns = new ArrayList<>();
        this.registrationDate = LocalDate.now();
        this.isActive = true;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<String> getBorrowedBookIsbns() {
        return new ArrayList<>(borrowedBookIsbns);
    }

    public void setBorrowedBookIsbns(List<String> borrowedBookIsbns) {
        this.borrowedBookIsbns = new ArrayList<>(borrowedBookIsbns);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void borrowBook(String isbn) {
        if (!borrowedBookIsbns.contains(isbn)) {
            borrowedBookIsbns.add(isbn);
        }
    }

    public void returnBook(String isbn) {
        borrowedBookIsbns.remove(isbn);
    }

    public int getBorrowedBookCount() {
        return borrowedBookIsbns.size();
    }

    public boolean hasBorrowedBook(String isbn) {
        return borrowedBookIsbns.contains(isbn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }

    @Override
    public String toString() {
        return String.format("Thành viên[ID=%s, Tên='%s', Email='%s', Sách đã mượn=%d, Trạng thái=%s]",
                memberId, name, email, borrowedBookIsbns.size(), isActive);
    }
}