
/**
 * @author Parker Hutchinson
 * Start Date: September 13, 2019
 */

public class Rocket {
	
	// Dimensions
	private double x;
	private double y;
	private double width; // square, so length will be the same
	
	
	// Physics variables
	private double direction; // the angle between the ground and the top of the rocket
	private Vector2D velocity;
	
	// Flight attributes
	private double fuel;
	
	Rocket() {}
	
	/**
	 * @param x The middle x coordinate of the rocket
	 * @param y	The y coordinate of the top of the rocket
	 */
	Rocket(double x, double y) {
		
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	public double getFuel() {
		return fuel;
	}
	

	public void setFuel(double fuel) {
		this.fuel = fuel;
	}
	

	/**
	 * Fires the rocket's engine, propelling it in the direction it is facing.
	 * In a hoverslam, the rocket should probably only be firing at 100% power.
	 * @param percent the proportion of engine capacity to use (out of 100)
	 */
	public void fireEngine(double percent) {
		
		// TODO
		// NOTE: Maybe store an engine throttle variable that is either 1 or 0?
		// and when this is called just set it to 1 in a hoverslam case
		
	}
	
	/**
	 * Uses kinematic equations to determine the time(in seconds since program
	 * start) when the rocket will make contact with the ground. 
	 * @param currentTime the current time, in seconds since program start
	 * @return the time when the rocket will hit the ground, in seconds since 
	 * program start
	 */
	public double calculateImpactTime(double currentTime) {
		
		return 0.0;
		
	}
	
	public void tick() {
		// Apply gravity (World.GRAVITY?)
		// Apply user input (Or do this in runner file/ use one of the 
		// keyboardinput classes i made)
		
		
		
	}
	
	public void draw() {
		
		// Draw the rocket on the screen
		// NOTE: Or should I use objects like Rectangle, etc. that are added
		// to the root group?
		
	}
	
}
