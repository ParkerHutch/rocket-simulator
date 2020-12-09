package userinterface;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
import util.Entity;
import rocket.Rocket;

public abstract class VelocityIndicator extends Entity {
	
	private Rocket rocket;
	
	public VelocityIndicator(double xOffset, double yOffset, 
			double width, double height, Rocket rocket) {
		
		super(xOffset, yOffset, width, height);
		this.rocket = rocket;
		
	}

	public Rocket getRocket() {
		return rocket;
	}

	public void setRocket(Rocket rocket) {
		this.rocket = rocket;
	}

    protected abstract void drawVelocityArrow(GraphicsContext gc);

    protected abstract String getVelocityText();

    private void drawVelocityText(GraphicsContext gc) {
		
		gc.setFill(Color.WHITE);
		
		gc.setFill(Color.BLACK);
		
		gc.setTextAlign(TextAlignment.CENTER);
		
		//gc.setFont(new Font(20)); TODO use tahoma font
		
		gc.setTextBaseline(VPos.CENTER); // TODO needed?
		
        gc.fillText(
            getVelocityText(),
            getX() + getWidth() / 6, 
			getY() + getHeight() / 2);

    }
    
    private void drawBackgroundBox(GraphicsContext gc) {

        gc.setFill(Color.WHITE);
        gc.fillRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);

    }

	@Override
	public void draw(GraphicsContext gc) {
		
		if (isVisible()) {
            
            drawBackgroundBox(gc);
            drawVelocityArrow(gc);
            drawVelocityText(gc);

		}
		
    }
    
    @Override
	public void tick(double timeElapsed) {}
	
}
