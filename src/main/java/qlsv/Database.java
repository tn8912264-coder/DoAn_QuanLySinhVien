package qlsv;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;

/** Kết nối CSDL SQLite và khởi tạo bảng + dữ liệu mẫu. */
public class Database {
    private static final String URL = "jdbc:sqlite:quanlysinhvien.db";

    public static Connection getConnection() throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        try (Statement st = c.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return c;
    }

    public static String hash(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            StringBuilder sb = new StringBuilder();
            for (byte b : md.digest(s.getBytes(StandardCharsets.UTF_8))) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() throws SQLException {
        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS tai_khoan ("
                    + "username TEXT PRIMARY KEY, password TEXT NOT NULL)");
            st.execute("CREATE TABLE IF NOT EXISTS lop ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "ten_lop TEXT NOT NULL UNIQUE, khoa TEXT)");
            st.execute("CREATE TABLE IF NOT EXISTS sinh_vien ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "ma_sv TEXT NOT NULL UNIQUE, ho_ten TEXT NOT NULL,"
                    + "ngay_sinh TEXT, gioi_tinh TEXT, email TEXT,"
                    + "lop_id INTEGER REFERENCES lop(id))");

            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM tai_khoan")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement ps = c.prepareStatement(
                            "INSERT INTO tai_khoan VALUES (?, ?)")) {
                        ps.setString(1, "admin");
                        ps.setString(2, hash("123456"));
                        ps.executeUpdate();
                    }
                }
            }
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM lop")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    st.execute("INSERT INTO lop(ten_lop, khoa) VALUES "
                            + "('CNTT1', 'Công nghệ thông tin'),"
                            + "('CNTT2', 'Công nghệ thông tin'),"
                            + "('KT1', 'Kinh tế')");
                }
            }
        }
    }
}
