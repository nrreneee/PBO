/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abstracts;


/**
 *
 * @author LENOVO
 */
public class Guru extends User {

    public Guru(String id, String nama, String username, String password) {
        super(id, nama, username, password);
    }

    @Override
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    public String getRole() {
        return "Guru";
    }
    
}
