/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package politica;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author TONY
 */
public class MainUI extends BorderPane{
    
    private Image imgLogo;
    private ImageView iViewLogo;
    private HBox hBoxLogo;
    private Rectangle tipCard;
    private Label lblTip;
    private StackPane tipStack;
    private VBox vboxSideMenu;
    private ImageView imgLogout;
    private Region spacer;
    private TilePane optionTiles;
    //private String[] positions;
    private Button btnLogout;
    private ImageView imgTip;
    private HBox hboxTip;
    //private VBox tileTextLogo;
    private ImageView imgCoatofArms;
    private ImageView imgAdd;
    private Button btnAdd;
    private ImageView imgEdit;
    private Button btnEdit;
    private AddPolitician addUI;
    private Stage stageUI;
    private Scene sceneUI;
    private ScrollPane scrollUI;
    private EditUI editUI;
    private TextField txtSearch;
    private Map<String, String> positions;
    private DatabaseHelper dbData;
    
    public MainUI(DatabaseHelper db){
        
        this.dbData = db;
        
        imgTip = new ImageView(new Image(this.getClass().getResource("/Picture1.png").toString()));
        imgTip.setFitHeight(129);
        imgTip.setPreserveRatio(true);
        
        imgAdd = new ImageView(new Image(this.getClass().getResource("/Material Icons_e145(0)_48.png").toString()));
        imgEdit = new ImageView(new Image(this.getClass().getResource("/themify_e626(0)_48.png").toString()));
        imgLogout = new ImageView(new Image(this.getClass().getResource("/themify_e682(0)_48.png").toString()));
        
        btnEdit = new Button();
        btnEdit.setGraphic(imgEdit);
        btnEdit.setOnAction(e -> openEditDialog());
        
        btnLogout = new Button();
        btnLogout.setGraphic(imgLogout);
        
        
        
        btnAdd = new Button("");
        btnAdd.setGraphic(imgAdd);
        btnAdd.setOnAction(e -> openAddDialog());
                
        vboxSideMenu = new VBox(20, btnLogout /*, btnAdd, btnEdit*/);
        vboxSideMenu.getStyleClass().add("outline");
        
        imgLogo = new Image(this.getClass().getResource("/Politica-large-logos_transparent.png").toString());
        iViewLogo = new ImageView(imgLogo);
        iViewLogo.setPreserveRatio(true);
        iViewLogo.setFitWidth(300);
        
        tipCard = new Rectangle(1000, 120);
        tipCard.setStroke(Color.BLUE);
        tipCard.setStrokeWidth(0.1);
        tipCard.setArcHeight(10);
        tipCard.setArcWidth(10);
        tipCard.setFill(Color.WHITE);
        
        lblTip = new Label("\"The world bank ranks mental poverty as the worst kind of poverty in the world...that's why we created Politica.Yes precisely, to reducing mental poverty with such great poilitical correctness.\"\n\n(Mwanza Shem, Co-Founder, On the first unveiling of Politica)");
        //tip.getStyleClass().add("XFontSize");
        lblTip.setTextFill(Color.web("#273746"));
        lblTip.setPadding(new Insets(10));
        lblTip.setWrapText(true);
        //tip.setFont(Font.loadFont("file:resources/fonts/Montserrat-Regular.ttf", 15));
        
        lblTip.setFont(Font.loadFont(this.getClass().getResource("/fonts/OpenSans-Regular.ttf").toString(), 13));
        lblTip.getStyleClass().add("imageWindow");
        
        
        txtSearch = new TextField();
        txtSearch.setPromptText("Search Politica");
        txtSearch.setPrefWidth(200);
        txtSearch.setMinWidth(200);
        txtSearch.setMaxWidth(200);
        txtSearch.getStyleClass().add("searchBox");
        txtSearch.setOnKeyPressed((KeyEvent keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                InPosition subUI = new InPosition(txtSearch.getText(), dbData);
//                    Scene scene = new Scene(subUI);
//                    Stage stage = new Stage();
                subUI.getBtnBack().setOnAction(e -> {
                    MainUI.this.setCenter(optionTiles);
                });
//                    stage.setScene(scene);
//                    stage.initStyle(StageStyle.UNDECORATED);
//                    stage.initModality(Modality.APPLICATION_MODAL);
//                    stage.show(); 
                //ScrollPane spSubUI = new ScrollPane(subUI);
                MainUI.this.setCenter(subUI);
            }
        });
        
        HBox hbSearchBox = new HBox(txtSearch);
        hbSearchBox.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setMargin(txtSearch, new Insets(10));
        
        hboxTip = new HBox(15, imgTip, lblTip, hbSearchBox);
        hboxTip.setAlignment(Pos.CENTER);
        
        tipStack = new StackPane(tipCard, hboxTip);
        
        
        hBoxLogo = new HBox(20, iViewLogo, tipStack);
        hBoxLogo.setAlignment(Pos.CENTER_LEFT);
        hBoxLogo.setPadding(new Insets(10));
        HBox.setMargin(tipStack, new Insets(20));
        
        optionTiles = new TilePane();
        optionTiles.setHgap(10);
        optionTiles.setVgap(25);
        optionTiles.setPadding(new Insets(10));
        
        //positions = new String[]{"Presidency", "Deputy Presidency", "Gubernatorial", "Deputy Gubernatorial", "Senatorial", "MPs", "Women Rep", "MCAs"};
        positions = dbData.getRolesAsMap();
//        imgCoatofArms = new ImageView(new Image("file:C:\\Users\\TONY\\Documents\\NetBeansProjects\\Politica\\src\\politica\\1200px-Coat_of_arms_of_Kenya_(Official).svg.png"));
//        imgCoatofArms.setFitWidth(75);
//        imgCoatofArms.setPreserveRatio(true);
        
        for(Map.Entry<String, String> position: positions.entrySet()){
            Rectangle tile = new Rectangle(200, 200);
            tile.setFill(Color.GHOSTWHITE);
            tile.setStroke(Color.GREY);
            tile.setStrokeWidth(0.5);
            tile.setArcHeight(5);
            tile.setArcWidth(5);            
            tile.setOnMouseEntered(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    tile.setFill(Color.GAINSBORO);
                }
            });

            tile.setOnMouseExited(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    tile.setFill(Color.GHOSTWHITE);
                }
            });
            tile.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent t) {
                    InPosition subUI = new InPosition(Integer.parseInt(position.getKey()), dbData);
//                    Scene scene = new Scene(subUI);
//                    Stage stage = new Stage();
                    subUI.getBtnBack().setOnAction(e -> {
                        MainUI.this.setCenter(optionTiles);
                        
                    });
//                    stage.setScene(scene);
//                    stage.initStyle(StageStyle.UNDECORATED);
//                    stage.initModality(Modality.APPLICATION_MODAL);
//                    stage.show(); 
                    //ScrollPane spSubUI = new ScrollPane(subUI);
                    MainUI.this.setCenter(subUI);
                }
            });
            ImageView imgCoatofArms = new ImageView(new Image(this.getClass().getResource("/1200px-Coat_of_arms_of_Kenya_Official.svg_-1.png").toString()));
            imgCoatofArms.setFitWidth(75);
            imgCoatofArms.setPreserveRatio(true);
            Label lbl = new Label(position.getValue());
            lbl.getStyleClass().add("fontSize13");
            VBox tileTextLogo = new VBox(imgCoatofArms, lbl);
            tileTextLogo.setAlignment(Pos.CENTER);
            tileTextLogo.setMouseTransparent(true);
            //Group tileTextLogo = new Group(10, lbl, imgCoatofArms);
            StackPane sp = new StackPane(tile, tileTextLogo);
            optionTiles.getChildren().add(sp);
        }
        MainUI.this.setTop(hBoxLogo);
        MainUI.this.setLeft(vboxSideMenu);
        BorderPane.setMargin(vboxSideMenu, new Insets(10));
        MainUI.this.setCenter(optionTiles);
        
    }
    
    public Button getBtnLogout(){
        return btnLogout;
    }
    
//    public Button getBtnAdd(){
//        return btnAdd;
//    }
    
    public void openAddDialog(){
        stageUI = new Stage();
        addUI = new AddPolitician(stageUI, dbData);
        addUI.getBtnExit().setOnAction(e -> {
            stageUI.close();
        });
        StackPane spUI = new StackPane(addUI);
        spUI.setPrefWidth(400);
        scrollUI = new ScrollPane(spUI);
        
        scrollUI.getStyleClass().add("scrollPaneColor");
        //scrollUI.setFitToWidth(true);
        sceneUI = new Scene(scrollUI, Color.TRANSPARENT);
        sceneUI.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        stageUI.setScene(sceneUI);
        stageUI.initStyle(StageStyle.UNDECORATED);
        stageUI.initModality(Modality.APPLICATION_MODAL);
        stageUI.show();
    }
    
    public void openEditDialog(){
        stageUI = new Stage();
        editUI = new EditUI(stageUI, dbData);
        editUI.getBtnExit().setOnAction(e -> {
            stageUI.close();
        });
        StackPane spUI = new StackPane(editUI);
        spUI.setPrefWidth(400);
        scrollUI = new ScrollPane(spUI);
        scrollUI.getStyleClass().add("scrollPaneColor");
        scrollUI.setPrefViewportHeight(600);
        scrollUI.setFitToWidth(true);
        sceneUI = new Scene(scrollUI, Color.TRANSPARENT);
        sceneUI.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        stageUI.setScene(sceneUI);
        stageUI.initStyle(StageStyle.UNDECORATED);
        stageUI.initModality(Modality.APPLICATION_MODAL);
        stageUI.show();
    }
}
