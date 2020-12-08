package userinterface;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import rocket.Entity;
import rocket.Rocket;

public class UserInterface extends Entity {

	private double minWidth = 0;
	private double maxWidth;
	private boolean maximized = true;
	
	private AltitudeIndicator altitudeIndicator;

	private FuelIndicator fuelIndicator;

	private TimeIndicator timeIndicator;

	private TogglePlayButton togglePlayButton;

	private VerticalVelocityIndicator verticalVelocityIndicator;

	private HorizontalVelocityIndicator horizontalVelocityIndicator;

	ArrayList<CustomButton> buttons = new ArrayList<CustomButton>();
	ArrayList<Entity> interfaceElements = new ArrayList<Entity>();
	
	private double transitionSpeed = 2;

	private double buttonMargin = 15;

	public UserInterface(double x, double y, double width, double height, 
			Rocket rocket, double groundY) {
		
		super(x, y);
		this.maxWidth = width * 2;
		setWidth(width * 2);
		setHeight(height);
		
		// TODO I shouldn't need the maneuver calculator below anymore,
		// should be able to just pass in the initial altitude in rocket simulator.java
		setAltitudeIndicator(new AltitudeIndicator(maxWidth / 4, 20, 30, 100, rocket, 
			rocket.getManeuverCalculator().calculateAltitude()));
		
		setFuelIndicator(new FuelIndicator(maxWidth / 4, 300, 30, 100, rocket));

		setTimeIndicator(new TimeIndicator(maxWidth / 4, 200, 70, 70));

		this.verticalVelocityIndicator = new VerticalVelocityIndicator(maxWidth / 4, 425, 70, 50, rocket);
		this.horizontalVelocityIndicator = new HorizontalVelocityIndicator(maxWidth / 4, 500, 70, 50, rocket);

		interfaceElements.add(getAltitudeIndicator());
		interfaceElements.add(getFuelIndicator());
		interfaceElements.add(getTimeIndicator());

		interfaceElements.add(horizontalVelocityIndicator);
		interfaceElements.add(verticalVelocityIndicator);

		setUniformYOffsets(interfaceElements);
		
		Entity bottomElement = interfaceElements.get(interfaceElements.size() - 1);
		double bottomElementBottomY = bottomElement.getyOffset() + bottomElement.getHeight();

		setTogglePlayButton(new TogglePlayButton(maxWidth / 4, 
			bottomElementBottomY + buttonMargin,
			30, 
			30
		));
		buttons.add(getTogglePlayButton());
		buttons.add(new MinimizeMaximizeButton((maxWidth + 20) / 2, height / 2 - 50, 20, 50));
		
	}

	private void setUniformYOffsets(ArrayList<Entity> elements) {

		Entity aboveEntity = elements.get(0);

		for (int i = 1; i < elements.size(); i++) {

			elements.get(i).setyOffset(
				aboveEntity.getyOffset() + 
				aboveEntity.getHeight() + 
				buttonMargin);

			aboveEntity = elements.get(i);

		}

	}

	/**
	 * Updates the Rocket-tracking indicators to track the given Rocket object.
	 * @param rocket the Rocket which the indicators should track
	 */
	public void focusElements(Rocket rocket) {
		getAltitudeIndicator().setRocket(rocket);
		getFuelIndicator().setRocket(rocket);
		verticalVelocityIndicator.setRocket(rocket);
		horizontalVelocityIndicator.setRocket(rocket);
	}

	public void reset() {

		getTogglePlayButton().setState("PAUSE");
		getTimeIndicator().setInternalTime(0);
		getTimeIndicator().setPaused(false);
		getTimeIndicator().setForcePaused(false);

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

	public double getTransitionSpeed() {
		return transitionSpeed;
	}

	public void setTransitionSpeed(double transitionSpeed) {
		this.transitionSpeed = transitionSpeed;
	}

	public AltitudeIndicator getAltitudeIndicator() {
		return this.altitudeIndicator;
	}

	public void setAltitudeIndicator(AltitudeIndicator altitudeIndicator) {
		this.altitudeIndicator = altitudeIndicator;
	}

	public FuelIndicator getFuelIndicator() {
		return this.fuelIndicator;
	}

	public void setFuelIndicator(FuelIndicator fuelIndicator) {
		this.fuelIndicator = fuelIndicator;
	}

	public TimeIndicator getTimeIndicator() {
		return this.timeIndicator;
	}

	public void setTimeIndicator(TimeIndicator timeIndicator) {
		this.timeIndicator = timeIndicator;
	}

	public TogglePlayButton getTogglePlayButton() {
		return this.togglePlayButton;
	}

	public void setTogglePlayButton(TogglePlayButton togglePlayButton) {
		this.togglePlayButton = togglePlayButton;
	}

	public ArrayList<CustomButton> getButtons() {
		return buttons;
	}

	public void setButtons(ArrayList<CustomButton> buttons) {
		this.buttons = buttons;
	}

	public ArrayList<Entity> getInterfaceElements() {
		return interfaceElements;
	}

	public void setInterfaceElements(ArrayList<Entity> interfaceElements) {
		this.interfaceElements = interfaceElements;
	}

	public boolean isMaximized() {
		return maximized;
	}

	public void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}
	
	public void tick(double timeElapsed) {

		boolean timeIndicatorShouldPause = false;
		// TODO I can clean a lot of this up since components are named now
		for (CustomButton button : getButtons()) {

			if (button.getClass() == MinimizeMaximizeButton.class) {

				MinimizeMaximizeButton toggleButton = (MinimizeMaximizeButton) button;

				if (isMaximized() != toggleButton.isMaximized()) {

					setMaximized(toggleButton.isMaximized());

				}

				if (isMaximized() && getWidth() < getMaxWidth()) {

					double widthDiff = getMaxWidth() - getWidth();
					setWidth(getWidth() + widthDiff / 10);

				} else if (!isMaximized() && getWidth() > getMinWidth()) {

					double widthDiff = getWidth() - getMinWidth();
					setWidth(widthDiff / (1 + 1 / getTransitionSpeed()));

				}

				toggleButton.setxOffset((getWidth() + toggleButton.getWidth()) / 2);

			} else {

				button.setDisable(!isMaximized());
				button.setxOffset(getWidth() / 4);

			}
			
			if (button.getClass() == TogglePlayButton.class) {
				
				timeIndicatorShouldPause = !((TogglePlayButton) button).getState().equals("PAUSE");
				
			}
			
		}

		for (Entity element : getInterfaceElements()) {
			
			if (element.getClass() == TimeIndicator.class) {
				
				((TimeIndicator) element).setPaused(timeIndicatorShouldPause);
				
			}
			
			element.tick(timeElapsed);
			element.setVisible(isMaximized());
			
		}
		
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
		double canvasLeftX = -gc.getTransform().getTx();
		double canvasTopY = -gc.getTransform().getTy();
		
		alignWith(canvasLeftX, canvasTopY);
		
		if (isVisible()) {
			
			gc.setFill(Color.GRAY);
			gc.fillRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);
			gc.setStroke(Color.BLACK);
			gc.strokeRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);
		
		}

		for (Entity element : getInterfaceElements()) {
			
			element.alignWith(this);
			element.setxOffset(getWidth() / 4);
			element.draw(gc);
			
		}

		for (CustomButton button : getButtons()) {
			
			if (!button.isDisabled()) {
				
				button.alignWith(this);
				button.draw(gc);
			
			}
			
		}
		
	}
	
	
}
