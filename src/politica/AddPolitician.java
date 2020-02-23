/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package politica;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 *
 * @author TONY
 */
public class AddPolitician extends VBox{
    
    private Label lblFirstName;
    private TextField txtFirstname;
    private Label lblLastName;
    private TextField txtLastName;
    private DatePicker datePicker;
    private FileChooser fileChooser;
    private File fileImg;
    private File storeFileName;
    private Label lblChooseDate;
    private Button btnChooseImg;
    private Label lblAdd;
    private HBox hboxForX;
    private Button btnExit;
    private Button btnSave;
    private Rectangle recImage;
    private StackPane stackImg;
    private ImageView img;
    private ImageView imgLogo;
    private ImageView imgUser;
    private Image imgLeader;
    private HBox hBoxForLogo;
    private ChoiceBox<Role> cbViePos;
    private Label lblVie;
    private DatabaseHelper dbData;
    private int roleId;
    private ImageView imgClose;
    private Image imguser;
    
    public AddPolitician(Stage stage, DatabaseHelper db){
        
        this.dbData = db;
        
        imgLogo = new ImageView(new Image(this.getClass().getResource("/Politica-logos_transparent.png").toString()));
        imgLogo.setFitWidth(300);
        imgLogo.setFitHeight(77);
        imgLogo.setPreserveRatio(true);
        
//        recImage = new Rectangle(120, 120);
//        recImage.setStroke(Color.GREY);
//        recImage.setStrokeWidth(0.1);
//        recImage.setFill(Color.WHITE);
//        recImage.setArcHeight(5);
//        recImage.setArcWidth(5);
        imguser = new Image(this.getClass().getResource("/teacher-clip-filipino-3.png").toString());
        imgUser = new ImageView(imguser);
        imgUser.setFitWidth(150);
        imgUser.setPreserveRatio(true);
        
        stackImg = new StackPane(imgUser);
        stackImg.getStyleClass().add("imageWindow");
        
        btnSave = new Button("Save");
        btnSave.setMaxWidth(200);
        btnSave.setMinWidth(200);
        btnSave.setPrefWidth(200);
        btnSave.getStyleClass().add("button-large");
        btnSave.setOnAction(e -> saveData());
        
        imgClose = new ImageView(new Image(this.getClass().getResource("/Material Icons_e042(0)_16.png").toString()));
        
        btnExit = new Button();
        btnExit.setGraphic(imgClose);
        
        hBoxForLogo = new HBox(imgLogo);
        hBoxForLogo.setAlignment(Pos.CENTER);
        
        hboxForX = new HBox(btnExit);
        hboxForX.setAlignment(Pos.TOP_RIGHT);
        //HBox.setMargin(btnExit, new Insets(10));
        
//        hboxForX.setMaxHeight(10);
//        hboxForX.setMinHeight(10);
//        hboxForX.setPrefHeight(10);
        
        lblAdd = new Label("Add new politician");
        lblAdd.getStyleClass().add("fontSize17");
        //lblAdd.setTextFill(Color.WHITE);
        
        lblVie = new Label("Position vying");
        lblVie.getStyleClass().add("fontSize13");
        
        lblFirstName = new Label("First name");
        lblFirstName.getStyleClass().add("fontSize13");
        //lblFirstName.setTextFill(Color.WHITE);
        
        lblLastName = new Label("Last name");
        lblLastName.getStyleClass().add("fontSize13");
        //lblLastName.setTextFill(Color.WHITE);
        
        lblChooseDate = new Label("Date of birth");
        lblChooseDate.getStyleClass().add("fontSize13");
        //lblChooseDate.setTextFill(Color.WHITE);
        
        btnChooseImg =  new Button("Attach photo");
        btnChooseImg.setOnAction(e -> {
            //System.out.println(System.getProperty("user.home"));
            fileImg = fileChooser.showOpenDialog(stage);
            
            try{
                showPreview(fileImg);
            }
            catch(NullPointerException ex){
                
            }
        });
        
        txtFirstname = new TextField();
        txtFirstname.setPromptText("e.g Paul");
        txtFirstname.getStyleClass().add("txtBox");
        txtFirstname.setMaxWidth(200);
        txtFirstname.setMinWidth(200);
        txtFirstname.setPrefWidth(200);
        
        txtLastName = new TextField();
        txtLastName.setPromptText("e.g Mikiki");
        txtLastName.getStyleClass().add("txtBox");
        txtLastName.setMinWidth(200);
        txtLastName.setMaxWidth(200);
        txtLastName.setPrefWidth(200);
        
        datePicker = new DatePicker();
        datePicker.setMinWidth(200);
        datePicker.setMaxWidth(200);
        datePicker.setPrefWidth(200);
        
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "\\Downloads"));
        
        cbViePos = new ChoiceBox();
        cbViePos.setMinWidth(200);
        cbViePos.setMaxWidth(200);
        cbViePos.setPrefWidth(200);
        
        ObservableList<Role> items = FXCollections.observableArrayList(dbData.getRoles());
        
        cbViePos.setItems(items);
        cbViePos.setConverter(new StringConverter<Role>() {

            @Override
            public String toString(Role object) {
                if(object == null){
                    return null;
                }
                else{
                    return object.toString();
                }
            }

            @Override
            public Role fromString(String string) {
                return cbViePos.getItems().stream().filter(ap -> 
                    ap.toString().equals(string)).findFirst().orElse(null);
            }
        });
        cbViePos.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null){ 
                //System.out.println(newval.getId());
                roleId = newval.getId();
            }    
        });
        
        AddPolitician.this.setSpacing(10);
        AddPolitician.this.getChildren().addAll(hboxForX, hBoxForLogo, lblAdd, lblFirstName, txtFirstname, lblLastName, txtLastName, lblChooseDate, datePicker, lblVie, cbViePos, stackImg, btnChooseImg, btnSave);
        AddPolitician.this.setAlignment(Pos.CENTER);
        AddPolitician.this.getStyleClass().add("paneAddColor");
        AddPolitician.this.setPadding(new Insets(20));
    }
    
    public void showPreview(File source){
        imgLeader = new Image("file:" + source.toPath());
        img = new ImageView(imgLeader);
        Double aspectRatio = imgLeader.getWidth()/imgLeader.getHeight();
        img.setFitWidth(179);
        img.setPreserveRatio(true);
        stackImg.getChildren().clear();
        stackImg.getChildren().add(img);
        stackImg.setMaxHeight(179/aspectRatio + 10);
        stackImg.setMinHeight(179/aspectRatio + 10);
        stackImg.setPrefHeight(179/aspectRatio + 10);
        stackImg.setMaxWidth(179 + 10);
        stackImg.setMinWidth(179 + 10);
        stackImg.setPrefWidth(179 + 10);
        btnChooseImg.setText("Not this one");
    }
    
    public Button getBtnExit(){
        return btnExit;
    }
    
    public TextField getTxtFirstName(){
        return txtFirstname;
    }
    
    public TextField getTxtLastName(){
        return txtLastName;
    }
    
    public DatePicker getDatePicker(){
        return datePicker;
    }
    
    public void saveData(){
        if(txtFirstname.getText().length() > 1 && txtLastName.getText().length() > 1 && datePicker.getValue().toString().length() > 1 && roleId > 0 && fileImg != null){
            if(dbData.addPolitician(txtFirstname.getText(), txtLastName.getText(), datePicker.getValue().toString(), roleId, fileImg)){
                txtFirstname.clear();
                txtLastName.clear();
                datePicker.getEditor().clear();
                Double aspectRatio = imguser.getWidth()/imguser.getHeight();
                stackImg.getChildren().clear();
                stackImg.getChildren().add(imgUser);
                stackImg.setMaxHeight(179/aspectRatio + 10);
                stackImg.setMinHeight(179/aspectRatio + 10);
                stackImg.setPrefHeight(179/aspectRatio + 10);
                stackImg.setMaxWidth(179 + 10);
                stackImg.setMinWidth(179 + 10);
                stackImg.setPrefWidth(179 + 10);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Record successfully added.");
                alert.show();
            }
        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Please fill out all fields!");
            alert.show();
        }
        
    }
}
