/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package politica;

import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

/**
 *
 * @author TONY
 */
public class PromDeed extends HBox{
    
    private  Button btnEditTerm;
    private ImageView imgTickOrX;
    private Region region;
    private ImageView imgEdit;
    private DatabaseHelper dbData;
    
    public PromDeed(String promise, String deed, int deed_id, boolean fulfills){
        try {
            dbData = new DatabaseHelper();
        } catch (SQLException ex) {
            Logger.getLogger(PromDeed.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(fulfills){
            imgTickOrX = new ImageView(new Image(this.getClass().getResource("/Material Icons_e5ca(0)_16.png").toString()));
        }
        else{
             imgTickOrX = new ImageView(new Image(this.getClass().getResource("/Material Icons_e5cd(0)_16.png").toString()));
        }
        
        region  = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
            
        imgEdit = new ImageView(new Image(this.getClass().getResource("/Material Icons_e042(0)_16.png").toString()));

        Button btnEditSmall = new Button();
        btnEditSmall.setGraphic(imgEdit);
        btnEditSmall.setId(Integer.toString(deed_id));
        btnEditSmall.setOnAction(f -> {
            if(dbData.deletePromDeed(Integer.parseInt(btnEditSmall.getId()))){
                PromDeed.this.getChildren().clear();
            }
        });
            
        Label lblDeed = new Label(deed);
        lblDeed.setWrapText(true);

        Tooltip  toolTip = new Tooltip("Promise:\n" + promise);
        TooltipDelay.hackTooltipActivationTimer(toolTip, 1);
        TooltipDelay.hackTooltipHideTimer(toolTip, 1200000);
        toolTip.setWrapText(true);
        toolTip.getStyleClass().add("fontSize13");

        lblDeed.setTooltip(toolTip);

        
        PromDeed.this.setSpacing(3);
        
        PromDeed.this.getChildren().addAll(imgTickOrX, lblDeed, region, btnEditSmall);
    }
}
