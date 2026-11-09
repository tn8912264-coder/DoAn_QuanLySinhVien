package qlsv;

public class LopHoc {
    private int id;
    private String tenLop;
    private String khoa;

    public LopHoc(int id, String tenLop, String khoa) {
        this.id = id;
        this.tenLop = tenLop;
        this.khoa = khoa;
    }

    public int getId() { return id; }
    public String getTenLop() { return tenLop; }
    public String getKhoa() { return khoa; }

    @Override
    public String toString() { return tenLop; } // hiển thị trong JComboBox
}
