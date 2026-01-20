package com.library.exception;

public class MemberNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static MemberNotFoundException forId(String memberId) {
        return new MemberNotFoundException("Không tìm thấy thành viên với ID: " + memberId);
    }

    public static MemberNotFoundException forEmail(String email) {
        return new MemberNotFoundException("Không tìm thấy thành viên với Email: " + email);
    }
}