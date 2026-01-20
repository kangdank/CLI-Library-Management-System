# Library Management System (CLI)

> Hệ thống quản lý thư viện CLI viết bằng Java và test sử dụng Junit 5.
Dự án được thiết kế để thể hiện các kỹ năng cơ bản của Java và cấu trúc dự án gọn gàng.


---

## Tính năng

### Quản lý sách
- **Thêm sách** - Tạo sách mới bằng chuẩn IBSN
- **Xóa sách** - Xóa sách với kiểm tra điều kiện (không thể xóa sách đang mượn)
- **Tìm sách** - Tìm sách bằng tên sách, tên tác giả, ISBN hoặc keyword
- **Danh sách sách** - Liệt kê tất cả sách và trạng thái của sách
- **Trạng thái sách** - Theo dõi trạng thái của sách

### Quản lý thành viên
- **Đăng ký thành viên** - Thêm thành viên mới  với email và số điện thoại hợp lệ
- **Tìm kiếm thành viên** - Tìm kiếm qua tên hoặc ID
- **Xem chi tiết** - Thông tin thành viên và sách đang mượn
- **Quản lý trạng thái tài khoản** - Kích hoạt/Hủy kích hoạt thành viên

### Transaction Management
- **Mượn sách** - Mượn sách (14 ngày)
- **Trả sách** - Trả sách và tính toán phí trả muộn
- **Lịch sử giao dịch** - Xem lịch sử giao dic của thành viên
- **Theo dõi giao dịch quá hạn** - Xác định và theo dõi sách trả muộn
- **Giao dịch gần đây** - Hiển thị những giao dịch gần nhất

### Tính năng
- **Lưu trữ dữ liệu** - Lưu/xóa dữ liệu thư viện
- **Kiểm thử đầu vào** - Kiểm tra tính hợp lệ của dữ liệu

---

## Mục tiêu
Dự án được thiết kế để thể hiện:
- **Lập trình hướng đối tượng**: Sử dụng class và các tính chất cơ bản của OOP như kế thừa, đa hình,...
- **Design Patterns**: Sử dụng mẫu thiết kế Singleton, Facade, Repository
- **Cấu trúc dữ liệu**: Sử dụng Java Collection Framework như List, Map
- **Tính năng Java hiện đại**: Stream, Lambda expression
- **Kiểm thử**: Sử dụng Junit 5

---

## Cấu trúc File dự án

```
CLILibraryManagment/
├── pom.xml
├── .gitignore
├── README.md
├── src/
    ├── main/
    │   └── java/com/library/
    │       ├── LibraryApplication.java
    │       ├── model/
    │       │   ├── Book.java
    │       │   ├── BookStatus.java
    │       │   ├── Member.java
    │       │   └── Transaction.java
    │       ├── repository/
    │       │   ├── BookRepository.java
    │       │   ├── MemberRepository.java
    │       │   └── TransactionRepository.java
    │       ├── service/
    │       │   ├── BookService.java
    │       │   ├── MemberService.java
    │       │   ├── TransactionService.java
    │       │   └── LibraryService.java
    │       ├── ui/
    │       │   ├── ConsoleUI.java
    │       │   └── MenuHandler.java
    │       ├── exception/
    │       │   ├── BookNotFoundException.java
    │       │   ├── MemberNotFoundException.java
    │       │   └── InvalidOperationException.java
    │       └── util/
    │           ├── InputValidator.java
    │           └── DateUtil.java
    │   
    │
    └── test/
        └── java/com/library/
            ├── service/
            │   ├── BookServiceTest.java
            │   └── MemberServiceTest.java
            └── util/
                └── InputValidatorTest.java

```

---

## Setup & Build chương trình

### Điều kiện trước hết

Kiểm tra môi trường để chương trình

```bash
# Phiên bản Java (17+)
java -version

# Phiên bản Maven (3.6+)
mvn -version

# Phiên bản git
git --version
```

### Tải xuống hoặc clone repo

Sử dụng Git
```bash
git clone https://github.com/kangdank/CLI-Library-Management-System
cd CLI-Library-Management-System
```

### Build chương trình

Ở thư mục gốc, chạy:

```bash
mvn clean compile
```

### Đóng gói

Tạo tệp thực thi JAR:

```bash
mvn clean package
```

Tạo ra 2 tệp ở đường dẫn `target/`:
- `CLILibraryManagment-1.0-SNAPSHOT`
- `CLILibraryManagment-1.0-SNAPSHOT-standalone` - Fat JAR (bao gồm tất cả independency)

---

## ▶Chạy chương trình

### Cách 1: Sử dụng Maven

```bash
mvn exec:java
```

### Cách 2: Sử dụng standalone JAR

```bash
java -jar target/CLILibraryManagment-1.0-SNAPSHOT-standalone.jar
```

### Cách 3: Sử dụng IntelliJ IDEA

1. Mở dự án sử dụng IntelliJ IDEA
2. Đến `src/main/java/com/library/LibraryApplication.java`
3. Chuột phải → **Run 'LibraryApplication.main()'**

### Lần chạy đầu

1. Thông báo xuất hiện: `"Không tìm thấy dữ liệu. Tải lên thư viện mới."`
2. Kế đến: `"Bạn có muốn tải lên dữ liệu mẫu? (y/n): "`
    - `y` để tải lên dữ liệu dựng sẵn
    - `n` để tạo thư viện trống

---

## Kiểm thử

### Chạy tất cả các test

```bash
mvn test
```

### Phạm vi kiểm thử

The project includes comprehensive unit tests covering:
- Xác minh đầu vào (email, SĐT, ISBN, ...)
- Thao tác với sách (thêm, xóa, tìm kiếm)
- Thao tác với thành viên (đăng ký, kích hoạt)
- Một số trường hợp exception handling
- Kiểm thử logic nghiệp vụ

---
\
\
\
\
\
\
\
\
\
\
\
**[Đặng Phú Khang]**
- GitHub: [https://github.com/kangdank](https://github.com/yourusername)
- LinkedIn: [www.linkedin.com/in/dang-khang-b507b732b](https://linkedin.com/in/yourprofile)
- Email: khangdangcv@gmail.com




