package userinterface;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import util.Entity;
import rocket.Rocket;

/**
 * A class used to show the user a sidebar of simulation controls. The UI's
 * elements are publicly accessible so that the simulation class can access
 * their states to determine aspects of the simulation (i.e. the simulator 
 * class reads the TogglePlayButton's state to determine if the simulation
 * should be paused)
 */
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

	private double elementVerticalSpacing = 15;


	/**
	 * Creates a UserInterface with the given center x and y coordinates and
	 * dimensions and passes it a rocket, ground y-coordinate, and initial 
	 * rocket height to use when creating its elements.
	 * @param x the center x-coordinate of the UserInterface
	 * @param y the center y-coordinate of the UserInterface
	 * @param width the width of the UserInterface
	 * @param height the height of the UserInterface
	 * @param rocket the Rocket that the UserInterface's elements should track
	 * @param groundY the ground's top y-coordinate
	 * @param rocketInitialHeight the Rocket's initial distance from the ground
	 */
	public UserInterface(double x, double y, double width, double height, 
			Rocket rocket, double groundY, double rocketInitialHeight) {
		
		super(x, y);
		setMaxWidth(width * 2);
		setWidth(width * 2);
		setHeight(height);
		
		createElements(rocket, rocketInitialHeight);

		Entity bottomElement = getInterfaceElements().get(
			getInterfaceElements().size() - 1);
		double bottomElementBottomY = bottomElement.getyOffset() + bottomElement.getHeight();
			
		double togglePlayButtonTopY = bottomElementBottomY + buttonMargin;
		
		createButtons(togglePlayButtonTopY);
		
	}

	/**
	 * Creates various elements and adds them to the UserInterface's 
	 * interfaceElements list.
	 * @param rocket the Rocket that the elements should track
	 * @param rocketInitialHeight the Rocket's initial distance from the ground
	 */
	private void createElements(Rocket rocket, double rocketInitialHeight) {

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

		setUniformYOffsets(getInterfaceElements());

	}

	/**
	 * Creates buttons involved with the UserInterface.
	 * @param togglePlayButtonTopY the top y-coordinate for the 
	 * TogglePlayButton
	 */
	private void createButtons(double togglePlayButtonTopY) {

		setTogglePlayButton(new TogglePlayButton(getMaxWidth() / 4, 
			togglePlayButtonTopY,
			30, 
			30
		));
		buttons.add(getTogglePlayButton());

		setMinimizeMaximizeButton(new MinimizeMaximizeButton(
			(getMaxWidth() + 20) / 2, getHeight() / 2 - 50, 20, 50));
		buttons.add(getMinimizeMaximizeButton());

	}

	/**
	 * Vertically spaces out the Entities in the given list evenly, according
	 * to the <code>elementVerticalSpacing</code> variable.
	 * @param elements the elements to space out vertically
	 */
	private void setUniformYOffsets(ArrayList<Entity> elements) {

		Entity aboveEntity = elements.get(0);

		for (int i = 1; i < elements.size(); i++) {

			elements.get(i).setyOffset(
				aboveEntity.getyOffset() + 
				aboveEntity.getHeight() + 
				elementVerticalSpacing);

			aboveEntity = elements.get(i);

		}

	}

	/**
	 * Updates the Rocket-tracking indicators to track the given Rocket object.
	 * @param rocket the Rocket which the indicators should track
	 */
	public void calibrateElements(Rocket rocket) {
		getAltitudeIndicator().setRocket(rocket);
		getFuelIndicator().setRocket(rocket);
		verticalVelocityIndicator.setRocket(rocket);
		horizontalVelocityIndicator.setRocket(rocket);
	}

	/**
	 * Resets the states of various elements who have variables that change as
	 * the simulation is carried out.
	 */
	public void reset() {

		getTogglePlayButton().setState("PAUSE");
		getTimeIndicator().setInternalTime(0);
		getTimeIndicator().setPaused(false);
		getTimeIndicator().setForcePaused(false);

	}
	
	/**
	 * Gets the width of the UserInterface when it is minimized.
	 * @return the minimized width
	 */
	public double getMinWidth() {
		return minWidth;
	}

	/**
	 * Sets the width of the UserInterface when it is minimized.
	 * @param minWidth the new minimized width
	 */
	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
	}

	/**
	 * Gets the width of the UserInterface when it is maximized.
	 * @return the maximized width
	 */
	public double getMaxWidth() {
		return maxWidth;
	}

	/**
	 * Sets the width of the UserInterface when it is maximized.
	 * @param maxWidth the new maximized width
	 */
	public void setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
	}

	/**
	 * Gets the speed at which the UserInterface transitions between being
	 * minimized and maximized.
	 * @return the UserInterface's transition speed
	 */
	public double getTransitionSpeed() {
		return transitionSpeed;
	}

	/**
	 * Sets the speed at which the UserInterface transitions between being
	 * minimized and maximized.
	 * @param transitionSpeed the UserInterface's transition speed
	 */
	public void setTransitionSpeed(double transitionSpeed) {
		this.transitionSpeed = transitionSpeed;
	}

	/**
	 * Gets the UserInterface's AltitudeIndicator.
	 * @return the UserInterface's AltitudeIndicator
	 */
	public AltitudeIndicator getAltitudeIndicator() {
		return this.altitudeIndicator;
	}

	/**
	 * Gets the UserInterface's AltitudeIndicator.
	 * @param altitudeIndicator the UserInterface's new AltitudeIndicator
	 */
	public void setAltitudeIndicator(AltitudeIndicator altitudeIndicator) {
		this.altitudeIndicator = altitudeIndicator;
	}

	/**
	 * Gets the UserInterface's FuelIndicator.
	 * @return the UserInterface's FuelIndicator
	 */
	public FuelIndicator getFuelIndicator() {
		return this.fuelIndicator;
	}
	
	/**
	 * Sets the UserInterface's FuelIndicator.
	 * @param fuelIndicator the UserInterface's new FuelIndicator
	 */
	public void setFuelIndicator(FuelIndicator fuelIndicator) {
		this.fuelIndicator = fuelIndicator;
	}

	/**
	 * Gets the UserInterface's TimeIndicator.
	 * @return the UserInterface's TimeIndicator
	 */
	public TimeIndicator getTimeIndicator() {
		return this.timeIndicator;
	}

	/**
	 * Sets the UserInterface's TimeIndicator.
	 * @param timeIndicator the UserInterface's new TimeIndicator
	 */
	public void setTimeIndicator(TimeIndicator timeIndicator) {
		this.timeIndicator = timeIndicator;
	}

	/**
	 * Gets the UserInterface's TogglePlayButton.
	 * @return the UserInterface's TogglePlayButton
	 */
	public TogglePlayButton getTogglePlayButton() {
		return this.togglePlayButton;
	}

	/**
	 * Sets the UserInterface's TogglePlayButton.
	 * @param togglePlayButton the UserInterface's new TogglePlayButton
	 */
	public void setTogglePlayButton(TogglePlayButton togglePlayButton) {
		this.togglePlayButton = togglePlayButton;
	}

	/**
	 * Gets the UserInterface's MinimizeMaximizeButton.
	 * @return the UserInterface's MinimizeMaximizeButton
	 */
	public MinimizeMaximizeButton getMinimizeMaximizeButton() {
		return this.minimizeMaximizeButton;
	}

	/**
	 * Sets the UserInterface's MinimizeMaximizeButton.
	 * @param minimizeMaximizeButton the UserInterface's new 
	 * MinimizeMaximizeButton
	 */
	public void setMinimizeMaximizeButton(MinimizeMaximizeButton minimizeMaximizeButton) {
		this.minimizeMaximizeButton = minimizeMaximizeButton;
	}

	/**
	 * Gets the UserInterface's VerticalVelocityIndicator.
	 * @return the UserInterface's VerticalVelocityIndicator
	 */
	public VerticalVelocityIndicator getVerticalVelocityIndicator() {
		return this.verticalVelocityIndicator;
	}

	/**
	 * Sets the UserInterface's VerticalVelocityIndicator.
	 * @param verticalVelocityIndicator the UserInterface's new 
	 * VerticalVelocityIndicator
	 */
	public void setVerticalVelocityIndicator(VerticalVelocityIndicator verticalVelocityIndicator) {
		this.verticalVelocityIndicator = verticalVelocityIndicator;
	}

	/**
	 * Gets the UserInterface's HorizontalVelocityIndicator.
	 * @return the UserInterface's HorizontalVelocityIndicator
	 */
	public HorizontalVelocityIndicator getHorizontalVelocityIndicator() {
		return this.horizontalVelocityIndicator;
	}

	/**
	 * Sets the UserInterface's HorizontalVelocityIndicator.
	 * @param horizontalVelocityIndicator the UserInterface's new 
	 * HorizontalVelocityIndicator
	 */
	public void setHorizontalVelocityIndicator(HorizontalVelocityIndicator horizontalVelocityIndicator) {
		this.horizontalVelocityIndicator = horizontalVelocityIndicator;
	}

	/**
	 * Gets all the CustomButtons associated with the UserInterface.
	 * @return the UserInterface's buttons
	 */
	public ArrayList<CustomButton> getButtons() {
		return buttons;
	}

	/**
	 * Sets the list of CustomButtons associated with the UserInterface.
	 * @param buttons the UserInterface's new buttons
	 */
	public void setButtons(ArrayList<CustomButton> buttons) {
		this.buttons = buttons;
	}

	/**
	 * Gets the list of all the UserInterface elements.
	 * @return the interface elements
	 */
	public ArrayList<Entity> getInterfaceElements() {
		return interfaceElements;
	}

	/**
	 * Sets the list of all the UserInterface elements.
	 * @param interfaceElements the interface elements
	 */
	public void setInterfaceElements(ArrayList<Entity> interfaceElements) {
		this.interfaceElements = interfaceElements;
	}

	/**
	 * Gets the maximized state of the UserInterface.
	 * @return the UserInterface's maximized state
	 */
	public boolean isMaximized() {
		return maximized;
	}

	/**
	 * Sets the maximized state of the UserInterface.
	 * @param maximized the UserInterface's maximized state
	 */
	public void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}

	/**
	 * Gets the vertical spacing between each element in the UserInterface's
	 * sidebar.
	 * @return the vertical spacing between each element
	 */
	public double getElementVerticalSpacing() {
		return this.elementVerticalSpacing;
	}

	/**
	 * Sets the vertical spacing between each element in the UserInterface's
	 * sidebar.
	 * @param elementVerticalSpacing the new vertical spacing between each 
	 * element
	 */
	public void setElementVerticalSpacing(double elementVerticalSpacing) {
		this.elementVerticalSpacing = elementVerticalSpacing;
	}

	@Override
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
