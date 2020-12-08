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

	private MinimizeMaximizeButton minimizeMaximizeButton;

	ArrayList<CustomButton> buttons = new ArrayList<CustomButton>();
	ArrayList<Entity> interfaceElements = new ArrayList<Entity>();
	
	private double transitionSpeed = 2;

	private double buttonMargin = 15;

	public UserInterface(double x, double y, double width, double height, 
			Rocket rocket, double groundY, double rocketInitialHeight) {
		
		super(x, y);
		setMaxWidth(width * 2);
		setWidth(width * 2);
		setHeight(height);
		
		// TODO I shouldn't need the maneuver calculator below anymore,
		// should be able to just pass in the initial altitude in rocket simulator.java
		setAltitudeIndicator(new AltitudeIndicator(maxWidth / 4, 20, 30, 100, rocket, 
			rocketInitialHeight));
		
		setFuelIndicator(new FuelIndicator(maxWidth / 4, 30, 100, rocket));

		setTimeIndicator(new TimeIndicator(maxWidth / 4, 70, 70));

		setVerticalVelocityIndicator(new VerticalVelocityIndicator(maxWidth / 4, 70, 50, rocket));
		setHorizontalVelocityIndicator(new HorizontalVelocityIndicator(maxWidth / 4, 70, 50, rocket));

		interfaceElements.add(getAltitudeIndicator());
		interfaceElements.add(getFuelIndicator());
		interfaceElements.add(getTimeIndicator());

		interfaceElements.add(getHorizontalVelocityIndicator());
		interfaceElements.add(getVerticalVelocityIndicator());

		setUniformYOffsets(interfaceElements);
		
		Entity bottomElement = interfaceElements.get(interfaceElements.size() - 1);
		double bottomElementBottomY = bottomElement.getyOffset() + bottomElement.getHeight();

		setTogglePlayButton(new TogglePlayButton(maxWidth / 4, 
			bottomElementBottomY + buttonMargin,
			30, 
			30
		));
		buttons.add(getTogglePlayButton());

		setMinimizeMaximizeButton(new MinimizeMaximizeButton((maxWidth + 20) / 2, height / 2 - 50, 20, 50));
		buttons.add(getMinimizeMaximizeButton());
		
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


	public MinimizeMaximizeButton getMinimizeMaximizeButton() {
		return this.minimizeMaximizeButton;
	}

	public void setMinimizeMaximizeButton(MinimizeMaximizeButton minimizeMaximizeButton) {
		this.minimizeMaximizeButton = minimizeMaximizeButton;
	}


	public VerticalVelocityIndicator getVerticalVelocityIndicator() {
		return this.verticalVelocityIndicator;
	}

	public void setVerticalVelocityIndicator(VerticalVelocityIndicator verticalVelocityIndicator) {
		this.verticalVelocityIndicator = verticalVelocityIndicator;
	}

	public HorizontalVelocityIndicator getHorizontalVelocityIndicator() {
		return this.horizontalVelocityIndicator;
	}

	public void setHorizontalVelocityIndicator(HorizontalVelocityIndicator horizontalVelocityIndicator) {
		this.horizontalVelocityIndicator = horizontalVelocityIndicator;
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

		// Derive the UI's maximized state from the minimize/maximize button
		if (isMaximized() != getMinimizeMaximizeButton().isMaximized()) {
			setMaximized(getMinimizeMaximizeButton().isMaximized());
		}

		if (isMaximized() && getWidth() < getMaxWidth()) {

			/*
				If the UI is maximized but doesn't have the maximized width
				yet, increase the width by an amount proportional to the
				difference between the current width and the maximized width.
			*/
			double widthDiff = getMaxWidth() - getWidth();
			setWidth(getWidth() + widthDiff / getTransitionSpeed());

		} else if (!isMaximized() && getWidth() > getMinWidth()) {

			/*
				If the UI is minimized but doesn't have the minimized width
				yet, decrease the width by an amount proportional to the
				difference between the current width and the minimized width.
			*/
			double widthDiff = getWidth() - getMinWidth();
			setWidth(widthDiff / (1 + 1 / getTransitionSpeed()));

		}

		/*
			Set the x-position pf the minimize/maximize button so it's on the 
			right of the UI bar
		*/
		getMinimizeMaximizeButton().setxOffset((getWidth() + getMinimizeMaximizeButton().getWidth()) / 2);

		for (CustomButton button : getButtons()) {

			if (button.getClass() != MinimizeMaximizeButton.class) {

				button.setDisable(!isMaximized());
				button.setxOffset(getWidth() / 4);

			} 
			
		}

		// Derive the TimeIndicator's paused state from the TogglePlayButton's state
		boolean timeIndicatorShouldPause = !getTogglePlayButton().getState().equals("PAUSE");
		getTimeIndicator().setPaused(timeIndicatorShouldPause);

		for (Entity element : getInterfaceElements()) {
			
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
