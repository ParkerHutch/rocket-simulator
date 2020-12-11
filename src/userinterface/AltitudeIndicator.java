package userinterface;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import util.Entity;
import rocket.Rocket;

/**
 * An element that shows the user a simple representation of the Rocket's
 * altitude when drawn.
 */
public class AltitudeIndicator extends Entity {
	
	private Rocket rocket;
	private double maxAltitude;
	
	/**
	 * Creates an AltitudeIndicator with the given x and y offsets and
	 * dimensions, and stores the Rocket it should be tracking as well as the 
	 * Rocket's initial altitude
	 * @param xOffset the AltitudeIndicator's x offset
	 * @param yOffset the AltitudeIndicator's y offset
	 * @param width the AltitudeIndicator's width
	 * @param height the AltitudeIndicator's height
	 * @param rocket the Rocket which the AltitudeIndicator tracks the height 
	 * of
	 * @param initialAltitude the Rocket's initial altitude
	 */
	public AltitudeIndicator(double xOffset, double yOffset, 
			double width, double height, Rocket rocket, 
			double initialAltitude) {
		
		super(xOffset, yOffset, width, height);
		this.rocket = rocket;

		this.maxAltitude = initialAltitude;
		
	}

	/**
	 * Creates an AltitudeIndicator with the given x offset and dimensions, a y
	 * offset of 0, and stores the Rocket it should be tracking as well as the 
	 * Rocket's initial altitude
	 * @param xOffset the AltitudeIndicator's x offset
	 * @param width the AltitudeIndicator's width
	 * @param height the AltitudeIndicator's height
	 * @param rocket the Rocket which the AltitudeIndicator tracks the height 
	 * of
	 * @param initialAltitude the Rocket's initial altitude
	 */
	public AltitudeIndicator(double xOffset,
			double width, double height, Rocket rocket, 
			double initialAltitude) {
		
		super(xOffset, 0, width, height);
		this.rocket = rocket;

		this.maxAltitude = initialAltitude;
		
	}

	/**
	 * Gets the Rocket that the AltitudeIndicator is tracking the altitude of.
	 * @return the AltitudeIndicator's Rocket
	 */
	private Rocket getRocket() {
		return rocket;
	}

	/**
	 * Sets the Rocket that the AltitudeIndicator should track the altitude of.
	 * @param rocket the Rocket to track
	 */
	public void setRocket(Rocket rocket) {
		this.rocket = rocket;
	}

	/**
	 * Gets the max altitude value to use when drawing the AltitudeIndicator 
	 * and determining the height of the Rocket's dot on the AltitudeIndicator.
	 * @return the max altitude
	 */
	private double getMaxAltitude() {
		return maxAltitude;
	}

	/**
	 * Sets the max altitude value to use when drawing the AltitudeIndicator 
	 * and determining the height of the Rocket's dot on the AltitudeIndicator.
	 * @param maxAltitude the new max altitude
	 */
	public void setMaxAltitude(double maxAltitude) {
		this.maxAltitude = maxAltitude;
	}
	
	/**
	 * Draws evenly spaced horizontal lines to represent increments of height.
	 * @param gc the GraphicsContext used to draw the AltitudeIndicator
	 */
	public void drawTickMarks(GraphicsContext gc) {
		
		gc.setFill(Color.BLACK);
		
		int numTickMarks = 3;
		
		double majorTickWidth = getWidth() / 1.5;
		double majorTickHeight = 5;
		
		double minorTickWidth = majorTickWidth / 1.5;
		double minorTickHeight = majorTickHeight / 1.5;
		
		// Top and bottom 'lines'
		gc.fillRect(getX() - majorTickWidth / 2, getY(), majorTickWidth, majorTickHeight);
		gc.fillRect(getX() - majorTickWidth / 2, getY() + getHeight() - majorTickHeight, 
				majorTickWidth, majorTickHeight);
		
		double insideHeight = getHeight() - majorTickHeight * 2;
		double spaceBetweenTicks = insideHeight / (numTickMarks + 1);
		
		for (int i = 1; i <= numTickMarks; i++) {
			
			gc.fillRect(getX() - minorTickWidth / 2, 
					getY() + majorTickHeight + (i * spaceBetweenTicks), 
					minorTickWidth, minorTickHeight);
			
		}
		
	}
	
	/**
	 * Draws a circle to represent the Rocket and its altitude.
	 * @param gc the GraphicsContext used to draw the AltitudeIndicator
	 */
	public void drawRocketAltitudePoint(GraphicsContext gc) {

		double pointRadius = 3;

		double currentAltitudeProportion = getRocket().getManeuverCalculator().calculateAltitude()
				/ maxAltitude;

		double scaledAltitudeY = getY() + getHeight() - pointRadius
				- (currentAltitudeProportion * (getHeight() - pointRadius));

		// Check to make sure that the dot will be drawn within the bounds
		// of the AltitudeIndicator (give or take 2 pixels)
		if (scaledAltitudeY - pointRadius >= getY() - 2 && scaledAltitudeY + pointRadius <= getY() + getHeight() + 2) {

			gc.setFill(Color.CORNFLOWERBLUE);
			gc.fillOval(getX() - pointRadius, scaledAltitudeY - pointRadius, pointRadius * 2, pointRadius * 2);

		}
		
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
		if (isVisible()) {
			
			gc.setFill(Color.LIGHTGRAY);
			gc.fillRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);

			drawTickMarks(gc);
			
			drawRocketAltitudePoint(gc);
		}
		
		
	}
	
	@Override
	public void tick(double timeElapsed) {
		
		double rocketAltitude = 
				getRocket().getManeuverCalculator().calculateAltitude();
		if (rocketAltitude > getMaxAltitude()) {
			
			setMaxAltitude(getMaxAltitude() * 2);
			
		}
		
	}
	
	
	
}
