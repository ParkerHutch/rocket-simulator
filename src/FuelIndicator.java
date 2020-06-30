import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FuelIndicator extends Entity {
	
	private double width;
	private double height;
	private Rocket rocket;
	private double maxFuelLevel;
	private Color baseColor = Color.LIGHTGRAY;
	
	public FuelIndicator(double xOffset, double yOffset, 
			double width, double height, Rocket rocket) {
		
		setxOffset(xOffset);
		setyOffset(yOffset);
		this.width = width;
		this.height = height;
		this.rocket = rocket;
		this.maxFuelLevel = rocket.getFuel();
		
	}

	public Rocket getRocket() {
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

	public Color getBaseColor() {
		return baseColor;
	}

	public void setBaseColor(Color baseColor) {
		this.baseColor = baseColor;
	}

	public double getMaxFuelLevel() {
		return maxFuelLevel;
	}

	public void setMaxFuelLevel(double maxFuelLevel) {
		this.maxFuelLevel = maxFuelLevel;
	}

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
