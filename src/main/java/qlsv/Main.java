package qlsv;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            Database.init();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không khởi tạo được CSDL: " + e.getMessage());
            return;
        }
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
