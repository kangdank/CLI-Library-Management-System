package com.library;

import com.library.service.LibraryService;
import com.library.ui.ConsoleUI;
import com.library.ui.MenuHandler;

public class LibraryApplication
{
    public static void main(String[] args) {
        LibraryApplication app = new LibraryApplication();
        app.run();
    }

    public void run() {
        LibraryService libraryService = LibraryService.getInstance();
        ConsoleUI ui = new ConsoleUI();
        MenuHandler menuHandler = new MenuHandler(libraryService, ui);

        try {
            libraryService.loadData();
            System.out.println("✅ Dữ liệu được tải lên thành công.");
        } catch (Exception e) {
            System.out.println("ℹ️  Không tìm thấy dữ liệu. Tải lên thư viện mới.");

            System.out.print("Bạn có muốn tải lên dữ liệu mẫu? (y/n): ");
            String response = ui.readString("").toLowerCase();
            if (response.equals("y") || response.equals("yes")) {
                libraryService.initializeSampleData();
                System.out.println("✅ Dữ liệu mẫu tạo thành công!");
            }
        }

        boolean running = true;

        System.out.println("\n" + "═".repeat(60));
        System.out.println("     Hệ thống quản lý thư viện");
        System.out.println("═".repeat(60));

        ui.pause();

        // Main application loop
        while (running) {
            try {
                ui.displayMainMenu();
                int choice = ui.readInt("Điền lựa chọn: ");

                switch (choice) {
                    case 1:
                        handleBookManagement(ui, menuHandler);
                        break;
                    case 2:
                        handleMemberManagement(ui, menuHandler);
                        break;
                    case 3:
                        handleTransactionManagement(ui, menuHandler);
                        break;
                    case 4:
                        menuHandler.handleViewStatistics();
                        ui.pause();
                        break;
                    case 5:
                        menuHandler.handleSaveData();
                        ui.pause();
                        break;
                    case 6:
                        System.out.print("\nLưu dữ liệu trước khi thoát? (y/n): ");
                        String save = ui.readString("").toLowerCase();
                        if (save.equals("y") || save.equals("yes")) {
                            menuHandler.handleSaveData();
                        }
                        running = false;
                        System.out.println("\n Đã thoát hệ thống quản lý thư viện.");
                        break;
                    default:
                        ui.displayError("Lựa chọn không hợp lệ! Hãy điền 1-6.");
                        ui.pause();
                }
            } catch (Exception e) {
                ui.displayError("Lỗi bất thường xảy ra: " + e.getMessage());
                ui.pause();
            }
        }

        ui.close();
    }

    private void handleBookManagement(ConsoleUI ui, MenuHandler menuHandler) {
        boolean back = false;

        while (!back) {
            ui.displayBookMenu();
            int choice = ui.readInt("Điền lựa chọn: ");

            switch (choice) {
                case 1:
                    menuHandler.handleAddBook();
                    break;
                case 2:
                    menuHandler.handleRemoveBook();
                    break;
                case 3:
                    menuHandler.handleSearchBooks();
                    break;
                case 4:
                    menuHandler.handleListAllBooks();
                    break;
                case 5:
                    menuHandler.handleListAvailableBooks();
                    break;
                case 6:
                    back = true;
                    continue;
                default:
                    ui.displayError("Lựa chọn không hợp lệ! Hãy điền 1-6.");
            }

            if (!back) {
                ui.pause();
            }
        }
    }

    private void handleMemberManagement(ConsoleUI ui, MenuHandler menuHandler) {
        boolean back = false;

        while (!back) {
            ui.displayMemberMenu();
            int choice = ui.readInt("Điền lựa chọn: ");

            switch (choice) {
                case 1:
                    menuHandler.handleRegisterMember();
                    break;
                case 2:
                    menuHandler.handleSearchMembers();
                    break;
                case 3:
                    menuHandler.handleListAllMembers();
                    break;
                case 4:
                    menuHandler.handleViewMemberDetails();
                    break;
                case 5:
                    menuHandler.handleDeactivateMember();
                    break;
                case 6:
                    menuHandler.handleActivateMember();
                    break;
                case 7:
                    back = true;
                    continue;
                default:
                    ui.displayError("Lựa chọn không hợp lệ! Hãy điền 1-7.");
            }

            if (!back) {
                ui.pause();
            }
        }
    }

    private void handleTransactionManagement(ConsoleUI ui, MenuHandler menuHandler) {
        boolean back = false;

        while (!back) {
            ui.displayTransactionMenu();
            int choice = ui.readInt("Điền lựa chọn: ");

            switch (choice) {
                case 1:
                    menuHandler.handleBorrowBook();
                    break;
                case 2:
                    menuHandler.handleReturnBook();
                    break;
                case 3:
                    menuHandler.handleViewMemberTransactions();
                    break;
                case 4:
                    menuHandler.handleViewOverdueBooks();
                    break;
                case 5:
                    menuHandler.handleViewRecentTransactions();
                    break;
                case 6:
                    back = true;
                    continue;
                default:
                    ui.displayError("Lựa chọn không hợp lệ! Hãy điền 1-6.");
            }

            if (!back) {
                ui.pause();
            }
        }
    }
}
