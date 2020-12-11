package userinterface;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
import util.Entity;
import rocket.Rocket;

/**
 * An element that shows the user a visual and numerical representation of the
 * Rocket's horizontal velocity's magnitude and sign.
 */
public class HorizontalVelocityIndicator extends VelocityIndicator {
    
    /**
     * Creates a HorizontalVelocityIndicator with the given x and y offsets and
     * dimensions and passes it a Rocket to track the horizontal velocity of.
     * @param xOffset the HorizontalVelocityIndicator's x offset
     * @param yOffset the HorizontalVelocityIndicator's y offset
     * @param width the HorizontalVelocityIndicator's width
     * @param height the HorizontalVelocityIndicator's height
     * @param rocket the Rocket the HorizontalVelocityIndicator should track
     * the horizontal velocity of
     */
	public HorizontalVelocityIndicator(double xOffset, double yOffset, 
			double width, double height, Rocket rocket) {
		
		super(xOffset, yOffset, width, height, rocket);
		
    }
    
    /**
     * Creates a HorizontalVelocityIndicator with the given x offset and
     * dimensions and passes it a Rocket to track the horizontal velocity of.
     * @param xOffset the HorizontalVelocityIndicator's x offset
     * @param width the HorizontalVelocityIndicator's width
     * @param height the HorizontalVelocityIndicator's height
     * @param rocket the Rocket the HorizontalVelocityIndicator should track
     * the horizontal velocity of
     */
    public HorizontalVelocityIndicator(double xOffset,  
			double width, double height, Rocket rocket) {
		
		super(xOffset, 0, width, height, rocket);
		
	}

	@Override
	public String getVelocityText() {

		return ""+Math.min(
            Math.abs(Math.round(getRocket().getVelocity().getX() / 10) * 10), 
            999
        );

	}

	@Override
    protected void drawVelocityArrow(GraphicsContext gc) {

        gc.setLineWidth(3);
        double lineWidth = getHeight() / 2;

        double arrowWidth = 10;
        double arrowheadHeight = getWidth() / 5;
        double lineCenterX = getX() - getWidth() / 4;
        double lineCenterY = getY() + getHeight() / 2;

        double lineLeftX = lineCenterX - lineWidth / 2;
        double lineRightX = lineCenterX + lineWidth / 2;

        gc.setStroke(Color.BLACK);

		gc.strokeLine(lineLeftX, lineCenterY, lineRightX, lineCenterY);
		
        if (getRocket().getVelocity().getX() < 0) {
            
            // Draw velocity arrow pointed left
            gc.strokeLine(lineLeftX, lineCenterY, 
				lineLeftX + arrowWidth, lineCenterY + arrowheadHeight / 2);
			gc.strokeLine(lineLeftX, lineCenterY, 
                lineLeftX + arrowWidth, lineCenterY - arrowheadHeight / 2);

        } else {

			// Draw velocity arrow pointed right
			gc.strokeLine(lineRightX, lineCenterY,
				lineRightX - arrowWidth, lineCenterY + arrowheadHeight / 2);
			gc.strokeLine(lineRightX, lineCenterY,
				lineRightX - arrowWidth, lineCenterY - arrowheadHeight / 2);

		}
    }
	
}
