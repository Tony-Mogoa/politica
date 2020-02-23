/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package politica;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 *
 * @author TONY
 */
public class RegisterGrid extends GridPane{
    
    
    private ColumnConstraints col1;
    private ColumnConstraints col2;
    private Image imgLogo;
    private ImageView iViewLogo;
    private Button btnExit;
    private TextField txtFirstName;
    private TextField txtLastName;
    private TextField txtEmail;
    private PasswordField txtPassword;
    private PasswordField txtConfirmPassword;
    private Button btnRegister;
    private Label lblFirstName;
    private Label lblLastName;
    private Label lblEmail;
    private Label lblPassword;
    private Label lblConfirmPassword;
    private Label lblRegister;
    public Button btnBackToLogin;
    private User user;
    private Alert alertInRegister;
    private ImageView imgClose;
    private DatabaseHelper dbData;
    
    public RegisterGrid(DatabaseHelper db) {
        this.dbData = dbData;
        user = new User(db);
        
        lblRegister = new Label("Signing you up!");
        lblRegister.getStyleClass().add("XFontSize");
        
        lblFirstName = new Label("First name");
        lblFirstName.getStyleClass().add("fontSize13");
        
        lblLastName = new Label("Last name");
        lblLastName.getStyleClass().add("fontSize13");
        
        lblEmail = new Label("Email address");
        lblEmail.getStyleClass().add("fontSize13");
        
        lblPassword = new Label("Enter password");
        lblPassword.getStyleClass().add("fontSize13");
        
        lblConfirmPassword = new Label("Confirm password");
        lblConfirmPassword.getStyleClass().add("fontSize13");
        
        txtFirstName = new TextField();
        txtFirstName.setPrefWidth(200);
        txtFirstName.setMaxWidth(200);
        txtFirstName.setPromptText("e.g. Shem");
        txtFirstName.getStyleClass().add("txtBox");
        
        txtLastName = new TextField();
        txtLastName.setPrefWidth(200);
        txtLastName.setMaxWidth(200);
        txtLastName.setPromptText("e.g Mwanza");
        txtLastName.getStyleClass().add("txtBox");
        
        txtEmail = new TextField();
        txtEmail.setPrefWidth(200);
        txtEmail.setMaxWidth(200);
        txtEmail.setPromptText("shem.mwanza@example.com");
        txtEmail.getStyleClass().add("txtBox");
        
        txtPassword = new PasswordField();
        txtPassword.setPrefWidth(200);
        txtPassword.setMaxWidth(200);
        txtPassword.setPromptText("Password");
        txtPassword.getStyleClass().add("txtBox");
        
        txtConfirmPassword = new PasswordField();
        txtConfirmPassword.setPrefWidth(200);
        txtConfirmPassword.setMaxWidth(200);
        txtConfirmPassword.setPromptText("Just re-enter the password..");
        txtConfirmPassword.getStyleClass().add("txtBox");
        
        imgClose = new ImageView(new Image(this.getClass().getResource("/Material Icons_e042(0)_16.png").toString()));
        
        btnExit = new Button();
        btnExit.setGraphic(imgClose);
        btnExit.setOnAction(e -> {
            Platform.exit();
        });
        
        btnRegister = new Button("Create Account");
        btnRegister.setPrefWidth(200);
        btnRegister.getStyleClass().add("button-large");
        btnRegister.setOnAction(e -> registerUser());
        
        btnBackToLogin = new Button("Login Instead");
        btnBackToLogin.getStyleClass().add("button-large");
                
        imgLogo = new Image(this.getClass().getResource("/Politica-large-logos_transparent.png").toString());
        
        iViewLogo = new ImageView(imgLogo);
        iViewLogo.setFitWidth(300);
        iViewLogo.setPreserveRatio(true);
        
        col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        
        RegisterGrid.this.setPadding(new Insets(10));
        RegisterGrid.this.setHgap(10);
        RegisterGrid.this.setVgap(10);
        RegisterGrid.this.getColumnConstraints().addAll(col1, col2);
        RegisterGrid.this.add(btnExit, 1, 0);
        GridPane.setHalignment(btnExit, HPos.RIGHT);
        RegisterGrid.this.addRow(1, iViewLogo);
        GridPane.setColumnSpan(iViewLogo, 2);
        GridPane.setHalignment(iViewLogo, HPos.CENTER);
        RegisterGrid.this.addRow(2, lblRegister);
        GridPane.setColumnSpan(lblRegister, 2);
        GridPane.setHalignment(lblRegister, HPos.CENTER);
        GridPane.setHalignment(iViewLogo, HPos.CENTER);
        RegisterGrid.this.addRow(3, lblFirstName, txtFirstName);
        GridPane.setHalignment(lblFirstName, HPos.RIGHT);
        GridPane.setHalignment(txtFirstName, HPos.LEFT);
        RegisterGrid.this.addRow(4, lblLastName, txtLastName);
        GridPane.setHalignment(lblLastName, HPos.RIGHT);
        GridPane.setHalignment(txtLastName, HPos.LEFT);
        RegisterGrid.this.addRow(5, lblEmail, txtEmail);
        GridPane.setHalignment(lblEmail, HPos.RIGHT);
        GridPane.setHalignment(txtEmail, HPos.LEFT);
        RegisterGrid.this.addRow(6, lblPassword, txtPassword);
        GridPane.setHalignment(lblPassword, HPos.RIGHT);
        GridPane.setHalignment(txtPassword, HPos.LEFT);
        RegisterGrid.this.addRow(7, lblConfirmPassword, txtConfirmPassword);
        GridPane.setHalignment(lblConfirmPassword, HPos.RIGHT);
        GridPane.setHalignment(txtConfirmPassword, HPos.LEFT);
        RegisterGrid.this.add(btnRegister, 1, 8);
        GridPane.setHalignment(btnRegister, HPos.LEFT);
        RegisterGrid.this.add(btnBackToLogin, 1, 9);
        GridPane.setHalignment(btnBackToLogin, HPos.CENTER);
    }
    
    public Button getBtnBackToLogin(){
        return btnBackToLogin;
    }
    
    public void registerUser(){
        if(txtFirstName.getText().length() > 2 && txtLastName.getText().length() > 2 && txtEmail.getText().length() > 2 && txtPassword.getText().length() > 7){
            if(txtPassword.getText().equals(txtConfirmPassword.getText())){
                if(user.registerUser(txtFirstName.getText(), txtLastName.getText(), txtEmail.getText(), txtPassword.getText())){
                    alertInRegister = new Alert(AlertType.INFORMATION);
                    alertInRegister.setContentText("Successfully signed you up!");
                    alertInRegister.show();
                    btnBackToLogin.fire();
                }
            }
            else{
                alertInRegister = new Alert(AlertType.ERROR);
                alertInRegister.setContentText("Passwords you entered are not matching!");
                alertInRegister.show();
            }
        }
        else{
            alertInRegister = new Alert(AlertType.ERROR);
            alertInRegister.setContentText("Password length should be at least 8 characters, and length of all other text fields should be of at least 3 characters.");
            alertInRegister.show();
        }
    }
    
}
