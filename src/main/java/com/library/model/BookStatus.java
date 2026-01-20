package com.library.model;

public enum BookStatus {
    AVAILABLE("Còn sách"),
    BORROWED("Sách đã mượn");

    private final String displayName;

    BookStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}