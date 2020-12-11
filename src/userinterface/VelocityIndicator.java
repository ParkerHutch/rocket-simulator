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
 * Rocket's velocity's magnitude and sign. The class should be extended to
 * implement the way the arrow and velocity are displayed as those aspects are
 * specific to which velocity (horizontal/vertical) is being displayed.
 */
public abstract class VelocityIndicator extends Entity {
	
	private Rocket rocket;
	
	/**
	 * Creates a VelocityIndicator with the given x and y offsets and 
	 * dimensions and passes it a Rocket to track the velocity of.
	 * @param xOffset the VelocityIndicator's x offset
	 * @param yOffset the VelocityIndicator's y offset
	 * @param width the VelocityIndicator's width
	 * @param height the VelocityIndicator's height
	 * @param rocket the Rocket the VelocityIndicator should track the velocity
	 * of
	 */
	public VelocityIndicator(double xOffset, double yOffset, 
			double width, double height, Rocket rocket) {
		
		super(xOffset, yOffset, width, height);
		this.rocket = rocket;
		
	}

	/**
	 * Gets the Rocket that the VelocityIndicator should be tracking.
	 * @return the VelocityIndicator's Rocket
	 */
	public Rocket getRocket() {
		return rocket;
	}

	/**
	 * Sets the Rocket that the VelocityIndicator should be tracking.
	 * @param rocket the VelocityIndicator's new Rocket
	 */
	public void setRocket(Rocket rocket) {
		this.rocket = rocket;
	}

	/**
	 * Draws a velocity arrow within the bounds of the VelocityIndicator
	 * representing the direction of velocity.
	 * @param gc the GraphicsContext used to draw the VelocityIndicator
	 */
    protected abstract void drawVelocityArrow(GraphicsContext gc);

	/**
	 * Gets a String representing the Rocket's velocity.
	 * @return the Rocket's velocity text
	 */
    protected abstract String getVelocityText();

	/**
	 * Draws the Rocket's velocity text box.
	 * @param gc the GraphicsContext to draw the VelocityIndicator on
	 */
    private void drawVelocityText(GraphicsContext gc) {
		
		gc.setFill(Color.WHITE);
		
		gc.setFill(Color.BLACK);
		
		gc.setTextAlign(TextAlignment.CENTER);
		
		gc.setTextBaseline(VPos.CENTER); 
		
        gc.fillText(
            getVelocityText(),
            getX() + getWidth() / 6, 
			getY() + getHeight() / 2);

    }
	
	/**
	 * Draws a background box covering the bounds of the VelocityIndicator.
	 * @param gc the GraphicsContext used to draw the VelocityIndicator
	 */
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
