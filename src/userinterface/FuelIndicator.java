package userinterface;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import util.Entity;
import rocket.Rocket;

/**
 * An element that shows the user a visual representation of how much fuel the
 * Rocket has left.
 */
public class FuelIndicator extends Entity {
	
	private Rocket rocket;
	private double maxFuelLevel;
	private Color baseColor = Color.LIGHTGRAY;
	
	/**
	 * Creates a FuelIndicator with the given x and y offsets, dimensions, and
	 * passes it a Rocket to track the fuel of.
	 * @param xOffset the FuelIndicator's x offset
	 * @param yOffset the FuelIndicator's y offset
	 * @param width the FuelIndicator's width
	 * @param height the FuelIndicator's height
	 * @param rocket the Rocket to track the fuel of
	 */
	public FuelIndicator(double xOffset, double yOffset, 
			double width, double height, Rocket rocket) {
		
		super(xOffset, yOffset, width, height);
		this.rocket = rocket;
		this.maxFuelLevel = rocket.getFuel();
		
	}

	/**
	 * Creates a FuelIndicator with the given x offset and dimensions and 
	 * passes it a Rocket to track the fuel of.
	 * @param xOffset the FuelIndicator's x offset
	 * @param width the FuelIndicator's width
	 * @param height the FuelIndicator's height
	 * @param rocket the Rocket to track the fuel of
	 */
	public FuelIndicator(double xOffset, 
			double width, double height, Rocket rocket) {
		
		super(xOffset, 0, width, height);
		this.rocket = rocket;
		this.maxFuelLevel = rocket.getFuel();
		
	}

	/**
	 * Gets the Rocket that the FuelIndicator is tracking the fuel of.
	 * @return the FuelIndicator's Rocket
	 */
	private Rocket getRocket() {
		return rocket;
	}

	/**
	 * Sets the Rocket that the FuelIndicator should track the fuel of.
	 * @param rocket the FuelIndicator's new Rocket
	 */
	public void setRocket(Rocket rocket) {
		this.rocket = rocket;
	}

	/**
	 * Gets the FuelIndicator's main (fill) color.
	 * @return the FuelIndicator's base color
	 */
	public Color getBaseColor() {
		return baseColor;
	}

	/**
	 * Sets the FuelIndicator's main (fill) color.
	 * @param baseColor the FuelIndicator's new base color
	 */
	public void setBaseColor(Color baseColor) {
		this.baseColor = baseColor;
	}

	/**
	 * Gets the maximum amount of fuel the Rocket can have. This is usually
	 * the Rocket's initial fuel. This value will be the upper bound for the
	 * fuel bar.
	 * @return the Rocket's max fuel level
	 */
	public double getMaxFuelLevel() {
		return maxFuelLevel;
	}

	/**
	 * Sets the maximum fuel level. This value will be the upper bound for the
	 * fuel bar.
	 * @param maxFuelLevel the maximum fuel level
	 */
	public void setMaxFuelLevel(double maxFuelLevel) {
		this.maxFuelLevel = maxFuelLevel;
	}

	/**
	 * Draws evenly spaced horizontal tick marks on the FuelIndicator.
	 * @param gc the GraphicsContext used to draw the FuelIndicator
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
	 * Draws a dot to show how much fuel the Rocket has left, relative to the
	 * maximum fuel level.
	 * @param gc the GraphicsContext used to draw the FuelIndicator
	 */
	public void drawRocketFuelLevelPoint(GraphicsContext gc) {

		double currentFuelProportion = getRocket().getFuel() / getMaxFuelLevel();
		
		double barHeight = getHeight() * currentFuelProportion;

		gc.setFill(Color.LIGHTGREEN.darker());

		gc.fillRoundRect(getX() - getWidth() / 2, getY() + getHeight() - barHeight, 
				getWidth(), barHeight, 10, 10);
		
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
		// Base rectangle
		if (isVisible()) {
			
			gc.setFill(getBaseColor());
			gc.fillRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);

			drawRocketFuelLevelPoint(gc);
			drawTickMarks(gc);
		}
		
		
	}
	
	@Override
	public void tick(double timeElapsed) {
		
		if (rocket.getFuel() <= 0) {
			
			setBaseColor(Color.RED);
			
		}
		
	}
	
}
