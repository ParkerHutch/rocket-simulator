package userinterface;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
import rocket.Entity;
import rocket.Rocket;

public class VerticalVelocityIndicator extends Entity {
	
	private double width;
	private double height;
	private Rocket rocket;
	
	public VerticalVelocityIndicator(double xOffset, double yOffset, 
			double width, double height, Rocket rocket) {
		
		setxOffset(xOffset);
		setyOffset(yOffset);
		this.width = width;
		this.height = height;
		this.rocket = rocket;
		
	}

	private Rocket getRocket() {
		return rocket;
	}

	public void setRocket(Rocket rocket) {
		this.rocket = rocket;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

    private void drawVelocityArrow(GraphicsContext gc) {

        // arrow
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

    private void drawVelocityText(GraphicsContext gc) {
		
		gc.setFill(Color.WHITE);
		
		gc.setFill(Color.BLACK);
		
		gc.setTextAlign(TextAlignment.CENTER);
		
		//gc.setFont(new Font(20)); TODO use tahoma font
		
		gc.setTextBaseline(VPos.CENTER); // TODO needed?
		
        gc.fillText(
            ""+Math.min(
                Math.abs(Math.round(getRocket().getVelocity().getY() / 10) * 10), 
                999
            ),
            getX() + getWidth() / 6, 
			getY() + getHeight() / 2);

	}

	@Override
	public void draw(GraphicsContext gc) {
		
		if (isVisible()) {
            
            // Background box
			gc.setFill(Color.WHITE);
            gc.fillRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);
            gc.strokeRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);

            drawVelocityArrow(gc);
            drawVelocityText(gc);

		}
		
		
	}
	
	@Override
	public void tick(double timeElapsed) {
        
        // TODO might not even need this method
		
	}
	
	
	
}
