package util;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A class containing general data for most elements that take part in the 
 * simulation.
 */
public abstract class Entity {
	
	private double x; 
	private double y; 
	private double width = 0;
	private double height = 0;
	private double xOffset = 0;
	private double yOffset = 0;
	private double direction = 90;
	private Color color = Color.WHITE;
	
	private Vector2D velocity = new Vector2D();
	private Vector2D acceleration = new Vector2D();
	
	private boolean visible = true;
	
	public Entity() {}
	
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
	 * Creates an Entity with arguments for position and color.
	 * @param x the middle x coordinate of the Entity
	 * @param y the top y coordinate of the Entity
	 * @param color the color of the Entity
	 */
	public Entity(double x, double y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	/**
	 * Creates an Entity with arguments for position, color, and x and y 
	 * offsets.
	 * @param x the middle x coordinate of the Entity
	 * @param y the top y coordinate of the Entity
	 * @param color the color of the Entity
	 * @param xOffset the Entity's x offset
	 * @param yOffset the Entity's y offset
	 */
	public Entity(double x, double y, Color color, double xOffset, 
					double yOffset) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	/**
	 * Creates an Entity with arguments for position, size, color, and x and y 
	 * offsets.
	 * @param x the middle x coordinate of the Entity
	 * @param y the top y coordinate of the Entity
	 * @param width the width of the Entity
	 * @param height the height of the Entity
	 * @param color the color of the Entity
	 * @param xOffset the Entity's x offset
	 * @param yOffset the Entity's y offset
	 */
	public Entity(double x, double y, double width, double height, Color color, 
					double xOffset, double yOffset) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	/**
	 * Creates an Entity with no x and y position with the given x and y 
	 * offsets and dimensions.
	 * @param xOffset the Entity's x offset
	 * @param yOffset the Entity's y offset
	 * @param width the Entity's width
	 * @param height the Entity's height
	 */
	public Entity(double xOffset, double yOffset, double width, 
					double height) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
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
	 * Gets the width of the Entity.
	 * @return the Entity's width
	 */
	public double getWidth() {
		return this.width;
	}

	/**
	 * Sets the width of the Entity.
	 * @param width the Entity's new width
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Gets the height of the Entity.
	 * @return the Entity's height
	 */
	public double getHeight() {
		return this.height;
	}

	/**
	 * Sets the height of the Entity.
	 * @param height the Entity's new height
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * Gets the Entity's x offset, used when the Entity is drawn.
	 * @return the Entity's x offset
	 */
	public double getxOffset() {
		return xOffset;
	}

	/**
	 * Sets the Entity's x offset, used when the Entity is drawn.
	 * @param xOffset the Entity's new x offset
	 */
	public void setxOffset(double xOffset) {
		this.xOffset = xOffset;
	}

	/**
	 * Gets the Entity's y offset, used when the Entity is drawn.
	 * @return the Entity's y offset
	 */
	public double getyOffset() {
		return yOffset;
	}

	/**
	 * Sets the Entity's y offset, used when the Entity is drawn.
	 * @param yOffset the Entity's new y offset
	 */
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
	 * Gets the Entity's acceleration vector.
	 * @return the Entity's acceleration
	 */
	public Vector2D getAcceleration() {
		return acceleration;
	}

	/**
	 * Sets the Entity's acceleration vector.
	 * @param acceleration the Entity's new acceleration
	 */
	public void setAcceleration(Vector2D acceleration) {
		this.acceleration = acceleration;
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
	 * Adds the x and y components of the Entity's acceleration vector to its 
	 * velocity's x and y components.
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public void applyAcceleration(double timeElapsed) {
		
		getVelocity().setX(getVelocity().getX() + 
				getAcceleration().getX() * timeElapsed);
		getVelocity().setY(getVelocity().getY() + 
				getAcceleration().getY() * timeElapsed);
		
	}
	
	/**
	 * Updates the Entity's position and velocity by adding the velocity to 
	 * the position and adding the acceleration to the velocity.
	 * @param timeElapsed the time since the last update, in seconds
	 */
	public void applyForces(double timeElapsed) {
		
		applyVelocity(timeElapsed);
		applyAcceleration(timeElapsed);
		
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

	/**
	 * Returns whether the Entity is currently being drawn on the screen.
	 * @return whether the Entity is visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets whether the Entity should be drawn on the screen.
	 * @param visible whether the Entity should be visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Aligns this Entity to another Entity by matching the other Entity's 
	 * position, adding this Entity's offsets to that position, and then 
	 * matching the other Entity's velocity and direction.
	 * @param target the Entity to align this Entity with
	 */
	public void alignWith(Entity target) {

		setX(target.getX() + getxOffset());
		setY(target.getY() + getyOffset());
		setVelocity(target.getVelocity());
		setDirection(target.getDirection());

	}

	/**
	 * Aligns the Entity with a point by setting this Entity's position to the
	 * given point with this Entity's offsets added on.
	 * @param x the x-coordinate of the point to align with
	 * @param y the y-coordinate of the point to align with
	 */
	public void alignWith(double x, double y) {

		setX(x + getxOffset());
		setY(y + getyOffset());

	}

}
