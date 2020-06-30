import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RocketComputer {
	
	private Rocket rocket;
	private ManeuverCalculator maneuverCalculator;
	private AltitudeIndicator altitudeIndicator;
	
	private boolean maximized = true; 
	
	public RocketComputer(Rocket rocket, double groundY) {
		this.rocket = rocket;
		this.maneuverCalculator = new ManeuverCalculator(rocket, groundY);
		this.altitudeIndicator = 
				new AltitudeIndicator(20, 100, rocket, 
						getManeuverCalculator().calculateAltitude());
	}
	
	public Rocket getRocket() {
		return rocket;
	}

	public void setRocket(Rocket rocket) {
		this.rocket = rocket;
	}

	/**
	 * Gets the ManeuverCalculator associated with the Rocket
	 * @return the Rocket's ManeuverCalculator
	 */
	public ManeuverCalculator getManeuverCalculator() {
		return maneuverCalculator;
	}

	/**
	 * Sets the ManeuverCalculator associated with the Rocket
	 * @param maneuverCalculator the new ManeuverCalculator
	 */
	public void setManeuverCalculator(ManeuverCalculator maneuverCalculator) {
		this.maneuverCalculator = maneuverCalculator;
	}

	public AltitudeIndicator getAltitudeIndicator() {
		return altitudeIndicator;
	}

	public void setAltitudeIndicator(AltitudeIndicator altitudeIndicator) {
		this.altitudeIndicator = altitudeIndicator;
	}

	public boolean isMaximized() {
		return maximized;
	}

	public void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}
	
	public void tick(double timeElapsed) {
		
		// TODO do I need this?
		
	}
	
	public void draw(GraphicsContext gc) {
		
		double leftX = -gc.getTransform().getTx();
		double topY = -gc.getTransform().getTy();
		
		
		gc.setFill(Color.RED);
		gc.fillRect(leftX, topY, 100, 100);
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font(24));
		
		rocket.getVelocity().draw(rocket.getX(), 
				rocket.getY() + rocket.getHeight() / 2,
				Color.YELLOW, 3, gc);
		
		/*
		gc.fillText("Altitude: " + (int) getManeuverCalculator().calculateAltitude(), 
				leftX + 0, topY + gc.getFont().getSize());
		*/
		getAltitudeIndicator().setX(leftX + 20);
		getAltitudeIndicator().setY(topY + 20);
		getAltitudeIndicator().draw(gc);
		
		
		// TODO Use gc.getTransform().tX, etc. to draw
		
	}
	
}
