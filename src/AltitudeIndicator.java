import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AltitudeIndicator extends Entity {
	
	private double width;
	private double height;
	private Rocket rocket;
	private double maxAltitude;
	
	
	
	public AltitudeIndicator(double width, double height, Rocket rocket, double initialAltitude) {
		this.width = width;
		this.height = height;
		this.rocket = rocket;
		/*
		this.maxAltitude = 
				rocket.getComputer().getManeuverCalculator().calculateAltitude();
				*/
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
	
	public void drawLables(GraphicsContext gc) {
		
		gc.fillText("Altitude", getX() - getWidth() / 2, getY());
		
	}
	@Override
	public void draw(GraphicsContext gc) {
		
		// Base rectangle
		gc.setFill(Color.WHITE);
		gc.fillRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight());
		
		// Altitude meter
		gc.setFill(Color.GREEN);
		double altitudeBarWidth = getWidth() * (2.0 / 3.0);
		
		double currentAltitudeProportion = 
				getRocket().getComputer().getManeuverCalculator().calculateAltitude() / 
				maxAltitude;
		
		double scaledAltitudeY = 
				getY() + getHeight() - (currentAltitudeProportion * getHeight());
		
		gc.fillRect(getX() - altitudeBarWidth / 2.0, scaledAltitudeY, 
				altitudeBarWidth, getHeight() * currentAltitudeProportion);
		
		drawLables(gc);
		
	}
	
	@Override
	public void tick(double timeElapsed) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
