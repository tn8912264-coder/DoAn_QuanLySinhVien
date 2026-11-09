package qlsv;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/** FORM 2: Quản lý sinh viên (thêm - sửa - xóa - tìm kiếm). */
public class StudentForm extends JFrame {
    private final SinhVienDAO svDAO = new SinhVienDAO();
    private final LopDAO lopDAO = new LopDAO();

    private final JTextField txtMa = new JTextField(15);
    private final JTextField txtTen = new JTextField(15);
    private final JTextField txtNgaySinh = new JTextField(15);
    private final JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
    private final JTextField txtEmail = new JTextField(15);
    private final JComboBox<LopHoc> cboLop = new JComboBox<>();
    private final JTextField txtTimKiem = new JTextField(20);

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Mã SV", "Họ tên", "Ngày sinh", "Giới tính", "Email", "Lớp"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private int selectedId = -1;

    public StudentForm() {
        super("Quản lý sinh viên");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 560);
        setLocationRelativeTo(null);

        // ----- Panel nhập liệu -----
        JPanel input = new JPanel(new GridLayout(3, 4, 10, 10));
        input.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));
        input.add(new JLabel("Mã SV:"));          input.add(txtMa);
        input.add(new JLabel("Họ tên:"));         input.add(txtTen);
        input.add(new JLabel("Ngày sinh (yyyy-MM-dd):")); input.add(txtNgaySinh);
        input.add(new JLabel("Giới tính:"));      input.add(cboGioiTinh);
        input.add(new JLabel("Email:"));          input.add(txtEmail);
        input.add(new JLabel("Lớp:"));            input.add(cboLop);

        // ----- Nút chức năng -----
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnMoi = new JButton("Làm mới");
        JButton btnLop = new JButton("Quản lý lớp");
        JButton btnDangXuat = new JButton("Đăng xuất");
        JPanel buttons = new JPanel();
        for (JButton b : new JButton[]{btnThem, btnSua, btnXoa, btnMoi, btnLop, btnDangXuat}) buttons.add(b);

        // ----- Tìm kiếm -----
        JButton btnTim = new JButton("Tìm kiếm");
        JButton btnTatCa = new JButton("Hiện tất cả");
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.add(new JLabel("Từ khóa (mã/tên/email):"));
        search.add(txtTimKiem);
        search.add(btnTim);
        search.add(btnTatCa);

        JPanel top = new JPanel(new BorderLayout());
        top.add(input, BorderLayout.NORTH);
        top.add(buttons, BorderLayout.CENTER);
        top.add(search, BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(50);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ----- Sự kiện -----
        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnMoi.addActionListener(e -> lamMoi());
        btnTim.addActionListener(e -> loadTable(txtTimKiem.getText()));
        txtTimKiem.addActionListener(e -> loadTable(txtTimKiem.getText()));
        btnTatCa.addActionListener(e -> { txtTimKiem.setText(""); loadTable(""); });
        btnLop.addActionListener(e -> new ClassForm(this::loadLop).setVisible(true));
        btnDangXuat.addActionListener(e -> { new LoginForm().setVisible(true); dispose(); });
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromTable();
        });

        loadLop();
        loadTable("");
    }

    private void loadLop() {
        try {
            cboLop.removeAllItems();
            for (LopHoc l : lopDAO.getAll()) cboLop.addItem(l);
        } catch (SQLException ex) {
            loi(ex);
        }
    }

    private void loadTable(String keyword) {
        try {
            model.setRowCount(0);
            List<SinhVien> list = svDAO.search(keyword);
            for (SinhVien s : list) {
                model.addRow(new Object[]{s.getId(), s.getMaSV(), s.getHoTen(), s.getNgaySinh(),
                        s.getGioiTinh(), s.getEmail(), s.getTenLop()});
            }
        } catch (SQLException ex) {
            loi(ex);
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedId = (int) model.getValueAt(row, 0);
        txtMa.setText(String.valueOf(model.getValueAt(row, 1)));
        txtTen.setText(String.valueOf(model.getValueAt(row, 2)));
        txtNgaySinh.setText(String.valueOf(model.getValueAt(row, 3)));
        cboGioiTinh.setSelectedItem(model.getValueAt(row, 4));
        txtEmail.setText(String.valueOf(model.getValueAt(row, 5)));
        String tenLop = String.valueOf(model.getValueAt(row, 6));
        for (int i = 0; i < cboLop.getItemCount(); i++) {
            if (cboLop.getItemAt(i).getTenLop().equals(tenLop)) { cboLop.setSelectedIndex(i); break; }
        }
    }

    private SinhVien readForm() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String ns = txtNgaySinh.getText().trim();
        LopHoc lop = (LopHoc) cboLop.getSelectedItem();
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã SV và Họ tên không được để trống!");
            return null;
        }
        if (!ns.isEmpty() && !ns.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Ngày sinh phải có dạng yyyy-MM-dd (vd: 2005-09-20)!");
            return null;
        }
        if (lop == null) {
            JOptionPane.showMessageDialog(this, "Chưa có lớp nào, hãy thêm lớp trước!");
            return null;
        }
        return new SinhVien(selectedId, ma, ten, ns, (String) cboGioiTinh.getSelectedItem(),
                txtEmail.getText().trim(), lop.getId(), lop.getTenLop());
    }

    private void them() {
        SinhVien s = readForm();
        if (s == null) return;
        try {
            svDAO.insert(s);
            loadTable("");
            lamMoi();
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("UNIQUE"))
                JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại!");
            else loi(ex);
        }
    }

    private void sua() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một sinh viên trong bảng để sửa!");
            return;
        }
        SinhVien s = readForm();
        if (s == null) return;
        try {
            svDAO.update(s);
            loadTable("");
            lamMoi();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("UNIQUE"))
                JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại!");
            else loi(ex);
        }
    }

    private void xoa() {
        if (selectedId < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một sinh viên trong bảng để xóa!");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        try {
            svDAO.delete(selectedId);
            loadTable("");
            lamMoi();
        } catch (SQLException ex) {
            loi(ex);
        }
    }

    private void lamMoi() {
        selectedId = -1;
        txtMa.setText("");
        txtTen.setText("");
        txtNgaySinh.setText("");
        txtEmail.setText("");
        cboGioiTinh.setSelectedIndex(0);
        if (cboLop.getItemCount() > 0) cboLop.setSelectedIndex(0);
        table.clearSelection();
    }

    private void loi(Exception ex) {
        JOptionPane.showMessageDialog(this, "Lỗi CSDL: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
