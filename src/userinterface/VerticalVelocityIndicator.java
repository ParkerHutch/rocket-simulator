package userinterface;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
import rocket.Entity;
import rocket.Rocket;

public class VerticalVelocityIndicator extends VelocityIndicator {
	
	public VerticalVelocityIndicator(double xOffset, double yOffset, 
			double width, double height, Rocket rocket) {
		
		super(xOffset, yOffset, width, height, rocket);
		
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
