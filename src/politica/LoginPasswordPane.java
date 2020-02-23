/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package politica;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author TONY
 */
public class LoginPasswordPane extends StackPane{
    
    private Image img;
    private ImageView logo;
    private Rectangle card;
    private GridPane passwordGrid;
    private Label lblPassword;
    private PasswordField txtPassword;
    private Button btnExit;
    private Button btnLogin;
    private ColumnConstraints col1;
    private ColumnConstraints col2;
    private Button btnBack;
    private Label lblUsername;
    private HBox hbProgress;
    private ImageView imgClose;
    
    public LoginPasswordPane(String email, DatabaseHelper db){
        
        lblUsername = new Label("username: " + email);
        lblUsername.getStyleClass().add("boldFont");
        
        lblPassword = new Label("Enter your password");
        lblPassword.getStyleClass().add("fontSize13");
        
        imgClose = new ImageView(new Image(this.getClass().getResource("/Material Icons_e042(0)_16.png").toString()));
        
        btnExit = new Button();
        btnExit.setGraphic(imgClose);
        btnExit.setOnAction(e -> {
            try {
                db.closeDbConn();
            } catch (NullPointerException ex) {
                //No db connection created
            }
            
            Platform.exit();
        });
    
        txtPassword = new PasswordField();
        txtPassword.setMinWidth(200);
        txtPassword.setMaxWidth(200);
        txtPassword.setPrefWidth(200);
        txtPassword.getStyleClass().add("txtBox");
        txtPassword.setPromptText("Password");
        Platform.runLater(() -> txtPassword.requestFocus());
    
        btnLogin = new Button("Login");
        btnLogin.setMinWidth(200);
        btnLogin.setMaxWidth(200);
        btnLogin.setPrefWidth(200);
        btnLogin.getStyleClass().add("button-large");
        
        btnBack = new Button("Back");
        //btnBack.setOnAction(e ->{switchToUsername();});
    
        img = new Image(this.getClass().getResource("/Politica-logos_transparent.png").toString());
        logo = new ImageView(img);
        logo.setFitWidth(300);
        logo.setFitHeight(77);
        logo.setPreserveRatio(true);
        
        passwordGrid = new GridPane();
        passwordGrid.setPadding(new Insets(10));
        passwordGrid.setHgap(10);
        passwordGrid.setVgap(10);
        
        col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        
        col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        
        hbProgress = new HBox();
        hbProgress.setAlignment(Pos.CENTER);
        
        passwordGrid.getColumnConstraints().addAll(col1, col2);
        passwordGrid.add(btnExit, 1, 0);
        GridPane.setHalignment(btnExit, HPos.RIGHT);
        passwordGrid.addRow(1, logo);
        GridPane.setColumnSpan(logo, 2);
        GridPane.setHalignment(logo, HPos.CENTER);
        passwordGrid.addRow(2, lblPassword);
        GridPane.setColumnSpan(lblPassword, 2);
        GridPane.setHalignment(lblPassword, HPos.CENTER);
        passwordGrid.addRow(3, lblUsername);
        GridPane.setColumnSpan(lblUsername, 2);
        GridPane.setHalignment(lblUsername, HPos.CENTER);
        passwordGrid.addRow(4, txtPassword);
        GridPane.setColumnSpan(txtPassword, 2);
        GridPane.setHalignment(txtPassword, HPos.CENTER);
        passwordGrid.addRow(5, btnLogin);
        GridPane.setColumnSpan(btnLogin, 2);
        GridPane.setHalignment(btnLogin, HPos.CENTER);
        passwordGrid.addRow(6, hbProgress);
        GridPane.setColumnSpan(hbProgress, 2);
        GridPane.setHalignment(hbProgress, HPos.CENTER);
        passwordGrid.add(btnBack, 1, 7);
        GridPane.setHalignment(btnBack, HPos.CENTER);
        
        card = new Rectangle(400, 330);
        card.setStroke(Color.GREY);
        card.setFill(Color.rgb(248, 249, 249, 1.0));
        card.setStrokeWidth(0.1);
        
        boolean addAll = LoginPasswordPane.this.getChildren().addAll(card, passwordGrid);
        
    }
    
    public Button getBtnBack(){
        return btnBack;
    }
    
    public Button getBtnLogin(){
        return btnLogin;
    }
    
    public PasswordField getTxtPassword(){
        return this.txtPassword;
    }
    
    public Label getLblUsername(){
        return lblUsername;
    }
    
    public HBox getHbProgress(){
        return hbProgress;
    }
}
