package com.library.ui;

import com.library.exception.BookNotFoundException;
import com.library.exception.InvalidOperationException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.service.LibraryService;

import java.util.List;

public class MenuHandler {
    private final LibraryService libraryService;
    private final ConsoleUI ui;

    public MenuHandler(LibraryService libraryService, ConsoleUI ui) {
        this.libraryService = libraryService;
        this.ui = ui;
    }

    public void handleAddBook() {
        try {
            String isbn = ui.readString("Điền ISBN: ");
            String title = ui.readString("Điền tên sách: ");
            String author = ui.readString("Điền tác giả: ");
            int year = ui.readInt("Điền năm xuất bản: ");

            Book book = libraryService.getBookService().addBook(isbn, title, author, year);
            ui.displaySuccess("Thêm sách mới thành công. ISBN: " + book.getIsbn());
        } catch (InvalidOperationException e) {
            ui.displayError(e.getMessage());
        }
    }

    public void handleRemoveBook() {
        try {
            String isbn = ui.readString("Điền IBSN của sách cần xóa: ");
            libraryService.getBookService().removeBook(isbn);
            ui.displaySuccess("Xóa sách thành công!");
        } catch (BookNotFoundException | InvalidOperationException e) {
            ui.displayError(e.getMessage());
        }
    }

    public void handleSearchBooks() {
        System.out.println("\nTìm với:");
        System.out.println("1. Tên sách");
        System.out.println("2. Tác giả");
        System.out.println("3. ISBN");
        System.out.println("4. Từ khóa (tất cả)");

        int choice = ui.readInt("\nEnter choice: ");
        List<Book> books;

        try {
            switch (choice) {
                case 1:
                    String title = ui.readString("Điền tên sách: ");
                    books = libraryService.getBookService().searchBooksByTitle(title);
                    break;
                case 2:
                    String author = ui.readString("Điền tên tác giả: ");
                    books = libraryService.getBookService().searchBooksByAuthor(author);
                    break;
                case 3:
                    String isbn = ui.readString("Điền ISBN: ");
                    try {
                        Book book = libraryService.getBookService().findBookByIsbn(isbn);
                        books = List.of(book);
                    } catch (BookNotFoundException e) {
                        ui.displayError(e.getMessage());
                        return;
                    }
                    break;
                case 4:
                    String keyword = ui.readString("Điền từ khóa: ");
                    books = libraryService.getBookService().searchBooks(keyword);
                    break;
                default:
                    ui.displayError("Sai lựa chọn!");
                    return;
            }

            ui.displayBooks(books);
        } catch (Exception e) {
            ui.displayError("Lỗi khi tìm sách: " + e.getMessage());
        }
    }

    public void handleListAllBooks() {
        List<Book> books = libraryService.getBookService().getAllBooks();
        ui.displayBooks(books);
    }

    public void handleListAvailableBooks() {
        List<Book> books = libraryService.getBookService().getAvailableBooks();
        ui.displayBooks(books);
    }

    public void handleRegisterMember() {
        try {
            String name = ui.readString("Điền tên: ");
            String email = ui.readString("Điền Email: ");
            String phone = ui.readString("Điền số điện thoại: ");

            Member member = libraryService.getMemberService().registerMember(name, email, phone);
            ui.displaySuccess("Đăng ký thành viên thành công! ID: " + member.getMemberId());
        } catch (InvalidOperationException e) {
            ui.displayError(e.getMessage());
        }
    }

    public void handleSearchMembers() {
        String name = ui.readString("Điền tên thành viên: ");
        List<Member> members = libraryService.getMemberService().searchMembersByName(name);
        ui.displayMembers(members);
    }

    public void handleListAllMembers() {
        List<Member> members = libraryService.getMemberService().getAllMembers();
        ui.displayMembers(members);
    }

    public void handleViewMemberDetails() {
        try {
            String memberId = ui.readString("Điền ID thành viên: ");
            Member member = libraryService.getMemberService().findMemberById(memberId);
            ui.displayMemberDetails(member);

            List<Transaction> activeTransactions =
                    libraryService.getTransactionService().getMemberActiveTransactions(memberId);

            if (!activeTransactions.isEmpty()) {
                System.out.println("\nSách hiện đang mượn:");
                ui.displayTransactions(activeTransactions);
            }
        } catch (MemberNotFoundException e) {
            ui.displayError(e.getMessage());
        }
    }

    public void handleDeactivateMember() {
        try {
            String memberId = ui.readString("Điền ID thành viên để vô hiệu hóa: ");
            libraryService.getMemberService().deactivateMember(memberId);
            ui.displaySuccess("Thành viên được vô hiệu hóa thành công!");
        } catch (MemberNotFoundException | InvalidOperationException e) {
            ui.displayError(e.getMessage());
        }
    }
    public void handleActivateMember() {
        try {
            String memberId = ui.readString("Điền ID thành viên để tái kích hoạt: ");
            libraryService.getMemberService().activateMember(memberId);
            ui.displaySuccess("Thành viên được kích hoạt thành công!");
        } catch (MemberNotFoundException | InvalidOperationException e) {
            ui.displayError(e.getMessage());
        }
    }

    public void handleBorrowBook() {
        try {
            String memberId = ui.readString("Điền ID thành viên: ");
            String isbn = ui.readString("Điền ISBN sách: ");

            Transaction transaction = libraryService.getTransactionService()
                    .borrowBook(memberId, isbn);

            ui.displaySuccess("Sách được mượn thành công!");
            ui.displayInfo("ID Giao dịch: " + transaction.getTransactionId());
            ui.displayInfo("Ngày trả: " + transaction.getDueDate());
        } catch (MemberNotFoundException | BookNotFoundException | InvalidOperationException e) {
            ui.displayError(e.getMessage());
        }
    }

    public void handleReturnBook() {
        try {
            String memberId = ui.readString("Điền ID thành viên: ");
            String isbn = ui.readString("Điền ISBN sách: ");

            Transaction transaction = libraryService.getTransactionService()
                    .returnBook(memberId, isbn);

            ui.displaySuccess("Sách được trả thành công!");

            if (transaction.getLateFee() > 0) {
                ui.displayInfo(String.format("Phí trả muộn: $%.2f (%d ngày muộn)",
                        transaction.getLateFee(),
                        transaction.getDaysOverdue()));
            }
        } catch (MemberNotFoundException | BookNotFoundException | InvalidOperationException e) {
            ui.displayError(e.getMessage());
        }
    }

    public void handleViewMemberTransactions() {
        try {
            String memberId = ui.readString("Điền ID thành viên: ");
            List<Transaction> transactions = libraryService.getTransactionService()
                    .getMemberTransactions(memberId);
            ui.displayTransactions(transactions);
        } catch (Exception e) {
            ui.displayError("Lỗi khi tìm kiếm thông tin giao dịch: " + e.getMessage());
        }
    }

    public void handleViewOverdueBooks() {
        List<Transaction> overdueTransactions = libraryService.getTransactionService()
                .getOverdueTransactions();

        if (overdueTransactions.isEmpty()) {
            ui.displayInfo("Không có sách trả muộn!");
        } else {
            System.out.println("\n⚠️  SÁCH TRẢ MUỘN ⚠️");
            ui.displayTransactions(overdueTransactions);
        }
    }

    public void handleViewRecentTransactions() {
        int limit = ui.readInt("Điền số lượng giao dịch gần đây để hiển thị: ");
        List<Transaction> transactions = libraryService.getTransactionService()
                .getRecentTransactions(limit);
        ui.displayTransactions(transactions);
    }

    public void handleViewStatistics() {
        String statistics = libraryService.getLibraryStatistics();
        System.out.println(statistics);
    }

    public void handleSaveData() {
        try {
            libraryService.saveData();
            ui.displaySuccess("Lưu dữ liệu thành công!");
        } catch (Exception e) {
            ui.displayError("Lỗi khi lưu dữ liệu: " + e.getMessage());
        }
    }
}