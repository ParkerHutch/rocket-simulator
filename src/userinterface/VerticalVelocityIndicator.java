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
 * Rocket's vertical velocity's magnitude and sign.
 */
public class VerticalVelocityIndicator extends VelocityIndicator {
    
    /**
     * Creates a VerticalVelocityIndicator with the given x and y offsets and
     * dimensions and passes it a Rocket to track the vertical velocity of.
     * @param xOffset the VerticalVelocityIndicator's x offset
     * @param yOffset the VerticalVelocityIndicator's y offset
     * @param width the VerticalVelocityIndicator's width
     * @param height the VerticalVelocityIndicator's height
     * @param rocket the Rocket the VerticalVelocityIndicator should track
     * the vertical velocity of
     */
	public VerticalVelocityIndicator(double xOffset, double yOffset, 
			double width, double height, Rocket rocket) {
		
		super(xOffset, yOffset, width, height, rocket);
		
    }
    
    /**
     * Creates a VerticalVelocityIndicator with the given x offset and
     * dimensions and passes it a Rocket to track the vertical velocity of.
     * @param xOffset the VerticalVelocityIndicator's x offset
     * @param width the VerticalVelocityIndicator's width
     * @param height the VerticalVelocityIndicator's height
     * @param rocket the Rocket the VerticalVelocityIndicator should track
     * the vertical velocity of
     */
    public VerticalVelocityIndicator(double xOffset, 
			double width, double height, Rocket rocket) {
		
		super(xOffset, 0, width, height, rocket);
		
	}

	@Override
	public String getVelocityText() {

		return ""+Math.min(
            Math.abs(Math.round(getRocket().getVelocity().getY() / 10) * 10), 
            999
        );

	}

	@Override
    protected void drawVelocityArrow(GraphicsContext gc) {

        gc.setLineWidth(3);
        double arrowWidth = getWidth() / 5;
        double arrowheadHeight = 10;
        double lineCenterX = getX() - getWidth() / 4;
        double lineTopY = getY() + getHeight() / 4;
        double lineBottomY = getY() + 3 * getHeight() / 4;

        gc.setStroke(Color.BLACK);

        gc.strokeLine(lineCenterX, lineTopY, lineCenterX, lineBottomY);

        if (getRocket().getVelocity().getY() < 0) {
            
            // Draw velocity arrow pointed down
            gc.strokeLine(lineCenterX - arrowWidth / 2, lineTopY + arrowheadHeight, 
                lineCenterX, lineTopY);
            gc.strokeLine(lineCenterX + arrowWidth / 2, lineTopY + arrowheadHeight, 
                lineCenterX, lineTopY);

        } else {

            // Draw velocity arrow pointed up
            gc.strokeLine(lineCenterX - arrowWidth / 2, lineBottomY - arrowheadHeight, 
                lineCenterX, lineBottomY);
            gc.strokeLine(lineCenterX + arrowWidth / 2, lineBottomY - arrowheadHeight, 
                lineCenterX, lineBottomY);

        }

    }

}
