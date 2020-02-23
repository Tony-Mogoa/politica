/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package politica;

/**
 *
 * @author TONY
 */
public class Role {

    
    private int id;
    private String role;
    
    public Role(int id, String role){
        this.id = id;
        this.role = role;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    
    public int getId(){
        return id;
    }
    
    public String getRole(){
        return role;
    }
    
    @Override
    public String toString(){
        return getRole();
    }
}
