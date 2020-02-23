/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package politica;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 *
 * @author TONY
 */
public class EditUI extends GridPane{
    
    private ComboBox<Politician> comboLeaders;
    private ImageView imgUser;
    private TextField txtTermFrom;
    private TextField txtTermTo;
    private Button btnExit;
    private Button btnEdit;
    private Button btnAdd;
    private StackPane stackImg;
    private ColumnConstraints col1;
    private ColumnConstraints col2;
    private ColumnConstraints col3;
    private ColumnConstraints col4;
    private Label lblEdit; 
    private ImageView imgLogo;
    private Accordion acc;
    private final Label lblTermYears;
    private Alert alertError;
    private Button btnEditProfile;
    private DatabaseHelper dbData;
    private TextField txtTermRole;
    private int id;
    private AddPolitician addUI;
    private Map<Vector<String>, ResultSet> termsPromsDeeds;
    private VBox vbPromsDeedsFromDb;
    private Stage stage;
    private ImageView imgClose;
    
    public EditUI(Stage stage, DatabaseHelper db){
        this.stage = stage;
        
        this.dbData = db;
        
        imgLogo = new ImageView(new Image(this.getClass().getResource("/Politica-logos_transparent.png").toString()));
        imgLogo.setFitWidth(300);
        imgLogo.setFitHeight(77);
        imgLogo.setPreserveRatio(true);
        
        lblEdit = new Label("Edit politician data");
        lblEdit.getStyleClass().add("fontSize17");
        
        lblTermYears = new Label("Term years");
        lblTermYears.getStyleClass().add("fontSize13");
        
        imgUser = new ImageView(new Image(this.getClass().getResource("/teacher-clip-filipino-3.png").toString()));
        imgUser.setFitWidth(100);
        imgUser.setPreserveRatio(true);
        
        stackImg = new StackPane(imgUser);
        stackImg.getStyleClass().add("imageWindow");
        
        imgClose = new ImageView(new Image(this.getClass().getResource("/Material Icons_e042(0)_16.png").toString()));
        btnExit = new Button();
        btnExit.setGraphic(imgClose);
        
        btnEdit = new Button("Edit");
        
        btnAdd = new Button("Add");
        btnAdd.setOnAction(e -> addTerm());
        
        btnEditProfile = new Button("Delete Politician");
        btnEditProfile.setOnAction(t -> {
            if(dbData.deletePolitician(id)){
                this.stage.close();
            }
        });
        comboLeaders = new ComboBox<>();
        //comboLeaders.getItems().addAll("Uhuru Kenyattta", "William Ruto", "Raila Odinga", "Kalonzo Musyoka", "Moses Kuria", "Ken Lusaka", "William Oparanya");
        comboLeaders.setVisibleRowCount(5); /// use setOnAction
        comboLeaders.setEditable(true);
        comboLeaders.setPrefWidth(300);
        comboLeaders.setMaxWidth(300);
        comboLeaders.setMinWidth(300);
        comboLeaders.setPrefHeight(30);
        
        ObservableList<Politician> items = FXCollections.observableArrayList(dbData.getPoliticians());
        //items = ;
        //System.out.println(dbData.getPoliticians());
        // Create a FilteredList wrapping the ObservableList.
        FilteredList<Politician> filteredItems = new FilteredList<Politician>(items, p -> true);

        // Add a listener to the textProperty of the combobox editor. The
        // listener will simply filter the list every time the input is changed
        // as long as the user hasn't selected an item in the list.
        comboLeaders.setItems(filteredItems);
        
        comboLeaders.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            comboLeaders.show();
            final TextField editor = comboLeaders.getEditor();
            final Politician selected = comboLeaders.getSelectionModel().getSelectedItem();

            // This needs run on the GUI thread to avoid the error described
            // here: https://bugs.openjdk.java.net/browse/JDK-8081700.
            Platform.runLater(() -> {
                // If the no item in the list is selected or the selected item
                // isn't equal to the current input, we refilter the list.
                if (selected == null || !selected.toString().equals(editor.getText())) {
                    filteredItems.setPredicate(item -> {
                        // We return true for any items that starts with the
                        // same letters as the input. We use toUpperCase to
                        // avoid case sensitivity.
                        if (item.toString().toUpperCase().startsWith(newValue.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
        });

        
//        comboLeaders.setOnAction(e -> {
//            System.out.println(comboLeaders.getValue().getId());
//        });
        comboLeaders.setConverter(new StringConverter<Politician>() {

            @Override
            public String toString(Politician object) {
                if(object == null){
                    return null;
                }
                else{
                    return object.toString();
                }
            }

            @Override
            public Politician fromString(String string) {
                return comboLeaders.getItems().stream().filter(ap -> 
                    ap.toString().equals(string)).findFirst().orElse(null);
            }
        });
        comboLeaders.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null){
                id = newval.getId();
                populateData(newval.getId());
            }    
        });
        
        txtTermFrom = new TextField();
        txtTermFrom.setMinWidth(70);
        txtTermFrom.setMaxWidth(70);
        txtTermFrom.setPrefWidth(70);
        txtTermFrom.setPromptText("e.g. 1991");
        
        txtTermTo = new TextField();
        txtTermTo.setMinWidth(70);
        txtTermTo.setMaxWidth(70);
        txtTermTo.setPrefWidth(70);
        txtTermTo.setPromptText("e.g. 2002");
        
        txtTermRole = new TextField();
        txtTermRole.setMinWidth(90);
        txtTermRole.setMaxWidth(90);
        txtTermRole.setPrefWidth(90);
        txtTermRole.setPromptText("Role held");
        
        col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        
        col2 = new ColumnConstraints();
        col2.setPercentWidth(25);
        
        col3 = new ColumnConstraints();
        col3.setPercentWidth(25);
        
        col4 = new ColumnConstraints();
        col4.setPercentWidth(25);
        
        acc = new Accordion();
        
        vbPromsDeedsFromDb = new VBox(1);
        
        EditUI.this.setPadding(new Insets(10));
        EditUI.this.setHgap(10);
        EditUI.this.setVgap(10);
        EditUI.this.getColumnConstraints().addAll(col1, col2, col3, col4);
        EditUI.this.add(btnExit, 3, 0);
        GridPane.setHalignment(btnExit, HPos.RIGHT);
        EditUI.this.addRow(1, imgLogo);
        GridPane.setHalignment(imgLogo, HPos.CENTER);
        GridPane.setColumnSpan(imgLogo, 4);
        EditUI.this.addRow(2, lblEdit);
        GridPane.setColumnSpan(lblEdit, 4);
        GridPane.setHalignment(lblEdit, HPos.CENTER);
        EditUI.this.addRow(3, comboLeaders);
        GridPane.setColumnSpan(comboLeaders, 4);
        GridPane.setHalignment(comboLeaders, HPos.CENTER);
        EditUI.this.addRow(4, stackImg);
        GridPane.setColumnSpan(stackImg, 4);
        GridPane.setHalignment(stackImg, HPos.CENTER);
        EditUI.this.addRow(5, vbPromsDeedsFromDb);
        GridPane.setColumnSpan(vbPromsDeedsFromDb, 4);
        EditUI.this.addRow(6, acc);
        GridPane.setColumnSpan(acc, 4);
        EditUI.this.addRow(7, txtTermRole, txtTermFrom, txtTermTo, btnAdd);
        GridPane.setHalignment(txtTermFrom, HPos.RIGHT);
        GridPane.setHalignment(txtTermTo, HPos.LEFT);
        GridPane.setHalignment(btnAdd, HPos.LEFT);
        GridPane.setHalignment(txtTermRole, HPos.CENTER);
        EditUI.this.addRow(8, btnEditProfile);
        GridPane.setColumnSpan(btnEditProfile, 4);
        GridPane.setHalignment(btnEditProfile, HPos.CENTER);
    }
    
    public Button getBtnExit(){
        return btnExit;
    }
    
    public void addTerm(){
        TextArea txtPromise = new TextArea();
        txtPromise.setPrefSize(200, 50);
        txtPromise.setWrapText(true);
        txtPromise.setPromptText("What did he say he will do?");
        
        TextArea txtDeed = new TextArea();
        txtDeed.setPrefSize(200, 50);
        txtDeed.setWrapText(true);
        txtDeed.setPromptText("and what has he done?");
                
        VBox vbTextArea = new VBox(10, txtPromise, txtDeed);
        
        VBox vbItemsArea = new VBox(10);
        
        CheckBox chkTick = new CheckBox("walk talk");
        
        Button btnDeleteTerm = new Button("Delete term");
        
        
        Button btnSaveTermData = new Button("Save");
        btnSaveTermData.setOnAction(e -> {
            HBox hbDeed = new HBox(3);
            int success = 0;
            ImageView imgTickOrX;
            if(chkTick.isSelected()){
                imgTickOrX = new ImageView(new Image(this.getClass().getResource("/Material Icons_e5ca(0)_16.png").toString()));
            }
            else{
                imgTickOrX = new ImageView(new Image(this.getClass().getResource("/Material Icons_e5cd(0)_16.png").toString()));
            }
            try {
                if(txtPromise.getText().length() > 0 && txtDeed.getText().length() > 0){
                    success = dbData.newPromDeed(id, txtPromise.getText(), Integer.parseInt(btnDeleteTerm.getId()), txtDeed.getText(), chkTick.isSelected());
                    chkTick.setSelected(false);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill all fields.");
                    alert.show();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EditUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            Region region  = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            
            ImageView imgEdit = new ImageView(new Image(this.getClass().getResource("/Material Icons_e042(0)_16.png").toString()));
            
            Button btnEditSmall = new Button();
            btnEditSmall.setGraphic(imgEdit);
            btnEditSmall.setId(Integer.toString(success));
            btnEditSmall.setOnAction(f -> {
                if(dbData.deletePromDeed(Integer.parseInt(btnEditSmall.getId()))){
                    vbItemsArea.getChildren().remove(hbDeed);
                    //hbDeed.getChildren().clear();
                }
            });
            
            Label lblDeed = new Label(txtDeed.getText());
            lblDeed.setWrapText(true);
            
            Tooltip  toolTip = new Tooltip("Promise:\n" + txtPromise.getText());
            TooltipDelay.hackTooltipActivationTimer(toolTip, 1);
            TooltipDelay.hackTooltipHideTimer(toolTip, 1200000);
            toolTip.setWrapText(true);
            toolTip.getStyleClass().add("fontSize13");
            
            lblDeed.setTooltip(toolTip);
            
            txtPromise.clear();
            txtDeed.clear();
            
            hbDeed.getChildren().addAll(imgTickOrX, lblDeed, region, btnEditSmall);
            if(success > 0){
                vbItemsArea.getChildren().add(hbDeed);
            }
            
        });
        
        VBox vbButtonHolder = new VBox(20, btnDeleteTerm, chkTick, btnSaveTermData);
        
        
        HBox hbTerm = new HBox(10, vbTextArea, vbButtonHolder);
        hbTerm.setAlignment(Pos.CENTER);
        
        VBox vbActualPane = new VBox(10, vbItemsArea, hbTerm);
        vbActualPane.setAlignment(Pos.CENTER);

        if(txtTermFrom.getText().length() == 4 && txtTermTo.getText().length() == 4 && txtTermRole.getText().length() > 0){
            int idx = dbData.newTerm(id, txtTermRole.getText(), txtTermFrom.getText(), txtTermTo.getText());
            TitledPane tPane = new TitledPane(txtTermRole.getText() + " : " +txtTermFrom.getText() + " to " + txtTermTo.getText(), vbActualPane);
            //btnSaveTermData.setId(Integer.toString(idx));
            btnDeleteTerm.setId(Integer.toString(idx));
            btnDeleteTerm.setOnAction(g ->{
                if(dbData.deleteTermAndData(idx)){
                    acc.getPanes().remove(tPane);
                }
            });
            acc.getPanes().add(tPane);
            
        }
        else{
            alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setContentText("Year must be of 4 digits and role must be filled out.");
            alertError.show();
        }
    }
    
    public Button getBtnEditProfile(){
        return btnEditProfile;
    }
    
    public void populateData(int pol_id){
        acc.getPanes().clear();
        try {
            ResultSet rs = dbData.getPoliticianData(pol_id);
            if(rs.next()){
                try {
                    //Getting the image from the SQL result set
                    byte b[];
                    String filename = "/" + pol_id + "." + rs.getString("leader_img_ext");
                    File file = new File(System.getenv("APPDATA") + "/politica" + filename);
                    Blob blob = rs.getBlob("leader_img");
                    FileOutputStream fos = new FileOutputStream(file);
                    b = blob.getBytes(1, (int)blob.length());
                    fos.write(b);
                    //String filename = "/" + pol_id + "." + rs.getString("leader_img_ext");
                    
                    URL url = this.getClass().getResource(filename);
                    Image img = new Image("file:" + file.toPath());
//                    if(url != null){
//                        img = new Image(url.toString());
//                    }
                    imgUser = new ImageView(img);
                    Double aspectRatio = img.getWidth()/img.getHeight();
                    imgUser.setFitWidth(179);
                    imgUser.setPreserveRatio(true);
                    stackImg.getChildren().clear();
                    stackImg.getChildren().add(imgUser);
                    stackImg.setMaxHeight(179/aspectRatio + 10);
                    stackImg.setMinHeight(179/aspectRatio + 10);
                    stackImg.setPrefHeight(179/aspectRatio + 10);
                    stackImg.setMaxWidth(179 + 10);
                    stackImg.setMinWidth(179 + 10);
                    stackImg.setPrefWidth(179 + 10);
                    //now unpacking the data;
                } catch (SQLException ex) {
                    Logger.getLogger(EditUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(EditUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(EditUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                populatePromsDeeds(pol_id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void populatePromsDeeds(int id) throws SQLException{
        termsPromsDeeds = dbData.getPolPromDeed(id);
        //Add titled panes to the vbPromDeeed.
        for(Map.Entry<Vector<String>, ResultSet> termPromDeed: termsPromsDeeds.entrySet()){
            TextArea txtPromise = new TextArea();
            txtPromise.setPrefSize(200, 50);
            txtPromise.setWrapText(true);
            txtPromise.setPromptText("What did he say he will do?");

            TextArea txtDeed = new TextArea();
            txtDeed.setPrefSize(200, 50);
            txtDeed.setWrapText(true);
            txtDeed.setPromptText("and what has he done?");
            
            VBox vbTextArea = new VBox(10, txtPromise, txtDeed);
            VBox vbPromsDeeds = new VBox(10);
            
            CheckBox chkTick = new CheckBox("walk talk");
        
            Button btnDeleteTerm = new Button("Delete term");
            btnDeleteTerm.setId(termPromDeed.getKey().get(0));
            while(termPromDeed.getValue().next()){
                boolean fulfill = Integer.parseInt(termPromDeed.getValue().getString("deed_fulfills_promise")) == 1;
                PromDeed pd = new PromDeed(termPromDeed.getValue().getString("promise_verbose"), termPromDeed.getValue().getString("deed_verbose"), termPromDeed.getValue().getInt("deed_id"), fulfill);
                vbPromsDeeds.getChildren().add(pd);
            }
            Button btnSaveTermData = new Button("Save");
            btnSaveTermData.setOnAction(e -> {
                int success = 0;
                HBox hbDeed = new HBox(3);
                ImageView imgTickOrX;
                if(chkTick.isSelected()){
                    imgTickOrX = new ImageView(new Image(this.getClass().getResource("/Material Icons_e5ca(0)_16.png").toString()));
                }
                else{
                    imgTickOrX = new ImageView(new Image(this.getClass().getResource("/Material Icons_e5cd(0)_16.png").toString()));
                }
                try {
                    if(txtPromise.getText().length() > 0 && txtDeed.getText().length() > 0){
                        success = dbData.newPromDeed(id, txtPromise.getText(), Integer.parseInt(btnDeleteTerm.getId()), txtDeed.getText(), chkTick.isSelected());
                        chkTick.setSelected(false);
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Please fill all fields.");
                        alert.show();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(EditUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                Region region  = new Region();
                HBox.setHgrow(region, Priority.ALWAYS);

                ImageView imgEdit = new ImageView(new Image(this.getClass().getResource("/Material Icons_e042(0)_16.png").toString()));

                Button btnEditSmall = new Button();
                btnEditSmall.setGraphic(imgEdit);
                btnEditSmall.setId(Integer.toString(success));
                btnEditSmall.setOnAction(f -> {
                    if(dbData.deletePromDeed(Integer.parseInt(btnEditSmall.getId()))){
                        vbPromsDeeds.getChildren().remove(hbDeed);
                        //hbDeed.getChildren().clear();
                    }
                });

                Label lblDeed = new Label(txtDeed.getText());
                lblDeed.setWrapText(true);

                Tooltip  toolTip = new Tooltip("Promise:\n" + txtPromise.getText());
                TooltipDelay.hackTooltipActivationTimer(toolTip, 1);
                TooltipDelay.hackTooltipHideTimer(toolTip, 1200000);
                toolTip.setWrapText(true);
                toolTip.getStyleClass().add("fontSize13");

                lblDeed.setTooltip(toolTip);
                
                txtPromise.clear();
                txtDeed.clear();

                hbDeed.getChildren().addAll(imgTickOrX, lblDeed, region, btnEditSmall);
                if(success > 0){
                    vbPromsDeeds.getChildren().add(hbDeed);
                }

            });

            VBox vbButtonHolder = new VBox(20, btnDeleteTerm, chkTick, btnSaveTermData);



            
            HBox hbTerm = new HBox(10, vbTextArea, vbButtonHolder);
            hbTerm.setAlignment(Pos.CENTER);

            VBox vbActualPane = new VBox(10, vbPromsDeeds, hbTerm);
            vbActualPane.setAlignment(Pos.CENTER);
            
            //vbPromsDeeds.getChildren().add(btnEditTerm);
            //btnEditTerm.setId(termPromDeed.getKey().get(0));
            vbPromsDeeds.setAlignment(Pos.CENTER);
            String title = termPromDeed.getKey().get(1) + " : " + termPromDeed.getKey().get(2) + " to " + termPromDeed.getKey().get(3);
            TitledPane termPane = new TitledPane(title, vbActualPane);
            
            acc.getPanes().add(termPane);
            btnDeleteTerm.setOnAction(g ->{
                System.out.print(Integer.parseInt(termPromDeed.getKey().get(0)));
                if(dbData.deleteTermAndData(Integer.parseInt(termPromDeed.getKey().get(0)))){
                    acc.getPanes().remove(termPane);
                }
            });
        }
    }
}
