/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package politica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author TONY
 */
public class DatabaseHelper {
    
    private String connectionUrl;
    private Connection connection;
    private Vector<String> userBasicProfile;
    
    public DatabaseHelper() throws SQLException{
//        connectionUrl = "jdbc:mysql://localhost:3306/politica?zeroDateTimeBehavior=convertToNull";
//        connection = DriverManager.getConnection(connectionUrl,"tony","mogoa");
        connectionUrl = "jdbc:mysql://remotemysql.com:3306/KA3oDT2n70?zeroDateTimeBehavior=convertToNull&serverTimezone=UTC&verifyServerCertificate=false&useSSL=true";
        connection = DriverManager.getConnection(connectionUrl,"KA3oDT2n70","DWTJokFJCq");
    }
    
    public void registerUser(String firstName, String lastName, String email, String password) throws SQLException{
        String sqlReqister = "INSERT INTO user(user_first_name, user_last_name, user_email, user_password) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmtRegister = connection.prepareStatement(sqlReqister);
        pstmtRegister.setString(1, firstName);
        pstmtRegister.setString(2, lastName);
        pstmtRegister.setString(3, email);
        pstmtRegister.setString(4, password);
        pstmtRegister.executeUpdate();
    }
    
    public ObservableList<Role> getRoles(){
        ObservableList<Role> roles = FXCollections.observableArrayList();
        try {
            String sqlRoles = "SELECT * FROM role";
            PreparedStatement psRoles = connection.prepareStatement(sqlRoles);
            ResultSet rsRoles = psRoles.executeQuery();
            while(rsRoles.next()){
                Role role = new Role(rsRoles.getInt("role_id"), rsRoles.getString("role"));
                roles.add(role);
            }
            return roles;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return roles;
        }
    }
    
    public Map<String, String> getRolesAsMap(){
        Map<String, String> roles = new HashMap<String, String>();
        try {
            String sqlRoles = "SELECT * FROM role";
            PreparedStatement psRoles = connection.prepareStatement(sqlRoles);
            ResultSet rsRoles = psRoles.executeQuery();
            while(rsRoles.next()){
                roles.put(Integer.toString(rsRoles.getInt("role_id")), rsRoles.getString("role"));
            }
            return roles;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return roles;
        }
    }
    
    public boolean deletePromDeed(int deedId){
        try {
            String sqlDelProm = "DELETE FROM promise WHERE promise_id = (SELECT promise_id FROM deed WHERE deed_id = ?)";
            String sqlDelDeed = "DELETE FROM deed WHERE deed_id = ?";
            PreparedStatement psDelProm = connection.prepareStatement(sqlDelProm);
            psDelProm.setInt(1, deedId);
            PreparedStatement psDelDeed = connection.prepareStatement(sqlDelDeed);
            psDelDeed.setInt(1, deedId);
            return psDelProm.executeUpdate() > 0 && psDelDeed.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean deleteTermAndData(int termId){
        try {
            String sqlDelProm = "DELETE FROM promise WHERE term_id = ?";
            String sqlDelDeed = "DELETE FROM deed WHERE term_id = ?";
            String sqlDelTerm = "DELETE FROM term WHERE term_id = ?";
            PreparedStatement psDelProm = connection.prepareStatement(sqlDelProm);
            psDelProm.setInt(1, termId);
            PreparedStatement psDelDeed = connection.prepareStatement(sqlDelDeed);
            psDelDeed.setInt(1, termId);
            PreparedStatement psDelTerm = connection.prepareStatement(sqlDelTerm);
            psDelTerm.setInt(1, termId);
            psDelProm.executeUpdate();
            psDelDeed.executeUpdate();
            return psDelTerm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean deletePolitician(int id){
        try {
            String sqlGetTerms = "SELECT term_id FROM term WHERE leader_id = ?";
            PreparedStatement psGetTerms = connection.prepareStatement(sqlGetTerms);
            psGetTerms.setInt(1, id);
            ResultSet rs = psGetTerms.executeQuery();
            while(rs.next()){
                deleteTermAndData(rs.getInt("term_id"));
            }
            String sqlDelPolitician = "DELETE FROM leader WHERE leader_id = ?";
            PreparedStatement psDelPolitician = connection.prepareStatement(sqlDelPolitician);
            psDelPolitician.setInt(1, id);
            return psDelPolitician.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean addPolitician(String leader_first_name, String leader_last_name, String leader_birth_date, int roleId, File file){
        try {
            String sqlAddLeader = "INSERT INTO leader (leader_first_name, leader_last_name, leader_birth_date, leader_img, leader_img_ext) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmtAddLeader = connection.prepareStatement(sqlAddLeader, Statement.RETURN_GENERATED_KEYS);
            InputStream inputImage = new FileInputStream(file);
            pstmtAddLeader.setString(1, leader_first_name);
            pstmtAddLeader.setString(2, leader_last_name);
            pstmtAddLeader.setString(3, leader_birth_date);
            String extension = "";
            int i = file.getName().lastIndexOf('.');
            if (i > 0) {
                extension = file.getName().substring(i+1);
            }
            pstmtAddLeader.setBinaryStream(4, inputImage, (int)(file.length()));
            pstmtAddLeader.setString(5, extension);
            pstmtAddLeader.executeUpdate();
            ResultSet rsId = pstmtAddLeader.getGeneratedKeys();
            //Add a vie
            String sqlAddVie = "INSERT INTO vie (leader_id, role_id) VALUES (?, ?)";
            PreparedStatement psAddVie = connection.prepareStatement(sqlAddVie);
            if(rsId.next()){
                psAddVie.setInt(1, rsId.getInt(1));
                psAddVie.setInt(2, roleId);
            }
            psAddVie.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
    
    public ObservableList<Politician> getPoliticians(){
        ObservableList<Politician> items = FXCollections.observableArrayList();
        try {
            String sqlGetPoliticians = "SELECT leader_first_name, leader_last_name, leader_id FROM leader";
            PreparedStatement pstmtGetPoliticians = connection.prepareStatement(sqlGetPoliticians);
            ResultSet rsPoliticians = pstmtGetPoliticians.executeQuery();
            while(rsPoliticians.next()){
                Politician politician = new Politician(rsPoliticians.getInt("leader_id"), rsPoliticians.getString("leader_first_name"), rsPoliticians.getString("leader_last_name"));
                items.add(politician);
            }
            return items;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            return items;
        }
    }
    
    public ResultSet getPoliticians(int role_id){
        ResultSet rsPoliticians = null;
        try {
            String sqlGetPoliticians = "SELECT leader.leader_id, vie.role_id FROM leader JOIN vie ON leader.leader_id = vie.leader_id JOIN role ON vie.role_id = role.role_id WHERE vie.vie_valid = '1' AND role.role_id = ?";
            PreparedStatement pstmtGetPoliticians = connection.prepareStatement(sqlGetPoliticians);
            pstmtGetPoliticians.setInt(1, role_id);
            rsPoliticians = pstmtGetPoliticians.executeQuery();
            return rsPoliticians;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            return rsPoliticians;
        }
    }
    
    public ResultSet getPoliticianData(int id){
        ResultSet rsPolData = null;
        try {
            String sqlGetPoliticianData = "SELECT leader.leader_first_name, leader.leader_last_name, leader.leader_img, leader.leader_img_ext, vie.role_id FROM leader NATURAL JOIN vie WHERE vie_valid = '1' AND leader.leader_id = ?";
            PreparedStatement pstmtPoliticianData = connection.prepareStatement(sqlGetPoliticianData);
            pstmtPoliticianData.setInt(1, id);
            rsPolData = pstmtPoliticianData.executeQuery();
            return rsPolData;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return rsPolData;
        }
    }
    
     public ResultSet getPoliticianSearches(String leader_name){
        ResultSet rsPolData = null;
        String[] searchArgs = leader_name.split(" ");
        try {
            String sqlGetPoliticianData = "SELECT leader.*, vie.role_id FROM leader NATURAL JOIN vie WHERE vie_valid = '1' AND (";
            for(int i = 0; i < searchArgs.length ; i++){
                if(searchArgs.length == 1)
                    sqlGetPoliticianData += "leader.leader_first_name LIKE '%" + searchArgs[i] + "%' OR leader.leader_last_name LIKE '%" + searchArgs[i] + "%'";
                else if(i == 0 && searchArgs.length > 1)
                    sqlGetPoliticianData += "leader.leader_first_name LIKE '%" + searchArgs[i] + "%' OR leader.leader_last_name LIKE '%" + searchArgs[i] + "%'";
                else
                    sqlGetPoliticianData += "OR leader.leader_first_name LIKE '%" + searchArgs[i] + "%' OR leader.leader_last_name LIKE '%" + searchArgs[i] + "%'";
            }
            sqlGetPoliticianData += ")";
            PreparedStatement pstmtPoliticianData = connection.prepareStatement(sqlGetPoliticianData);
            //pstmtPoliticianData.setInt(1, id);
            rsPolData = pstmtPoliticianData.executeQuery();
            return rsPolData;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return rsPolData;
        }
    }
    
    public Map<Vector<String>, ResultSet> getPolPromDeed(int id){
        Map<Vector<String>, ResultSet> data = new HashMap<Vector<String>, ResultSet>();
        try {
            
            String sqlTerms = "SELECT term_start, term_end, term_id, role_held FROM term WHERE term.leader_id = ?";
            PreparedStatement psTerm = connection.prepareStatement(sqlTerms);
            psTerm.setInt(1, id);
            ResultSet rsTerms = psTerm.executeQuery();
            while(rsTerms.next()){
                String sqlPromDeed = "SELECT deed.deed_verbose, deed.deed_id, deed.deed_fulfills_promise, promise.promise_id, promise.promise_verbose FROM deed NATURAL JOIN promise WHERE deed.term_id = ?";
                PreparedStatement psPromDeed = connection.prepareStatement(sqlPromDeed);
                psPromDeed.setInt(1, rsTerms.getInt("term_id"));
                ResultSet rsPromDeed = psPromDeed.executeQuery();
                Vector<String> termData = new Vector<String>();
                termData.add(Integer.toString(rsTerms.getInt("term_id")));
                termData.add(rsTerms.getString("role_held"));
                termData.add(rsTerms.getString("term_start"));
                termData.add(rsTerms.getString("term_end"));
                data.put(termData, rsPromDeed);
            }
            return data;
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return data;
        }
    }
    
    public int newTerm(int leader_id, String role_held, String term_start, String term_end){
        try {
            String sqlAddTerm = "INSERT INTO term (leader_id, role_held, term_start, term_end) VALUES (?, ?, ?, ?)";
            PreparedStatement psAddTerm = connection.prepareStatement(sqlAddTerm, Statement.RETURN_GENERATED_KEYS);
            psAddTerm.setInt(1, leader_id);
            psAddTerm.setString(2, role_held);
            psAddTerm.setString(3, term_start);
            psAddTerm.setString(4, term_end);
            psAddTerm.executeUpdate();
            ResultSet rs = psAddTerm.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    public int newPromDeed(int leader_id, String promise, int term_id, String deed, boolean fulfill) throws SQLException{
        String sqlAddDeed = "INSERT INTO deed (leader_id, promise_id, deed_fulfills_promise, deed_verbose, term_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement psAddDeed = null;
        Vector<Integer> ids = new Vector();
        try {
            psAddDeed = connection.prepareStatement(sqlAddDeed, Statement.RETURN_GENERATED_KEYS);
            psAddDeed.setInt(1, leader_id);
            psAddDeed.setString(3, fulfill == true ? "1" : "0");
            psAddDeed.setString(4, deed);
            psAddDeed.setInt(5, term_id);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            String sqlAddProm = "INSERT INTO promise (leader_id, promise_verbose, term_id) VALUES (?, ?, ?)";
            PreparedStatement psAddProm = connection.prepareStatement(sqlAddProm, Statement.RETURN_GENERATED_KEYS);
            psAddProm.setInt(1, leader_id);
            psAddProm.setString(2, promise);
            psAddProm.setInt(3, term_id);
            psAddProm.executeUpdate();
            ResultSet rs = psAddProm.getGeneratedKeys();
            rs.next();
            psAddDeed.setInt(2, rs.getInt(1));
            ids.add(rs.getInt(1));
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        psAddDeed.executeUpdate();
        ResultSet rsDeedId = psAddDeed.getGeneratedKeys();
        rsDeedId.next();
        return rsDeedId.getInt(1);
    }
    
    public Vector<String> retrieveUser(String email, String password) throws SQLException{
        userBasicProfile = new Vector<>();
        String sqlRetrieveUser = "SELECT user_id, user_first_name, user_last_name FROM user WHERE user_email = ? AND user_password = ?";
        PreparedStatement pstmtRetrieveUser = connection.prepareStatement(sqlRetrieveUser);
        pstmtRetrieveUser.setString(1, email);
        pstmtRetrieveUser.setString(2, password);
        ResultSet rsRetrievedUser = pstmtRetrieveUser.executeQuery();
        if(rsRetrievedUser.next()){
            userBasicProfile.add(Integer.toString(rsRetrievedUser.getInt("user_id")));
            userBasicProfile.add(rsRetrievedUser.getString("user_first_name"));
            userBasicProfile.add(rsRetrievedUser.getString("user_last_name"));
        }
        return userBasicProfile;
    }
    
    public void closeDbConn(){
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
