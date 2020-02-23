/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package politica;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 *
 * @author TONY
 */
public class PromDeedNoEdit extends HBox{

    private ImageView imgTickOrX;
    
    public PromDeedNoEdit (String promise, String deed, boolean fulfills){
        if(fulfills){
            imgTickOrX = new ImageView(new Image(this.getClass().getResource("/Material Icons_e5ca(0)_16.png").toString()));
        }
        else{
             imgTickOrX = new ImageView(new Image(this.getClass().getResource("/Material Icons_e5cd(0)_16.png").toString()));
        }
        
        Label lblDeed = new Label(deed);
        lblDeed.setWrapText(true);

        Tooltip  toolTip = new Tooltip("Promise:\n" + promise);
        TooltipDelay.hackTooltipActivationTimer(toolTip, 1);
        TooltipDelay.hackTooltipHideTimer(toolTip, 1200000);
        toolTip.setWrapText(true);
        toolTip.getStyleClass().add("fontSize13");

        lblDeed.setTooltip(toolTip);

        
        PromDeedNoEdit.this.setSpacing(3);
        
        PromDeedNoEdit.this.getChildren().addAll(imgTickOrX, lblDeed);
    }
}
