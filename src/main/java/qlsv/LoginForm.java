package qlsv;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/** FORM 1: Đăng nhập. */
public class LoginForm extends JFrame {
    private final JTextField txtUser = new JTextField(15);
    private final JPasswordField txtPass = new JPasswordField(15);

    public LoginForm() {
        super("Đăng nhập hệ thống");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("QUẢN LÝ SINH VIÊN", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        form.add(new JLabel("Tên đăng nhập:"));
        form.add(txtUser);
        form.add(new JLabel("Mật khẩu:"));
        form.add(txtPass);

        JButton btnLogin = new JButton("Đăng nhập");
        JButton btnExit = new JButton("Thoát");
        JPanel buttons = new JPanel();
        buttons.add(btnLogin);
        buttons.add(btnExit);

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> login());
        btnExit.addActionListener(e -> System.exit(0));
        txtPass.addActionListener(e -> login());
        getRootPane().setDefaultButton(btnLogin);

        pack();
        setLocationRelativeTo(null);
    }

    private void login() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT 1 FROM tai_khoan WHERE username=? AND password=?")) {
            ps.setString(1, user);
            ps.setString(2, Database.hash(pass));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    new StudentForm().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi CSDL: " + ex.getMessage());
        }
    }
}
