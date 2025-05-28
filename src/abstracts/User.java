/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abstracts;

/**
 *
 * @author LENOVO
 */
public abstract class User {
    protected String id;
    protected String nama;
    protected String username;
    protected String password;

    public User(String id, String nama, String username, String password) {
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Abstract method untuk login, implementasinya berbeda antara Guru dan Siswa
    public abstract boolean login(String username, String password);

    // Abstract method untuk menampilkan peran user
    public abstract String getRole();
}

