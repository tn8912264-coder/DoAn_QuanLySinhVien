package qlsv;

public class SinhVien {
    private int id;
    private String maSV, hoTen, ngaySinh, gioiTinh, email, tenLop;
    private int lopId;

    public SinhVien(int id, String maSV, String hoTen, String ngaySinh,
                    String gioiTinh, String email, int lopId, String tenLop) {
        this.id = id;
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.email = email;
        this.lopId = lopId;
        this.tenLop = tenLop;
    }

    public int getId() { return id; }
    public String getMaSV() { return maSV; }
    public String getHoTen() { return hoTen; }
    public String getNgaySinh() { return ngaySinh; }
    public String getGioiTinh() { return gioiTinh; }
    public String getEmail() { return email; }
    public int getLopId() { return lopId; }
    public String getTenLop() { return tenLop; }
}
