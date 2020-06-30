import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

abstract class Entity {
	
	private double x; 
	private double y; 
	private double xOffset = 0;
	private double yOffset = 0;
	private double direction = 90;
	private Color color = Color.WHITE;
	
	private Vector2D velocity = new Vector2D();
	
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
	public Entity(double x, double y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	
	public Entity(double x, double y, Color color, double xOffset, double yOffset) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
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

	public double getxOffset() {
		return xOffset;
	}

	public void setxOffset(double xOffset) {
		this.xOffset = xOffset;
	}

	public double getyOffset() {
		return yOffset;
	}

	public void setyOffset(double yOffset) {
		this.yOffset = yOffset;
	}

	/**
	 * Gets the angle from the positive x-axis counterclockwise to the top of
	 * the Entity
	 * @return the angle between the Entity and the x-axis
	 */
	public double getDirection() {
		return direction;
	}


	/**
	 * Sets the angle from the positive x-axis counterclockwise to the top of
	 * the Entity
	 * @param direction the new angle between the Entity and the x-axis
	 */
	public void setDirection(double direction) {
		this.direction = direction;
	}


	/**
	 * Gets the Vector2D object representing the Entity's 2D velocity
	 * @return
	 */
	public Vector2D getVelocity() {
		return velocity;
	}


	/**
	 * Sets a new Vector2D object for the Entity's velocity
	 * @param velocity the Entity's new velocity vector
	 */
	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	/**
	 * Adds the x and y components of the Entity's velocity vector to its x and
	 * y position.
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public void applyVelocity(double timeElapsed) {
		
		setX(getX() + getVelocity().getX() * timeElapsed);
		setY(getY() + getVelocity().getY() * timeElapsed);
		
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

	public void alignWith(Entity target) {

		setX(target.getX() + getxOffset());
		setY(target.getY() + getyOffset());
		setVelocity(target.getVelocity());
		setDirection(target.getDirection());

	}

	/*
	public void alignWith(Entity target, double xShift, double yShift) {
		
		setX(target.getX() + xShift);
		setY(target.getY() + yShift);
		setVelocity(target.getVelocity());
		setDirection(target.getDirection());

	}*/
	
}
