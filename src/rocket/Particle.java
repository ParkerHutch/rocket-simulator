package rocket;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import util.Entity;

public class Particle extends Entity {

	private double radius;
	
	private double initialLifetime = 5;
	private double lifetime = initialLifetime; // how long the Particle should exist(seconds)
	
	private double groundY;
	
	public Particle() {}

	/**
	 * Creates a particle with middle x coordinate x and top y coordinate y, a
	 * specified Color, the default width and height, and a value for the 
	 * World's ground y coordinate given.
	 * @param x the middle x coordinate of the Particle
	 * @param y the top y coordinate of the particle
	 * @param color
	 * @param groundY
	 */
	public Particle(double x, double y, Color color, double groundY) {
		super(x, y, color);
		this.groundY = groundY;
	}
	
	/**
	 * Spawns the particle with middle x coordinate x and top y coordinate y
	 * and a random velocity with a direction between minAngle and maxAngle.
	 * @param x the middle x coordinate of the Particle
	 * @param y the top y coordinate of the Particle
	 * @param radius the radius of the Particle
	 * @param color the color of the Particle
	 * @param minAngle the minimum angle of the Particle's velocity vector,
	 * in degrees
	 * @param maxAngle the maximum angle of the Particle's velocity vector,
	 * in degrees
	 * @param rocketSpeed the speed of the Rocket which the Particle is being
	 * launched from
	 * @param initialSpeed the initial speed to launch the Particle at(in 
	 * addition to the rocketSpeed)
	 * @param groundY the top y coordinate of the ground
	 */
	public Particle(double x, double y, double radius, Color color, 
			double minAngle, double maxAngle, double rocketSpeed, 
			double initialSpeed, double groundY) {
		
		super(x, y, color);
		this.radius = radius;
		double angle = getRandomAngle(minAngle, maxAngle);
		getVelocity().createVector(rocketSpeed + initialSpeed, angle);
		getVelocity().setX(getVelocity().getX() * -1); // TODO Explain why I have to do this
		this.groundY = groundY;
		
	}

	/**
	 * Gets the radius of the Particle.
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Sets the radius of the Particle.
	 * @param radius the new radius
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Gets the amount of time the Particle will be alive for when created.
	 * @return the Particle's lifetime on initialization
	 */
	public double getInitialLifetime() {
		return initialLifetime;
	}

	/**
	 * Sets the amount of time the Particle will be alive for when created.
	 * @param initialLifetime the Particle's lifetime on initialization
	 */
	public void setInitialLifetime(double initialLifetime) {
		this.initialLifetime = initialLifetime;
	}

	/**
	 * Gets the amount of lifetime the Particle has left. When this value is 0,
	 * the Particle will no longer be considered 'alive'.
	 * @return the Particle's current lifetime left
	 */
	public double getLifetime() {
		return lifetime;
	}

	/**
	 * Sets the lifetime the Particle has left.
	 * @param lifetime the Particle's time left to live
	 */
	public void setLifetime(double lifetime) {
		this.lifetime = lifetime;
	}

	/**
	 * Decreases the Particle's lifetime by the time, in seconds, that has
	 * passed.
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public void decreaseLifetime(double timeElapsed) {

		if (getLifetime() - timeElapsed <= 0) {

			setLifetime(0);

		} else {
			
			setLifetime(getLifetime() - timeElapsed);
			
		}

	}
	
	/**
	 * Sets the Particle's opacity to the proportion of its lifetime left over
	 * its initial lifetime
	 */
	public void fade() {
		
		double newOpacity = (getLifetime() / getInitialLifetime()) * 
				getColor().getOpacity();
		
		setColor(new Color(getColor().getRed(), getColor().getGreen(), 
				getColor().getBlue(), newOpacity));
		
	}
	
	/**
	 * Return a random angle, in degrees, between the two parameters given
	 * @param minAngle the lower bound for the random angle, in degrees
	 * @param maxAngle the upper bound for the random angle, in degrees
	 * @return a random angle between minAngle and maxAngle
	 */
	public static double getRandomAngle(double minAngle, double maxAngle) {
		
		return Math.random() * (maxAngle - minAngle) + minAngle;
		
	}
	
	/**
	 * Flips the Particle's velocity y component if it hits the ground
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public void bounceOffGround(double timeElapsed) {
		
		double yDistanceToBeTraveled = getVelocity().getY() * timeElapsed;
		
		if (getY() >= groundY || getY() + yDistanceToBeTraveled  >= groundY) {
			
			setY(groundY);
			getVelocity().setY(getVelocity().getY() * -1);
			
		}

	}

	@Override
	public void draw(GraphicsContext gc) {

		gc.setFill(getColor());
		gc.fillOval(getX() - getRadius(), getY(), getRadius() * 2, getRadius() * 2);

	}

	@Override
	public void tick(double timeElapsed) {
		
		fade();
		setLifetime(getLifetime() - timeElapsed);
		bounceOffGround(timeElapsed);
		applyForces(timeElapsed);
		
	}

}
