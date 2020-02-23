/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package politica;

/**
 *
 * @author TONY
 */
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.lang.reflect.Field;

/**
 * @author rdeardorff
 */
public class TooltipDelay {

    public static void hackTooltipActivationTimer( Tooltip tooltip, int delay ) {
        hackTooltipTiming( tooltip, delay, "activationTimer" );
    }

    public static void hackTooltipHideTimer( Tooltip tooltip, int delay ) {
        hackTooltipTiming( tooltip, delay, "hideTimer" );
    }

    private static void hackTooltipTiming( Tooltip tooltip, int delay, String property ) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField( "BEHAVIOR" );
            fieldBehavior.setAccessible( true );
            Object objBehavior = fieldBehavior.get( tooltip );

            Field fieldTimer = objBehavior.getClass().getDeclaredField( property );
            fieldTimer.setAccessible( true );
            Timeline objTimer = (Timeline) fieldTimer.get( objBehavior );

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add( new KeyFrame( new Duration( delay ) ) );
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}

