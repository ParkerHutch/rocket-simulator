import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AltitudeIndicator extends Entity {
	
	private double width;
	private double height;
	private Rocket rocket;
	private double maxAltitude;
	
	public AltitudeIndicator(double xOffset, double yOffset, 
			double width, double height, Rocket rocket, 
			double initialAltitude) {
		
		setxOffset(xOffset);
		setyOffset(yOffset);
		this.width = width;
		this.height = height;
		this.rocket = rocket;
		this.maxAltitude = initialAltitude;
		
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

	public double getMaxAltitude() {
		return maxAltitude;
	}

	public void setMaxAltitude(double maxAltitude) {
		this.maxAltitude = maxAltitude;
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
	
	public void drawRocketAltitudePoint(GraphicsContext gc) {

		double pointRadius = 3;

		double currentAltitudeProportion = getRocket().getComputer().getManeuverCalculator().calculateAltitude()
				/ maxAltitude;

		double scaledAltitudeY = getY() + getHeight() - pointRadius
				- (currentAltitudeProportion * (getHeight() - pointRadius));

		// Check to make sure that the dot will be drawn within the bounds
		// of the AltitudeIndicator (give or take 2 pixels)
		if (scaledAltitudeY - pointRadius >= getY() - 2 && scaledAltitudeY + pointRadius <= getY() + getHeight() + 2) {

			gc.setFill(Color.BLUE);
			gc.fillOval(getX() - pointRadius, scaledAltitudeY - pointRadius, pointRadius * 2, pointRadius * 2);

		}
		
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
		// Base rectangle
		/*
		gc.setFill(Color.WHITE);
		gc.fillRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight());
		*/
		
		if (isVisible()) {
			gc.setFill(Color.LIGHTGRAY);
			gc.fillRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);

			// Altitude meter
			drawTickMarks(gc);
			// double altitudeBarWidth = getWidth() * (2.0 / 3.0);

			drawRocketAltitudePoint(gc);
		}
		
		
	}
	
	@Override
	public void tick(double timeElapsed) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
