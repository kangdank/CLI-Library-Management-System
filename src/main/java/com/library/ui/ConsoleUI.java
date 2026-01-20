package com.library.ui;

import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.util.DateUtil;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;

    private static final String BORDER = "══════════════════════════════════════════════════════════════════════════════════════════════════";
    private static final String SEPARATOR = "──────────────────────────────────────────────────────────────────────────────────────────────────";

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public void displayMainMenu() {
        clearScreen();
        System.out.println("\n" + BORDER);
        System.out.println("           HỆ THỐNG QUẢN LÝ THƯ VIỆN");
        System.out.println(BORDER);
        System.out.println("\n1. Quản lý sách");
        System.out.println("2. Quản lí thành viên");
        System.out.println("3. Quản lí giao dịch");
        System.out.println("4. Xem thống kê");
        System.out.println("5. Lưu dữ liệu");
        System.out.println("6. Thoát");
        System.out.println("\n" + SEPARATOR);
    }

    public void displayBookMenu() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("          QUẢN LÝ SÁCH");
        System.out.println(SEPARATOR);
        System.out.println("\n1. Thêm sách mới");
        System.out.println("2. Xóa sách");
        System.out.println("3. Tìm sách");
        System.out.println("4. Tất cả sách");
        System.out.println("5. Tất cả sách còn");
        System.out.println("6. Trở về Menu chính");
        System.out.println("\n" + SEPARATOR);
    }

    public void displayMemberMenu() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("          QUẢN LÝ THÀNH VIÊN");
        System.out.println(SEPARATOR);
        System.out.println("\n1. Đăng ký thành viên mới");
        System.out.println("2. Tìm thành viên");
        System.out.println("3. Tất cả thành viên");
        System.out.println("4. Xem thông tin chi tiết");
        System.out.println("5. Hủy kích hoạt thành viên");
        System.out.println("6. Kích hoạt thành viên");
        System.out.println("7. Trở về Menu chính");
        System.out.println("\n" + SEPARATOR);
    }

    public void displayTransactionMenu() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("          QUẢN LÝ GIAO DỊCH");
        System.out.println(SEPARATOR);
        System.out.println("\n1. Mượn sách");
        System.out.println("2. Trả sách");
        System.out.println("3. Xem giao dịch của thành viên");
        System.out.println("4. Xem sách đã quá hạn trả");
        System.out.println("5. Xem giao dịch gần đây");
        System.out.println("6. Trở về Menu chính");
        System.out.println("\n" + SEPARATOR);
    }

    public void displayBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("\n❌ Danh sách sách trống");
            return;
        }

        System.out.println("\n" + BORDER);
        System.out.println(String.format("%-15s %-30s %-25s %-6s %-12s",
                "ISBN", "TÊN", "TÁC GIẢ", "NĂM", "TRẠNG THÁI"));
        System.out.println(BORDER);

        for (Book book : books) {
            System.out.println(String.format("%-15s %-30s %-25s %-6d %-12s",
                    truncate(book.getIsbn(), 15),
                    truncate(book.getTitle(), 30),
                    truncate(book.getAuthor(), 25),
                    book.getPublicationYear(),
                    book.getStatus()));
        }
        System.out.println(BORDER);
        System.out.println("Tổng cộng: " + books.size() + " cuốn sách.");
    }

    public void displayMembers(List<Member> members) {
        if (members.isEmpty()) {
            System.out.println("\n❌ Danh sách thành viên trống.");
            return;
        }

        System.out.println("\n" + BORDER);
        System.out.println(String.format("%-12s %-25s %-30s %-15s %-8s",
                "ID THÀNH VIÊN", "TÊN", "EMAIL", "SĐT", "SÁCH MƯỢN"));
        System.out.println(BORDER);

        for (Member member : members) {
            System.out.println(String.format("%-12s %-25s %-30s %-15s %-8d",
                    member.getMemberId(),
                    truncate(member.getName(), 25),
                    truncate(member.getEmail(), 30),
                    member.getPhone(),
                    member.getBorrowedBookCount()));
        }
        System.out.println(BORDER);
        System.out.println("Tổng cộng: " + members.size() + " thành viên.");
    }

    public void displayTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("\n❌ Danh sách giao dịch trống.");
            return;
        }

        System.out.println("\n" + BORDER);
        System.out.println(String.format("%-15s %-12s %-15s %-19s %-19s %-8s",
                "ID GIAO DỊCH", "ID THÀNH VIÊN", "ISBN", "NGÀY MƯỢN", "NGÀY TRẢ", "TRẠNG THÁI"));
        System.out.println(BORDER);

        for (Transaction transaction : transactions) {
            String status = transaction.getReturnDate() != null ? "ĐÃ TRẢ" :
                    transaction.isOverdue() ? "QUÁ HẠN TRẢ" : "ĐANG MƯỢN";

            System.out.println(String.format("%-15s %-12s %-15s %-19s %-19s %-8s",
                    truncate(transaction.getTransactionId(), 15),
                    transaction.getMemberId(),
                    truncate(transaction.getBookIsbn(), 15),
                    DateUtil.formatDateTimeForDisplay(transaction.getBorrowDate()),
                    DateUtil.formatDateTimeForDisplay(transaction.getDueDate()),
                    status));

            if (transaction.getLateFee() > 0) {
                System.out.println("    └─ Phí trả muộn: $" + String.format("%.2f", transaction.getLateFee()));
            }
        }
        System.out.println(BORDER);
        System.out.println("Tổng cộng: " + transactions.size() + " giao dịch.");
    }

    public void displayMemberDetails(Member member) {
        System.out.println("\n" + BORDER);
        System.out.println("          CHI TIẾT THÀNH VIÊN");
        System.out.println(BORDER);
        System.out.println("ID Thành viên:         " + member.getMemberId());
        System.out.println("Tên:              " + member.getName());
        System.out.println("Email:             " + member.getEmail());
        System.out.println("SĐT:             " + member.getPhone());
        System.out.println("Ngày đăng ký: " + DateUtil.formatDateForDisplay(member.getRegistrationDate()));
        System.out.println("Trạng thái:            " + (member.isActive() ? "Hoạt động" : "Không hoạt động"));
        System.out.println("Sách mượn:    " + member.getBorrowedBookCount());
        System.out.println(BORDER);
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Sai input, vui lòng thử lại.");
            }
        }
    }

    public void displaySuccess(String message) {
        System.out.println("\n✅ " + message);
    }

    public void displayError(String message) {
        System.out.println("\n❌ " + message);
    }

    public void displayInfo(String message) {
        System.out.println("\nℹ️  " + message);
    }

    public void pause() {
        System.out.print("\nNhấn Enter để tiếp tục...");
        scanner.nextLine();
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    public void close() {
        scanner.close();
    }
}