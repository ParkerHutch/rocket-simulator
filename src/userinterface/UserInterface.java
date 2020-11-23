package userinterface;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import rocket.Entity;
import rocket.Rocket;

public class UserInterface extends Entity {

	private double minWidth = 0;
	private double maxWidth;
	private double width;
	private double height;
	private boolean maximized = true;
	
	ArrayList<CustomButton> buttons = new ArrayList<CustomButton>();
	ArrayList<Entity> interfaceElements = new ArrayList<Entity>();
	
	//LandCrashMessage message;
	private double transitionSpeed = 2;
	
	public UserInterface(double x, double y, double width, double height, 
			Rocket rocket, double groundY, AnimationTimer animationTimer) {
		
		super(x, y);
		this.maxWidth = width * 2;
		this.width = width * 2;
		this.height = height;
		
		interfaceElements.add(new AltitudeIndicator(maxWidth / 4, 20, 30, 100, rocket, 
				rocket.getManeuverCalculator().calculateAltitude()));
		
		interfaceElements.add(new TimeIndicator(maxWidth / 4, 200, 70, 70));
		
		interfaceElements.add(new FuelIndicator(maxWidth / 4, 300, 30, 100, rocket));
		
		buttons.add(new TogglePlayButton(maxWidth / 4, 160, 30, 30, animationTimer));
		buttons.add(new MinimizeMaximizeButton((maxWidth + 20) / 2, height / 2 - 50, 20, 50));
		
		// TODO add LandCrashMessage, put its buttons in the buttons arraylist
		
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

	public double getTransitionSpeed() {
		return transitionSpeed;
	}

	public void setTransitionSpeed(double transitionSpeed) {
		this.transitionSpeed = transitionSpeed;
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
		
		//getMessage().tick(timeElapsed);

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
		
		//getMessage().draw(gc);
		
	}
	
	
}
