import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

abstract class Entity {
	
	private double x; 
	private double y; 
	private double width = 20; 
	private double height = 40;
	private Color color = Color.BLUE;
	
	Entity() {}
	
	
	/**
	 * Creates an Entity with arguments for x and y, and the default width, 
	 * height, and color.
	 * @param x the middle x coordinate of the Entity
	 * @param y the top y coordinate of the Entity
	 */
	public Entity(double x, double y) {
		this.x = x;
		this.y = y;
	}


	/**
	 * Creates an Entity with arguments for position, size, and color.
	 * @param x the middle x coordinate of the Entity
	 * @param y the top y coordinate of the Entity
	 * @param width the width of the Entity
	 * @param height the height of the Entity
	 * @param color the color of the Entity
	 */
	public Entity(double x, double y, double width, double height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	/**
	 * Draws the Entity on a Canvas
	 * @param gc the Canvas's GraphicsContext
	 */
	public abstract void draw(GraphicsContext gc);
	
	/**
	 * Updates the Entity
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public abstract void tick(double timeElapsed);

	/**
	 * Gets the middle x coordinate of the Entity
	 * @return the x of the Entity
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the middle x coordinate of the Entity
	 * @param x the new x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the top y coordinate of the Entity
	 * @return the y of the Entity
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the top y coordinate of the Entity
	 * @param y the new y
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Gets the width of the Entity
	 * @return the width of the Entity
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Sets the width of the Entity
	 * @param width the new width
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Gets the height of the Entity
	 * @return the height of the Entity
	 */
	public double getHeight() {
		return height;
	}

	/** Sets the height of the Entity
	 * @param height the new height
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * Gets the color of the Entity
	 * @return the color of the Entity
	 */
	public Color getColor() {
		return color;
	}

	/** Sets the color of the Entity
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	
	
}
