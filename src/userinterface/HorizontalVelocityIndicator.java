package userinterface;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
import rocket.Entity;
import rocket.Rocket;

public class HorizontalVelocityIndicator extends Entity {
	
	private double width;
	private double height;
	private Rocket rocket;
	
	public HorizontalVelocityIndicator(double xOffset, double yOffset, 
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

    private void drawVelocityText(GraphicsContext gc) {
		
		gc.setFill(Color.WHITE);
		
		gc.setFill(Color.BLACK);
		
		gc.setTextAlign(TextAlignment.CENTER);
		
		//gc.setFont(new Font(20)); TODO use tahoma font
		
		gc.setTextBaseline(VPos.CENTER); // TODO needed?
		
        gc.fillText(
            ""+Math.min(
                Math.abs(Math.round(getRocket().getVelocity().getX() / 10) * 10), 
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
