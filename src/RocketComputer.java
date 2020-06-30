import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RocketComputer extends Entity {
	
	private double minWidth = 0;
	private double maxWidth;
	private double width;
	private double height;
	private Rocket rocket;
	private ManeuverCalculator maneuverCalculator;
	private AltitudeIndicator altitudeIndicator;
	private TimeIndicator timeIndicator;
	private double internalTime = 0;
	
	private boolean maximized = true; 
	
	ArrayList<CustomButton> buttons = new ArrayList<CustomButton>();
	
	public RocketComputer(double x, double y, double width, double height, 
			Rocket rocket, double groundY, AnimationTimer animationTimer) {
		
		// TODO I don't think animationTimer needs to be passed
		super(x, y);
		this.maxWidth = width * 2;
		this.width = width * 2;
		this.height = height;
		this.rocket = rocket;
		this.maneuverCalculator = new ManeuverCalculator(rocket, groundY);
		this.altitudeIndicator = // TODO Just do buttons.add
				new AltitudeIndicator(maxWidth / 4, 20, 30, 100, rocket, 
						getManeuverCalculator().calculateAltitude());
		
		this.timeIndicator = new TimeIndicator(maxWidth / 4, 200, 70, 70);// TODO Just do buttons.add
		buttons.add(new TogglePlayButton(maxWidth / 4, 160, 30, 30, animationTimer));
		buttons.add(new MinimizeMaximizeButton((maxWidth + 20) / 2, height / 2 - 50, 20, 50));
		
	}
	
	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
	}

	public double getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
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

	public TimeIndicator getTimeIndicator() {
		return timeIndicator;
	}

	public void setTimeIndicator(TimeIndicator timeIndicator) {
		this.timeIndicator = timeIndicator;
	}

	public ArrayList<CustomButton> getButtons() {
		return buttons;
	}

	public void setButtons(ArrayList<CustomButton> buttons) {
		this.buttons = buttons;
	}

	public boolean isMaximized() {
		return maximized;
	}

	public void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}
	
	public double getInternalTime() {
		return internalTime;
	}

	public void setInternalTime(double internalTime) {
		this.internalTime = internalTime;
	}

	public void tick(double timeElapsed) {
		
		// TODO Why am I storing internal time?
		setInternalTime(getInternalTime() + timeElapsed);
		
		for (CustomButton button : getButtons()) {
			
			if (button.getClass() == MinimizeMaximizeButton.class) {
				
				MinimizeMaximizeButton toggleButton = 
						(MinimizeMaximizeButton) button;
				
				if (isMaximized() != toggleButton.isMaximized()) {
					
					setMaximized(toggleButton.isMaximized());
					
				}
				
				if (isMaximized() && getWidth() < getMaxWidth()) {
					
					double widthDiff = getMaxWidth() - getWidth();
					setWidth(getWidth() + widthDiff / 2);
					
				} else if (!isMaximized() && getWidth() > getMinWidth()){
					
					double widthDiff = getWidth() - getMinWidth();
					setWidth(widthDiff / 2);
					
				}
				
				toggleButton.setxOffset((getWidth() + toggleButton.getWidth()) / 2);
				
			} else {
			
				button.setDisable(!isMaximized());
				button.setVisible(isMaximized());
				//setVisible(isMaximized());

			}
		}
		
		altitudeIndicator.setVisible(isMaximized());
		timeIndicator.setVisible(isMaximized());
		
		timeIndicator.tick(timeElapsed);
		
		
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
		double canvasLeftX = -gc.getTransform().getTx();
		double canvasTopY = -gc.getTransform().getTy();
		
		alignWith(canvasLeftX, canvasTopY);
		
		if (isVisible()) {
			
			gc.setFill(Color.GRAY);
			gc.fillRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);
			gc.setFill(Color.BLACK);
			gc.strokeRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);
		
		}

		rocket.getVelocity().draw(rocket.getX(), 
				rocket.getY() + rocket.getHeight() / 2,
				Color.YELLOW, 3, gc);
		
		getAltitudeIndicator().alignWith(this);
		getAltitudeIndicator().draw(gc);
		
		getTimeIndicator().alignWith(this);
		getTimeIndicator().draw(gc);
		
		for (CustomButton button : getButtons()) {
			
			if (!button.isDisabled()) {
				
				button.alignWith(this);
				button.draw(gc);
			
			}
			
		}
		
	}
	
}
