package qlsv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LopDAO {
    public List<LopHoc> getAll() throws SQLException {
        List<LopHoc> list = new ArrayList<>();
        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, ten_lop, khoa FROM lop ORDER BY id")) {
            while (rs.next())
                list.add(new LopHoc(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        return list;
    }

    public void insert(String ten, String khoa) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO lop(ten_lop, khoa) VALUES (?, ?)")) {
            ps.setString(1, ten);
            ps.setString(2, khoa);
            ps.executeUpdate();
        }
    }

    public void update(int id, String ten, String khoa) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE lop SET ten_lop=?, khoa=? WHERE id=?")) {
            ps.setString(1, ten);
            ps.setString(2, khoa);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM lop WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
