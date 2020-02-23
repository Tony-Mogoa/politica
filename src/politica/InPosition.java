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
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

/**
 *
 * @author TONY
 */
public class InPosition extends BorderPane{
    
    private Button btnBack;
    private ImageView imgBack;
    private TilePane politicians;
    private DatabaseHelper dbData;
    private ScrollPane sp;
    private ProgressBar pb;
    private HBox header;
    private VBox vbCard;
    private Region region;
    
    public InPosition(int role_id, DatabaseHelper db){
        this.dbData = db;
        
        politicians = new TilePane();
        politicians.setPadding(new Insets(10));
        politicians.setHgap(10);
        politicians.setVgap(10);
        
        imgBack = new ImageView(new Image(this.getClass().getResource("/Entypo_e75d(0)_48.png").toString()));
        imgBack.setFitHeight(32);
        imgBack.setFitWidth(32);
        btnBack = new Button();
        btnBack.setGraphic(imgBack);
        
        pb = new ProgressBar();
        
        region = new Region();
        
        header = new HBox(10, btnBack, region, pb);
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox.setMargin(pb, new Insets(15));
        
        populateCards(role_id);
        
        
        InPosition.this.setTop(header);
        
        InPosition.this.setAlignment(header, Pos.CENTER);
        InPosition.this.setPadding(new Insets(10));
    }
    
    public InPosition(String leaderName, DatabaseHelper db){
        this.dbData = db;
        
        politicians = new TilePane();
        politicians.setPadding(new Insets(10));
        politicians.setHgap(10);
        politicians.setVgap(10);
        
        imgBack = new ImageView(new Image(this.getClass().getResource("/Entypo_e75d(0)_48.png").toString()));
        imgBack.setFitHeight(32);
        imgBack.setFitWidth(32);
        btnBack = new Button();
        btnBack.setGraphic(imgBack);
        
        pb = new ProgressBar();
        
        region = new Region();
        
        header = new HBox(10, btnBack, region, pb);
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox.setMargin(pb, new Insets(15));
        
        populateCards(leaderName);
        
        
        InPosition.this.setTop(header);
        
        InPosition.this.setAlignment(header, Pos.CENTER);
        InPosition.this.setPadding(new Insets(10));
    }
    
    public Button getBtnBack(){
        return btnBack;
    }
    
    public void populateCards(int role_id){
        Task task = new Task<Void>() {
            @Override public Void call() {
                try {
                    ResultSet rs = dbData.getPoliticians(role_id);
                    int counter = 1;
                    rs.last();
                    int size= rs.getRow();
                    rs.beforeFirst();
                    while(rs.next()){
                        Accordion acc = new Accordion();
                        Label lblLeaderNames = new Label();
                        StackPane stackImg = new StackPane();
                        ResultSet rsPolData = dbData.getPoliticianData(rs.getInt("leader_id"));
                        if(rsPolData.next()){
                            //Getting the image from the SQL result set
                            byte b[];
                            File file = new File(System.getenv("APPDATA") + "/politica/" + rs.getInt("leader_id") + "." + rsPolData.getString("leader_img_ext"));
                            Blob blob = rsPolData.getBlob("leader_img");
                            FileOutputStream fos = new FileOutputStream(file);
                            b = blob.getBytes(1, (int)blob.length());
                            fos.write(b);

                            Image img = new Image("file:" + file.toPath());
                            ImageView imgUser = new ImageView(img);
                            Double aspectRatio = img.getWidth()/img.getHeight();
                            imgUser.setFitWidth(180);
                            imgUser.setPreserveRatio(true);

                            stackImg = new StackPane(imgUser);
                            stackImg.getStyleClass().add("imageWindow");
                            stackImg.setPadding(new Insets(10));
                            stackImg.getChildren().clear();
                            stackImg.getChildren().add(imgUser);
                            stackImg.setMaxHeight(180/aspectRatio + 10);
                            stackImg.setMinHeight(180/aspectRatio + 10);
                            stackImg.setPrefHeight(180/aspectRatio + 10);
                            stackImg.setMaxWidth(180 + 10);
                            stackImg.setMinWidth(180 + 10);
                            stackImg.setPrefWidth(180 + 10);

                            
                            lblLeaderNames = new Label(rsPolData.getString("leader_first_name") + " " + rsPolData.getString("leader_last_name"));
                            lblLeaderNames.getStyleClass().add("fontSize13");
                            lblLeaderNames.getStyleClass().add("boldFont");

                            Map<Vector<String>, ResultSet> termsPromsDeeds = dbData.getPolPromDeed(rs.getInt("leader_id"));
                            //Add titled panes to the vbPromDeeed.
                            for(Map.Entry<Vector<String>, ResultSet> termPromDeed: termsPromsDeeds.entrySet()){

                                VBox vbPromsDeeds = new VBox(5);

                                while(termPromDeed.getValue().next()){
                                    boolean fulfill = Integer.parseInt(termPromDeed.getValue().getString("deed_fulfills_promise")) == 1;
                                    PromDeedNoEdit pd = new PromDeedNoEdit(termPromDeed.getValue().getString("promise_verbose"), termPromDeed.getValue().getString("deed_verbose"), fulfill);
                                    vbPromsDeeds.getChildren().add(pd);
                                }

                                String title = termPromDeed.getKey().get(1) + " : " + termPromDeed.getKey().get(2) + " to " + termPromDeed.getKey().get(3);

                                TitledPane termPane = new TitledPane(title, vbPromsDeeds);

                                acc.getPanes().add(termPane);
                            }
                        }
                        VBox vbCard = new VBox(10, stackImg, lblLeaderNames, acc);
                        vbCard.getStyleClass().add("imageWindow");
                        vbCard.setPadding(new Insets(10));
                        vbCard.setAlignment(Pos.CENTER);
                        politicians.getChildren().add(vbCard);
                        updateProgress(counter, size);
                        counter++;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(InPosition.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(InPosition.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(InPosition.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        };
        new Thread(task).start();
        pb.progressProperty().bind(task.progressProperty());
        
        task.setOnSucceeded(h -> {
            header.getChildren().remove(pb);
            sp = new ScrollPane(politicians);
            InPosition.this.setCenter(sp);
        });
          
        
    }
    
    public void populateCards(String leaderName){
        Task task = new Task<Void>() {
            @Override public Void call() {
                try {
                    ResultSet rs = dbData.getPoliticianSearches(leaderName);
                    int counter = 1;
                    rs.last();
                    int size= rs.getRow();
                    rs.beforeFirst();
                    while(rs.next()){
                        Accordion acc = new Accordion();
                        Label lblLeaderNames = new Label();
                        StackPane stackImg = new StackPane();
                        ResultSet rsPolData = dbData.getPoliticianData(rs.getInt("leader_id"));
                        if(rsPolData.next()){
                            //Getting the image from the SQL result set
                            byte b[];
                            File file = new File(System.getenv("APPDATA") + "/politica/" + rs.getInt("leader_id") + "." + rsPolData.getString("leader_img_ext"));
                            Blob blob = rsPolData.getBlob("leader_img");
                            FileOutputStream fos = new FileOutputStream(file);
                            b = blob.getBytes(1, (int)blob.length());
                            fos.write(b);

                            Image img = new Image("file:" + file.toPath());
                            ImageView imgUser = new ImageView(img);
                            Double aspectRatio = img.getWidth()/img.getHeight();
                            imgUser.setFitWidth(180);
                            imgUser.setPreserveRatio(true);

                            stackImg = new StackPane(imgUser);
                            stackImg.getStyleClass().add("imageWindow");
                            stackImg.setPadding(new Insets(10));
                            stackImg.getChildren().clear();
                            stackImg.getChildren().add(imgUser);
                            stackImg.setMaxHeight(180/aspectRatio + 10);
                            stackImg.setMinHeight(180/aspectRatio + 10);
                            stackImg.setPrefHeight(180/aspectRatio + 10);
                            stackImg.setMaxWidth(180 + 10);
                            stackImg.setMinWidth(180 + 10);
                            stackImg.setPrefWidth(180 + 10);


                            lblLeaderNames = new Label(rsPolData.getString("leader_first_name") + " " + rsPolData.getString("leader_last_name"));
                            lblLeaderNames.getStyleClass().add("fontSize13");
                            lblLeaderNames.getStyleClass().add("boldFont");

                            Map<Vector<String>, ResultSet> termsPromsDeeds = dbData.getPolPromDeed(rs.getInt("leader_id"));
                            //Add titled panes to the vbPromDeeed.
                            for(Map.Entry<Vector<String>, ResultSet> termPromDeed: termsPromsDeeds.entrySet()){

                                VBox vbPromsDeeds = new VBox(5);

                                while(termPromDeed.getValue().next()){
                                    boolean fulfill = Integer.parseInt(termPromDeed.getValue().getString("deed_fulfills_promise")) == 1;
                                    PromDeedNoEdit pd = new PromDeedNoEdit(termPromDeed.getValue().getString("promise_verbose"), termPromDeed.getValue().getString("deed_verbose"), fulfill);
                                    vbPromsDeeds.getChildren().add(pd);
                                }

                                String title = termPromDeed.getKey().get(1) + " : " + termPromDeed.getKey().get(2) + " to " + termPromDeed.getKey().get(3);

                                TitledPane termPane = new TitledPane(title, vbPromsDeeds);

                                acc.getPanes().add(termPane);
                            }
                        }
                        VBox vbCard = new VBox(10, stackImg, lblLeaderNames, acc);
                        vbCard.getStyleClass().add("imageWindow");
                        vbCard.setPadding(new Insets(10));
                        vbCard.setAlignment(Pos.CENTER);
                        politicians.getChildren().add(vbCard);
                        updateProgress(counter, size);
                        counter++;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(InPosition.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(InPosition.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(InPosition.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        };
        new Thread(task).start();
        pb.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(h -> {
            header.getChildren().remove(pb); 
            sp = new ScrollPane(politicians);
            InPosition.this.setCenter(sp);
        });
        
    }
}
                
            
        
    