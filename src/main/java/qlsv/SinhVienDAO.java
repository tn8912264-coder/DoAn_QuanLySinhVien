package qlsv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SinhVienDAO {
    /** Lấy danh sách; keyword rỗng thì lấy tất cả, ngược lại tìm theo mã/tên/email. */
    public List<SinhVien> search(String keyword) throws SQLException {
        List<SinhVien> list = new ArrayList<>();
        String sql = "SELECT s.id, s.ma_sv, s.ho_ten, s.ngay_sinh, s.gioi_tinh, s.email, "
                + "s.lop_id, IFNULL(l.ten_lop, '') FROM sinh_vien s "
                + "LEFT JOIN lop l ON s.lop_id = l.id "
                + "WHERE s.ma_sv LIKE ? OR s.ho_ten LIKE ? OR s.email LIKE ? ORDER BY s.id";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String k = "%" + keyword.trim() + "%";
            ps.setString(1, k);
            ps.setString(2, k);
            ps.setString(3, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new SinhVien(rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6),
                            rs.getInt(7), rs.getString(8)));
                }
            }
        }
        return list;
    }

    public void insert(SinhVien s) throws SQLException {
        String sql = "INSERT INTO sinh_vien(ma_sv, ho_ten, ngay_sinh, gioi_tinh, email, lop_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getMaSV());
            ps.setString(2, s.getHoTen());
            ps.setString(3, s.getNgaySinh());
            ps.setString(4, s.getGioiTinh());
            ps.setString(5, s.getEmail());
            ps.setInt(6, s.getLopId());
            ps.executeUpdate();
        }
    }

    public void update(SinhVien s) throws SQLException {
        String sql = "UPDATE sinh_vien SET ma_sv=?, ho_ten=?, ngay_sinh=?, gioi_tinh=?, "
                + "email=?, lop_id=? WHERE id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getMaSV());
            ps.setString(2, s.getHoTen());
            ps.setString(3, s.getNgaySinh());
            ps.setString(4, s.getGioiTinh());
            ps.setString(5, s.getEmail());
            ps.setInt(6, s.getLopId());
            ps.setInt(7, s.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM sinh_vien WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
