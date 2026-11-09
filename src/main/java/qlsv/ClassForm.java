package qlsv;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

/** FORM 3: Quản lý lớp học (thêm - sửa - xóa). */
public class ClassForm extends JFrame {
    private final LopDAO dao = new LopDAO();
    private final Runnable onChange;

    private final JTextField txtTen = new JTextField(15);
    private final JTextField txtKhoa = new JTextField(15);
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Tên lớp", "Khoa"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private int selectedId = -1;

    public ClassForm(Runnable onChange) {
        super("Quản lý lớp học");
        this.onChange = onChange;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel input = new JPanel(new GridLayout(2, 2, 8, 8));
        input.setBorder(BorderFactory.createTitledBorder("Thông tin lớp"));
        input.add(new JLabel("Tên lớp:")); input.add(txtTen);
        input.add(new JLabel("Khoa:"));    input.add(txtKhoa);

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnMoi = new JButton("Làm mới");
        JPanel buttons = new JPanel();
        buttons.add(btnThem); buttons.add(btnSua); buttons.add(btnXoa); buttons.add(btnMoi);

        JPanel top = new JPanel(new BorderLayout());
        top.add(input, BorderLayout.NORTH);
        top.add(buttons, BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnMoi.addActionListener(e -> lamMoi());
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (!e.getValueIsAdjusting() && row >= 0) {
                selectedId = (int) model.getValueAt(row, 0);
                txtTen.setText(String.valueOf(model.getValueAt(row, 1)));
                txtKhoa.setText(String.valueOf(model.getValueAt(row, 2)));
            }
        });
        loadTable();
    }

    private void loadTable() {
        try {
            model.setRowCount(0);
            for (LopHoc l : dao.getAll())
                model.addRow(new Object[]{l.getId(), l.getTenLop(), l.getKhoa()});
        } catch (SQLException ex) {
            loi(ex);
        }
    }

    private void done() {
        loadTable();
        lamMoi();
        if (onChange != null) onChange.run(); // cập nhật combobox ở form sinh viên
    }

    private void them() {
        String ten = txtTen.getText().trim();
        if (ten.isEmpty()) { JOptionPane.showMessageDialog(this, "Tên lớp không được để trống!"); return; }
        try {
            dao.insert(ten, txtKhoa.getText().trim());
            done();
        } catch (SQLException ex) {
            if (ex.getMessage().contains("UNIQUE")) JOptionPane.showMessageDialog(this, "Tên lớp đã tồn tại!");
            else loi(ex);
        }
    }

    private void sua() {
        if (selectedId < 0) { JOptionPane.showMessageDialog(this, "Hãy chọn một lớp để sửa!"); return; }
        String ten = txtTen.getText().trim();
        if (ten.isEmpty()) { JOptionPane.showMessageDialog(this, "Tên lớp không được để trống!"); return; }
        try {
            dao.update(selectedId, ten, txtKhoa.getText().trim());
            done();
        } catch (SQLException ex) {
            if (ex.getMessage().contains("UNIQUE")) JOptionPane.showMessageDialog(this, "Tên lớp đã tồn tại!");
            else loi(ex);
        }
    }

    private void xoa() {
        if (selectedId < 0) { JOptionPane.showMessageDialog(this, "Hãy chọn một lớp để xóa!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa lớp này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            dao.delete(selectedId);
            done();
        } catch (SQLException ex) {
            if (ex.getMessage().contains("FOREIGN KEY"))
                JOptionPane.showMessageDialog(this, "Lớp đang có sinh viên, không thể xóa!");
            else loi(ex);
        }
    }

    private void lamMoi() {
        selectedId = -1;
        txtTen.setText("");
        txtKhoa.setText("");
        table.clearSelection();
    }

    private void loi(Exception ex) {
        JOptionPane.showMessageDialog(this, "Lỗi CSDL: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
