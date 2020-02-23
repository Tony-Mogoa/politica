/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package politica;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author TONY
 */
public class Politica extends Application {
    
    private Stage stage;
    private Image img;
    private ImageView logo;
    private Label lblUsername;
    private TextField txtUsername;
    private GridPane grid;
    private ColumnConstraints col1;
    private ColumnConstraints col2;
    private Rectangle card;
    private StackPane pane;
    private Scene scene;
    private Button btnExit;
    private Button btnNext;
    private Button btnRegister;
    private LoginPasswordPane loginPasswordPane;
    private double dragOffsetX;
    private double dragOffsetY;
    private Scene registerScene;
    private User user;
    private Alert alertLogin;
    private ImageView loader;
    private Scene mainUIScene;
    private MainUI mainUIPane;
    private Stage stageMainUi;
    private ImageView imgClose;
    private DatabaseHelper dbData;
    
    @Override
    public void start(Stage primaryStage) {
        
        stage = primaryStage;
        createDirectory();
        img = new Image(this.getClass().getResource("/Politica-logos_transparent.png").toString());
        logo = new ImageView(img);
        logo.setFitWidth(300);
        logo.setFitHeight(77);
        logo.setPreserveRatio(true);
        
        lblUsername = new Label("Enter your username or email");
        lblUsername.getStyleClass().add("fontSize13");
        
        txtUsername = new TextField();
        txtUsername.setPromptText("shem.mwanza@example.com");
        txtUsername.setMinWidth(200);
        txtUsername.setMaxWidth(200);
        txtUsername.setPrefWidth(200);
        txtUsername.getStyleClass().add("txtBox");
        Platform.runLater(() -> txtUsername.requestFocus());
        txtUsername.setOnKeyPressed((KeyEvent keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                switchToPassword();
            }
        });
        
        
        grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1, col2);
        
        imgClose = new ImageView(new Image(this.getClass().getResource("/Material Icons_e042(0)_16.png").toString()));
        
        btnExit = new Button();
        //btnExit.getStyleClass().add("XFontSize");
        btnExit.setGraphic(imgClose);
        btnExit.setOnAction(e ->{
            Platform.exit();
        }
        );
        
        btnNext = new Button("next");
        btnNext.setOnAction(e -> {switchToPassword();});
        
        btnRegister = new Button("Register");
        btnRegister.setOnAction(e -> {switchToRegister();});
        
        grid.add(btnNext, 1, 5);
        grid.add(btnExit, 1, 0);
        grid.add(btnRegister, 0, 5);
        GridPane.setHalignment(btnRegister, HPos.CENTER);
        GridPane.setHalignment(btnNext, HPos.CENTER);
        GridPane.setHalignment(btnExit, HPos.RIGHT);
        grid.addRow(1, logo);
        GridPane.setColumnSpan(logo, 2);
        GridPane.setHalignment(logo, HPos.CENTER);
        grid.addRow(3, lblUsername);
        grid.addRow(4, txtUsername);
        GridPane.setHalignment(lblUsername, HPos.CENTER);
        GridPane.setColumnSpan(lblUsername, 2);
        GridPane.setHalignment(txtUsername, HPos.CENTER);
        GridPane.setColumnSpan(txtUsername, 2);
        
        card = new Rectangle(400, 300);
        card.setStroke(Color.GREY);
        card.setFill(Color.rgb(248, 249, 249, 1.0));
        card.setStrokeWidth(0.1);
        
        pane = new StackPane(card, grid);
        pane.setStyle("-fx-border-radius: 10");
        
        scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(this.getClass().getResource("/Material Icons_e862(0)_32.png").toString()));
        stage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void switchToPassword(){
        loginPasswordPane = new LoginPasswordPane(txtUsername.getText(), dbData);
        loginPasswordPane.getBtnBack().setOnAction(e -> switchToUsernameScene());
        loginPasswordPane.getBtnLogin().setOnAction(e -> logIn());
        loginPasswordPane.getTxtPassword().setOnKeyPressed((KeyEvent keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                logIn();
            }
        });

        Scene passwordScene = new Scene(loginPasswordPane);
        passwordScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        stage.setScene(passwordScene);
    }
    
    public void logIn(){
        ProgressBar pb = new ProgressBar();
        loginPasswordPane.getBtnLogin().setText("Authenticating..");
        loginPasswordPane.getBtnLogin().setDisable(true);
        loginPasswordPane.getHbProgress().getChildren().add(pb);
//        loader = new ImageView(new Image("file:C:\\Users\\TONY\\Documents\\NetBeansProjects\\Politica\\src\\politica\\loader.gif"));
//        loader.setFitHeight(30);
//        loader.setPreserveRatio(true);
//        loginPasswordPane.getBtnLogin().setGraphic(loader);
        Task task = new Task<Void>() {
            @Override public Void call() {
                try {
                    dbData = new DatabaseHelper();
                } catch (SQLException ex) {
                    Logger.getLogger(Politica.class.getName()).log(Level.SEVERE, null, ex);
                }
                user = new User(txtUsername.getText(), loginPasswordPane.getTxtPassword().getText(), dbData);
                return null;
            }
        };
        new Thread(task).start();
        pb.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(h -> {
            if(user.isLoggedIn()){
                mainUIPane = new MainUI(dbData);
                mainUIPane.getBtnLogout().setOnAction(e -> logout());
                //mainUIPane.getBtnAdd().setOnAction(e -> openAddDialog());
                mainUIScene = new Scene(mainUIPane);
                mainUIScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
                stageMainUi = new Stage();
                stageMainUi.setScene(mainUIScene);
                stageMainUi.initStyle(StageStyle.DECORATED);
                stageMainUi.setTitle("Politica");
                stageMainUi.setMaximized(true);
                stageMainUi.getIcons().add(new Image(this.getClass().getResource("/Material Icons_e862(0)_32.png").toString()));
                stageMainUi.setOnCloseRequest( event -> {dbData.closeDbConn();} );
                stageMainUi.show();
                txtUsername.clear();
                loginPasswordPane.getTxtPassword().clear();
                loginPasswordPane.getLblUsername().setText("");
                stage.hide();
            }
            else{
                loginPasswordPane.getBtnLogin().setText("Login");
                loginPasswordPane.getBtnLogin().setDisable(false);
                //loginPasswordPane.getBtnLogin().setGraphic(null);
                alertLogin = new Alert(Alert.AlertType.ERROR);
                alertLogin.setContentText("Invalid login credentials!");
                alertLogin.show();
            }
        });

    }
    
    public void logout(){
        scene.setRoot(pane);
        stage.setScene(scene);
        stage.setX(483.0);
        stage.setY(142.0);
        stage.show();
        stageMainUi.close();
    }
    
    public void switchToUsername(){
        scene.setRoot(pane);
    }
    
    public void switchToUsernameScene(){
        stage.setScene(scene);
        stage.setX(483.0);
        stage.setY(142.0);
    }
    public void switchToRegister(){
        RegisterGrid registerGrid = new RegisterGrid();
        registerGrid.btnBackToLogin.setOnAction(e -> switchToUsernameScene());
        registerScene = new Scene(registerGrid, 800, 600);
        registerScene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        registerScene.setOnMousePressed(e -> handleMousePressed(e));
        registerScene.setOnMouseDragged(e -> handleMouseDragged(e));
        registerScene.setFill(Color.rgb(248, 249, 249, 1.0));
        stage.setScene(registerScene);
        stage.setX(300);
        stage.setY(50);
        
    }
    
    protected void handleMousePressed(MouseEvent e){
        this.dragOffsetX = e.getSceneX() - stage.getX();
        this.dragOffsetY = e.getSceneX() - stage.getY();
    }
    
    protected void handleMouseDragged(MouseEvent e){
        stage.setX(e.getScreenX() - this.dragOffsetX);
        stage.setY(e.getScreenY() - this.dragOffsetY);
    }
    
    public void createDirectory(){
        new File(System.getenv("APPDATA") + "/politica").mkdirs();
    }
}
