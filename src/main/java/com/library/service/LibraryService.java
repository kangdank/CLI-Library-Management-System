package com.library.service;

import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;
import com.library.repository.TransactionRepository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/*
 * Main library service facade providing unified access to all services.
 * Service facade hợp nhất truy cập cho các service khác
 * Facade pattern, Singleton pattern, I/O, Serialization
 */
public class LibraryService {
    private static LibraryService instance;
    private static final String DATA_FILE = "library_data.ser";

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    private final BookService bookService;
    private final MemberService memberService;
    private final TransactionService transactionService;

    private LibraryService() {
        this.bookRepository = new BookRepository();
        this.memberRepository = new MemberRepository();
        this.transactionRepository = new TransactionRepository();

        this.bookService = new BookService(bookRepository);
        this.memberService = new MemberService(memberRepository);
        this.transactionService = new TransactionService(
                transactionRepository, bookService, memberService);
    }

    /*
     * Sử dụng synchronized để singleton thread-safe
     */
    public static synchronized LibraryService getInstance() {
        if (instance == null) {
            instance = new LibraryService();
        }
        return instance;
    }

    public BookService getBookService() {
        return bookService;
    }

    public MemberService getMemberService() {
        return memberService;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

    /*
     * Lưu dữ liệu ra file
     */
    public void saveData() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("books", bookRepository.findAll());
        data.put("members", memberRepository.findAll());
        data.put("transactions", transactionRepository.findAll());

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(data);
        }
    }

    /*
     * Load dữ liệu từ file
     */
    @SuppressWarnings("unchecked")
    public void loadData() throws IOException, ClassNotFoundException {
        File file = new File(DATA_FILE);
        if (!file.exists() || file.length() == 0) {
            throw new FileNotFoundException("Không tìm thấy file dữ liệu");
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            Map<String, Object> data = (Map<String, Object>) ois.readObject();

            // Xóa dữ liệu tồn tại
            bookRepository.clear();
            memberRepository.clear();
            transactionRepository.clear();

            // Load sách
            if (data.containsKey("books")) {
                ((java.util.List<com.library.model.Book>) data.get("books"))
                        .forEach(bookRepository::save);
            }

            // Load thành viên
            if (data.containsKey("members")) {
                ((java.util.List<com.library.model.Member>) data.get("members"))
                        .forEach(memberRepository::save);
            }

            // Load giao dịch
            if (data.containsKey("transactions")) {
                ((java.util.List<com.library.model.Transaction>) data.get("transactions"))
                        .forEach(transactionRepository::save);
            }
        }
    }


    public String getLibraryStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("\n╔═══════════════════════════════════════════╗\n");
        stats.append("║     THỐNG KÊ THƯ VIỆN                     ║\n");
        stats.append("╠═══════════════════════════════════════════╣\n");
        stats.append(String.format("║ Tổng sách:                    %-12d║\n", bookService.getTotalBookCount()));
        stats.append(String.format("║ Sách còn:                     %-12d║\n", bookService.getAvailableBookCount()));
        stats.append(String.format("║ Sách đã cho mượn:             %-12d║\n",
                bookService.getTotalBookCount() - bookService.getAvailableBookCount()));
        stats.append("╠═══════════════════════════════════════════╣\n");
        stats.append(String.format("║ Tổng thành viên:              %-12d║\n", memberService.getTotalMemberCount()));
        stats.append(String.format("║ Thành viên hoạt động:         %-12d║\n", memberService.getActiveMemberCount()));
        stats.append("╠═══════════════════════════════════════════╣\n");
        stats.append(String.format("║ Tổng giao dịch:               %-12d║\n", transactionService.getTotalTransactionCount()));
        stats.append(String.format("║ Giao dịch đang diễn ra:       %-12d║\n", transactionService.getActiveTransactionCount()));
        stats.append(String.format("║ Giao dịch trả muộn:           %-12d║\n", transactionService.getOverdueTransactions().size()));
        stats.append(String.format("║ Tổng tiền phạt trả muộn:      $%-11.2f║\n", transactionService.getTotalLateFees()));
        stats.append("╚═══════════════════════════════════════════╝\n");
        return stats.toString();
    }

    /*
     * Khởi tạo mẫu
     */
    public void initializeSampleData() {
        try {
            bookService.addBook("978-6049950421", "Hoàng tử bé", "Antoine de Saint-Exupéry", 2007);
            bookService.addBook("978-1784877996", "Rừng Na Uy", "Haruki Murakami", 2022);
            bookService.addBook("978-1908696519", "The Prince", "Niccolo Machiavelli ", 2012);
            bookService.addBook("978-6043199789", "Nhà Giả Kim", "Paulo Coelho", 2013);
            bookService.addBook("978-6048898809", "Dám bị ghét", "Kishimi Ichiro", 2022);

            memberService.registerMember("Đặng Văn A", "dangvan@email.com", "1234567890");
            memberService.registerMember("Nguyễn Thị B", "nguyenthi@email.com", "0987654321");
            memberService.registerMember("Phạm Long C", "phamlong@email.com", "7777777777");

        } catch (Exception e) {
            System.err.println("Lỗi khi khởi tạo mẫu dữ liệu: " + e.getMessage());
        }
    }
}