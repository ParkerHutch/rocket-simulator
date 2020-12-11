package userinterface;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * An element that allows the user to minimize or maximize the UserInterface.
 * The UserInterface continuously checks the <code>maximized</code> state of
 * its MinimizeMaximizeButton to determine whether it should be minimized or 
 * maximized. 
 */
public class MinimizeMaximizeButton extends CustomButton {
	
	private boolean maximized = true;
	
	private double symbolWidth;
	private double symbolHeight;
	
	private Color symbolColor = Color.RED;
	
	/**
	 * Creates a MinimizeMaximizeButton with the given x and y offsets and
	 * dimensions.
	 * @param xOffset the MinimizeMaximizeButton's x offset
	 * @param yOffset the MinimizeMaximizeButton's y offset
	 * @param width the MinimizeMaximizeButton's width
	 * @param height the MinimizeMaximizeButton's height
	 */
	public MinimizeMaximizeButton(double xOffset, double yOffset, 
			double width, double height) {
		super(xOffset, yOffset, width, height);

		this.symbolWidth = width / 2;
		this.symbolHeight = 4 * height / 5;
	
	}
	
	/**
	 * Returns whether the MinimizeMaximizeButton has been set to the maximized
	 * state.
	 * @return the maximized state of the MinimizeMaximizeButton
	 */
	public boolean isMaximized() {
		return maximized;
	}

	/**
	 * Sets whether the MinimizeMaximizeButton has been set to the maximized
	 * state.
	 * @param maximized the new maximized state of the MinimizeMaximizeButton
	 */
	public void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}

	/**
	 * Gets the width of the minor symbol inside the MinimizeMaximizeButton.
	 * @return the symbol width
	 */
	private double getSymbolWidth() {
		return symbolWidth;
	}

	/**
	 * Sets the width of the minor symbol inside the MinimizeMaximizeButton.
	 * @param symbolWidth the new symbol width
	 */
	private void setSymbolWidth(double symbolWidth) {
		this.symbolWidth = symbolWidth;
	}

	/**
	 * Gets the height of the minor symbol inside the MinimizeMaximizeButton.
	 * @return the symbol height
	 */
	private double getSymbolHeight() {
		return symbolHeight;
	}

	/**
	 * Sets the height of the minor symbol inside the MinimizeMaximizeButton.
	 * @param symbolHeight the new symbol height
	 */
	private void setSymbolHeight(double symbolHeight) {
		this.symbolHeight = symbolHeight;
	}

	/**
	 * Gets the fill color of the minor symbol inside the 
	 * MinimizeMaximizeButton.
	 * @return the symbol color
	 */
	public Color getSymbolColor() {
		return symbolColor;
	}

	/**
	 * Sets the fill color of the minor symbol inside the 
	 * MinimizeMaximizeButton.
	 * @param symbolColor the new symbol color
	 */
	public void setSymbolColor(Color symbolColor) {
		this.symbolColor = symbolColor;
	}

	/**
	 * Draws a triangle symbol to represent whether the button's maximized
	 * state will be set to true or false on the next click.
	 * @param gc the GraphicsContext used to draw the MinimizeMaximizeButton
	 * @param pointedLeft true if the triangle should point left, false if it
	 * should point right
	 */
	public void drawTriangle(GraphicsContext gc, boolean pointedLeft) {

		double [] triangleXPoints;
		
		if (pointedLeft) {
			
			triangleXPoints = new double[] {

				getX() + getSymbolWidth() / 2, getX() + getSymbolWidth() / 2, 
				getX() - getSymbolWidth() / 2

			};
			
		} else {
			
			triangleXPoints = new double[] {

					getX() - getSymbolWidth() / 2, getX() - getSymbolWidth() / 2, 
					getX() + getSymbolWidth() / 2

			};
			
		}
		
		double[] triangleYPoints = new double[] {

				getCenterY() + getSymbolHeight() / 2, getCenterY() - getSymbolHeight() / 2, getCenterY()

		};

		gc.setFill(getSymbolColor());
		gc.fillPolygon(triangleXPoints, triangleYPoints, 3);

	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
		gc.setFill(getBaseColor());
		gc.fillRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);
		gc.setStroke(getStrokeColor());
		gc.strokeRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);
		
		if (isMaximized()) {
			
			drawTriangle(gc, true);
			
		} else {
			
			drawTriangle(gc, false);
			
		}

	}

	@Override
	void onClick() {
		
		setMaximized(!isMaximized());
		
	}

}
