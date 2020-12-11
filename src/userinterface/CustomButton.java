package userinterface;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import util.Entity;

/**
 * A class containing data and functions which are useful for creating custom
 * interface elements that utilize clickable interactions.
 */
public abstract class CustomButton extends Button {

	private double x = 0; // center x
	private double y = 0; // center y
	private double xOffset = 0;
	private double yOffset = 0;
	
	private Color baseColor = Color.WHITE;
	private Color strokeColor = Color.BLACK;
	
	/**
	 * Creates a CustomButton with the given x and y offsets and the given
	 * dimensions.
	 * @param xOffset the CustomButton's x offset
	 * @param yOffset the CustomButton's y offset
	 * @param width the width of the CustomButton
	 * @param height the height of the CustomButton
	 */
	public CustomButton(double xOffset, double yOffset, double width, double height) {
		
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		setPrefSize(width, height);
		
		this.setOnMouseClicked(event -> onClick());
		this.setOpacity(0); // hides the node while allowing inputs to register
		
	}

	/**
	 * Creates a CustomButton with given x and y offsets, dimensions, and 
	 * colors.
	 * @param xOffset the CustomButton's x offset
	 * @param yOffset the CustomButton's y offset
	 * @param width the CustomButton's width
	 * @param height the CustomButton's height
	 * @param baseColor the CustomButton's base color
	 * @param strokeColor the CustomButton's stroke color
	 */
	public CustomButton(double xOffset, double yOffset, double width, double height, 
			Color baseColor, Color strokeColor) {

		this.xOffset = xOffset;
		this.yOffset = yOffset;
		setWidth(width);
		setHeight(height);
		this.baseColor = baseColor;
		this.strokeColor = strokeColor;
		this.setOnMouseClicked(event -> onClick());
		this.setOpacity(0); // hides the node while allowing inputs to register

	}
	
	/**
	 * Gets the CustomButton's center x-coordinate.
	 * @return the center x-coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the CustomButton's center x-coordinate.
	 * @param x the new center x-coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the CustomButton's top y-coordinate,
	 * @return the top y-coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the CustomButton's top y-coordinate.
	 * @return the top y-coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Gets the CustomButton's center y-coordinate.
	 * @return the center y-coordinate
	 */
	public double getCenterY() {
		
		return getY() + getHeight() / 2;
		
	}

	/**
	 * Gets the CustomButton's x offset.
	 * @return the x offset
	 */
	public double getxOffset() {
		return xOffset;
	}

	/**
	 * Sets the CustomButton's x offset.
	 * @param xOffset the new x offset
	 */
	public void setxOffset(double xOffset) {
		this.xOffset = xOffset;
	}

	/**
	 * Gets the CustomButton's y offset.
	 * @return the y offset
	 */
	public double getyOffset() {
		return yOffset;
	}

	/**
	 * Sets the CustomButton's y offset.
	 * @param yOffset the new y offset
	 */
	public void setyOffset(double yOffset) {
		this.yOffset = yOffset;
	}

	/**
	 * Gets the CustomButton's base color (usually the fill color).
	 * @return the base color
	 */
	public Color getBaseColor() {
		return baseColor;
	}

	/**
	 * Sets the CustomButton's base color (usually the fill color).
	 * @param baseColor the new base color
	 */
	public void setBaseColor(Color baseColor) {
		this.baseColor = baseColor;
	}

	/**
	 * Gets the CustomButton's stroke color.
	 * @return the stroke color
	 */
	public Color getStrokeColor() {
		return strokeColor;
	}

	/**
	 * Sets the CustomButton's stroke color.
	 * @param strokeColor the new stroke color
	 */
	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}
	
	/**
	 * Updates the layout x and y fields of the CustomButton using the 
	 * CustomButton's x offset, width, and y offset values, and sets the x and
	 * y coordinates to be the given x and y coordinates with the respective
	 * offset values added on.
	 * @param x the x value to align the CustomButton with
	 * @param y the y value to align the CustomButton with
	 */
	public void alignWith(double x, double y) {
		
		setLayoutX(getxOffset() - getWidth() / 2);
		setLayoutY(getyOffset());
		
		setX(x + getxOffset());
		setY(y + getyOffset());
		
	}
	
	/**
	 * Aligns the CustomButton on a given Entity by extracting its x and y
	 * coordinates.
	 * @param target the Entity to align the CustomButton with
	 */
	public void alignWith(Entity target) {

		alignWith(target.getX(), target.getY());

	}

	/**
	 * Defines what the CustomButton should do when clicked.
	 */
	abstract void onClick();
	
	/**
	 * Defines how the CustomButton should be drawn.
	 * @param gc the GraphicsContext used to draw the CustomButton
	 */
	abstract void draw(GraphicsContext gc);
	
}
