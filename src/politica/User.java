/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package politica;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TONY
 */
public class User {
    
    private int userId;
    private String userFirstName;
    private String userLastName;
    private DatabaseHelper dbData;
    private Vector<String> basicUserProfile;
    private boolean loggedIn;
    
    public User(DatabaseHelper dbData){
        this.dbData = dbData;
        userId = 0;
        userFirstName = "";
        userLastName = "";
        loggedIn = false;
    }
    
    public User (String email, String password, DatabaseHelper dbData){
        try {
            this.dbData = dbData;
            basicUserProfile = dbData.retrieveUser(email, Hashing.toHexString(Hashing.getSHA(password)));
        } catch (NoSuchAlgorithmException | SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!basicUserProfile.isEmpty()){
            userId = Integer.parseInt(basicUserProfile.get(0));
            userFirstName = basicUserProfile.get(1);
            userLastName = basicUserProfile.get(2);
            loggedIn = true;
        }
        else{
            loggedIn = false;
        }
    }
    
    public boolean registerUser(String firstName, String lastName, String email, String password){
        try {
            try {
                dbData.registerUser(firstName, lastName, email, Hashing.toHexString(Hashing.getSHA(password)));
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(RegisterGrid.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            
            return false;
        }
        
    }
    
    public boolean isLoggedIn(){
        return this.loggedIn;
    }
    
    @Override
    public String toString(){
        return userFirstName + " " + userLastName;
    }
}
