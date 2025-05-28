package guru;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ndita
 */
import config.DatabaseConnection;
import guru.FormData;
import java.awt.HeadlessException;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;



public class KelolaDataSiswa extends javax.swing.JFrame {
    int idUserLogin;
    private DefaultTableModel model;
    private String nisLama;


    /**
     * Creates new form KelolaNilaiSiswa
     */
  public KelolaDataSiswa(int IDUser) {
        this.idUserLogin = IDUser;
        initComponents();
        setLocationRelativeTo(null); 
        
        model = new DefaultTableModel(new String[]{"id User", "NIS", "Nama", "Jenis Kelamin", "Kelas" }, 0);
        tblSiswa.setModel(model);
        
    loadData();
    
    tblSiswa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblSiswa.getSelectedRow();
                if (selectedRow != -1) {
                    idUser.setText(model.getValueAt(selectedRow, 0).toString()); // Kolom ke-0: id User
                    nisLama = model.getValueAt(selectedRow, 1).toString(); // Simpan NIS lama
                    txtNIS.setText(nisLama); // Kolom ke-1: NIS
                    txtNama.setText(model.getValueAt(selectedRow, 2).toString()); // Kolom ke-2: Nama
                    String jk = model.getValueAt(selectedRow, 3).toString();
                    if (jk.equals("L")) {
                        cbJenisKelamin.setSelectedItem("Laki-Laki");
                    } else if (jk.equals("P")) {
                        cbJenisKelamin.setSelectedItem("Perempuan");
                    } else {
                        cbJenisKelamin.setSelectedIndex(0);
                    }
                    txtKelas.setText(model.getValueAt(selectedRow, 4).toString());
                }
            }
        });
    }
  
   // Metode untuk memvalidasi id_user siswa
    private boolean validateIdUserSiswa(String idUser, String nisToExclude) {
        try {
            int id = Integer.parseInt(idUser); // Pastikan id_user adalah angka
            Connection conn = DatabaseConnection.getConnection();

            // Cek apakah id_user ada di tabel Login dengan role "siswa"
            String sql = "SELECT COUNT(*) FROM Login WHERE id_user = ? AND role = 'siswa'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                JOptionPane.showMessageDialog(this, "ID User tidak ditemukan atau bukan siswa!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            // Ambil id_user asli dari record yang sedang diedit
            if (nisToExclude != null && !nisToExclude.isEmpty()) {
                sql = "SELECT id_user FROM Siswa WHERE nis = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, nisToExclude);
                rs = ps.executeQuery();
                if (rs.next()) {
                    int idUserAsli = rs.getInt("id_user");
                    if (id == idUserAsli) {
                        // Jika id_user baru sama dengan id_user asli, izinkan
                        return true;
                    }
                }
            }
            
              // Cek apakah id_user sudah digunakan di tabel Siswa (kecuali untuk NIS yang sedang diedit)
            sql = "SELECT COUNT(*) FROM Siswa WHERE id_user = ? AND nis != ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, nisToExclude != null ? nisToExclude : "");
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "ID User sudah digunakan oleh siswa lain!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
             return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID User harus berupa angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error validating id_user: " + e.getMessage());
            return false;
        }
    }
   
    private void tampilkanDataKeTabel() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("NIS");
    model.addColumn("Nama Siswa");
    model.addColumn("Jenis Kelamin");
    model.addColumn("Kelas");
    model.addColumn("id User");

    try {
        String sql = "SELECT * FROM Siswa";
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("nis"),
                rs.getString("nama_siswa"),
                rs.getString("jenis_kelamin"),
                rs.getString("kelas"),
                rs.getDouble("id_user"),
                
            });
        }
        tblSiswa.setModel(model); // <- ganti dengan nama JTable kamu
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
    }


// buat scroll pane agar tabel bisa di-scroll
javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(tblSiswa);
}

  // Tambahkan method untuk load data dari database ke tabel
private void loadData(){
       model.setRowCount(0); // kosongkan data dulu
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT * FROM siswa";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String id_user = rs.getString("id_user");
            String nis = rs.getString("nis");
            String nama = rs.getString("nama_siswa");
            String jk = rs.getString("jenis_kelamin");
            String kelas = rs.getString("kelas");
            

            model.addRow(new Object[]{id_user, nis, nama, jk, kelas});
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // </editor-fold>
@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNIS = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        txtKelas = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        cbJenisKelamin = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSiswa = new javax.swing.JTable();
        btnKembali = new javax.swing.JButton();
        idUser = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("NIS");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 69, -1, -1));

        jLabel2.setText("Nama");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 108, -1, -1));

        jLabel3.setText("Jenis Kelamin");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 151, -1, -1));

        jLabel4.setText("Kelas");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 200, -1, -1));
        getContentPane().add(txtNIS, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 66, 199, -1));
        getContentPane().add(txtNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 108, 201, -1));
        getContentPane().add(txtKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 197, 201, -1));

        btnTambah.setBackground(new java.awt.Color(0, 204, 255));
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });
        getContentPane().add(btnTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 250, -1, -1));

        btnEdit.setBackground(new java.awt.Color(0, 204, 255));
        btnEdit.setText("Update");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 250, -1, -1));

        btnHapus.setBackground(new java.awt.Color(0, 204, 255));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        getContentPane().add(btnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(593, 248, -1, -1));

        cbJenisKelamin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Jenis Kelamin", "Laki-Laki", "Perempuan" }));
        cbJenisKelamin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbJenisKelaminActionPerformed(evt);
            }
        });
        getContentPane().add(cbJenisKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 148, -1, -1));

        tblSiswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblSiswa);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 30, 330, 180));

        btnKembali.setBackground(new java.awt.Color(204, 204, 204));
        btnKembali.setText("Kembali");
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });
        getContentPane().add(btnKembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, -1, -1));

        idUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idUserActionPerformed(evt);
            }
        });
        getContentPane().add(idUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 32, 199, -1));

        jLabel5.setText("Id User");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 35, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/guru/background.png"))); // NOI18N
        jLabel6.setText("jLabel6");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 400));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
      String id_user = idUser.getText().trim();
        String nis = txtNIS.getText().trim();
        String nama = txtNama.getText().trim();
        String kelas = txtKelas.getText().trim();
        String jenisKelamin = cbJenisKelamin.getSelectedItem().toString();

        if (id_user.isEmpty() || nis.isEmpty() || nama.isEmpty() || jenisKelamin.equals("Pilih Jenis Kelamin") || kelas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua field harus diisi dengan benar!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (jenisKelamin.equals("Laki-Laki")) {
            jenisKelamin = "L";
        } else if (jenisKelamin.equals("Perempuan")) {
            jenisKelamin = "P";
        } else {
            jenisKelamin = "";
        }
         // Validasi id_user
        if (!validateIdUserSiswa(id_user, null)) {
            return;
        }

            try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Siswa (nis, nama_siswa, jenis_kelamin, kelas, id_user) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nis);
            ps.setString(2, nama);
            ps.setString(3, jenisKelamin);
            ps.setString(4, kelas);
            ps.setInt(5, Integer.parseInt(id_user));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
            resetForm();
            loadData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal menambah data: " + e.getMessage());
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    String id_user = idUser.getText().trim();
        String nis = txtNIS.getText().trim();
        String nama = txtNama.getText().trim();
        String jenisKelamin = cbJenisKelamin.getSelectedItem().toString();
        String kelas = txtKelas.getText().trim();

        if (id_user.isEmpty() || nis.isEmpty() || nama.isEmpty() || kelas.isEmpty() || jenisKelamin.equals("Pilih Jenis Kelamin")) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi dengan benar!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (jenisKelamin.equals("Laki-Laki")) {
            jenisKelamin = "L";
        } else if (jenisKelamin.equals("Perempuan")) {
            jenisKelamin = "P";
        } else {
            jenisKelamin = "";
        }
 // Validasi id_user (kecuali untuk NIS lama yang sedang diedit)
        if (!validateIdUserSiswa(id_user, nisLama)) {
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE Siswa SET nis = ?, nama_siswa = ?, jenis_kelamin = ?, kelas = ?, id_user = ? WHERE nis = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nis); // NIS baru
            stmt.setString(2, nama);
            stmt.setString(3, jenisKelamin);
            stmt.setString(4, kelas);
            stmt.setInt(5, Integer.parseInt(id_user));
            stmt.setString(6, nisLama); // NIS lama untuk menemukan record

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data siswa berhasil diperbarui!");
                resetForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data yang diperbarui!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
    } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
      String nis = txtNIS.getText().trim();

        if (nis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM siswa WHERE nis = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nis);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data siswa berhasil dihapus!");
                    resetForm();
                    loadData();
            } else {
                    JOptionPane.showMessageDialog(this, "Data tidak ditemukan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_btnHapusActionPerformed

    private void cbJenisKelaminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbJenisKelaminActionPerformed

    }//GEN-LAST:event_cbJenisKelaminActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        this.dispose();
        new FormData(idUserLogin).setVisible(true);
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void idUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idUserActionPerformed
       
    }//GEN-LAST:event_idUserActionPerformed

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<String> cbJenisKelamin;
    private javax.swing.JTextField idUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblSiswa;
    private javax.swing.JTextField txtKelas;
    private javax.swing.JTextField txtNIS;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables



    private void clearForm() {
        idUser.setText("");
        txtNIS.setText("");
        txtNama.setText("");
        cbJenisKelamin.setSelectedIndex(0);
        txtKelas.setText("");
        Object nisLama = null; // Reset nisLama
    }

    private void resetForm() {
        idUser.setText("");
        txtNIS.setText("");
        txtNama.setText("");
        cbJenisKelamin.setSelectedIndex(0);
        txtKelas.setText("");
        nisLama = null; // Reset nisLama
    }

  
}

