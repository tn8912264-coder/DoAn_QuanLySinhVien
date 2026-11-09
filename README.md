# Quản lý sinh viên (Java Swing + SQLite)

## Yêu cầu
- JDK 17 trở lên
- VS Code + extension **Extension Pack for Java** (đã gồm Maven for Java)

## Chạy trong VS Code
1. File > Open Folder... > chọn thư mục `QuanLySinhVien`
2. Chờ VS Code tải xong thư viện (thư viện sqlite-jdbc tự tải qua Maven)
3. Mở `src/main/java/qlsv/Main.java` > bấm **Run** (phía trên hàm main)

Hoặc dùng terminal:  `mvn compile exec:java -Dexec.mainClass=qlsv.Main`

## Tài khoản mặc định
- Tên đăng nhập: `admin`
- Mật khẩu: `123456`

## 3 form
1. LoginForm  – đăng nhập
2. StudentForm – thêm / sửa / xóa / tìm kiếm sinh viên
3. ClassForm  – thêm / sửa / xóa lớp học

CSDL SQLite tự tạo file `quanlysinhvien.db` ở lần chạy đầu tiên.
