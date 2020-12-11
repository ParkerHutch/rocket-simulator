package rocket;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import util.Entity;

/**
 * A class used to store data about a Rocket's engines, which generate thrust
 * in the direction of the Rocket and create particle effects through their
 * ParticleEmitters.
 */
public class RocketEngine extends Entity {
	
	private double width = 15;
	private double height = 10;
	private boolean on;
	private double fuelBurnRate = 1; // units of fuel per second
	private double thrustPower = 200; // pixels per second
	
	private ParticleEmitter emitter;
	
	/**
	 * Creates a gray RocketEngine of width 15 and height 10 that emits 
	 * Particles with colors between red and orange. The ground y-coordinate is
	 * also passed to the RocketEngine's ParticleEmitter.
	 * @param groundY the ground's top y-coordinate
	 */
	public RocketEngine(double groundY) {
		
		super();
		setColor(Color.GRAY);
		this.emitter = new ParticleEmitter(groundY, new Color[] {Color.RED, Color.ORANGE}, 
				0, getHeight());
	
	}
	
	/**
	 * Creates a gray RocketEngine of specified width and height at a position 
	 * given by xOffset and yOffset that emits Particles with colors between 
	 * red and orange. The ground y-coordinate is also passed to the 
	 * RocketEngine's ParticleEmitter.
	 * @param groundY the ground y-coordinate
	 * @param width the width of the RocketEngine
	 * @param height the height of the RocketEngine
	 * @param xOffset the RocketEngine's x offset
	 * @param yOffset the RocketEngine's y offset
	 */
	public RocketEngine(double groundY, double width, double height, 
			double xOffset, double yOffset) {
		
		super();
		setColor(Color.GRAY);
		this.emitter = new ParticleEmitter(groundY, new Color[] {Color.RED, Color.ORANGE}, 
				0, getHeight());
		this.width = width;
		this.height = height;
		setxOffset(xOffset);
		setyOffset(yOffset);
		
	}
	
	/**
	 * Gets the width of the RocketEngine
	 * @return the width of the RocketEngine
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Sets the width of the RocketEngine
	 * @param width the new width
	 */
	
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Gets the height of the RocketEngine
	 * @return the height of the RocketEngine
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Sets the height of the RocketEngine
	 * @param height the new height
	 */
	public void setHeight(double height) {
		this.height = height;
	}
	
	/**
	 * Gets the fuel consumption rate of the RocketEngine, in units per second
	 * @return the fuel consumption rate (fuel units/s)
	 */
	public double getFuelBurnRate() {
		return fuelBurnRate;
	}

	/**
	 * Sets the fuel consumption rate of the RocketEngine, in units per second
	 * @param fuelBurnRate the new fuel consumption rate (fuel units/s)
	 */
	public void setFuelBurnRate(double fuelBurnRate) {
		this.fuelBurnRate = fuelBurnRate;
	}

	/**
	 * Returns whether the engine is currently engaged and firing.
	 * @return the engine's 'on' status
	 */
	public boolean isOn() {
		return on;
	}

	/**
	 * Sets whether the engine is currently engaged and firing.
	 * @param on the engine's 'on' status
	 */
	public void setOn(boolean on) {
		this.on = on;
	}

	/**
	 * Gets the amount of thrust the engine generates when on, in pixels per
	 * second.
	 * @return the engine's thrust power
	 */
	public double getThrustPower() {
		return thrustPower;
	}

	/**
	 * Sets the amount of thrust the engine generates when on, in pixels per
	 * second.
	 * @param thrustPower the engine's thrust power
	 */
	public void setThrustPower(double thrustPower) {
		this.thrustPower = thrustPower;
	}
	
	/**
	 * Gets the ParticleEmitter used to display the RocketEngine's exhaust.
	 * @return the RocketEngine's ParticleEmitter
	 */
	public ParticleEmitter getEmitter() {
		return emitter;
	}

	/**
	 * Sets the ParticleEmitter used to display the RocketEngine's exhaust.
	 * @param emitter the RocketEngine's new ParticleEmitter
	 */
	public void setEmitter(ParticleEmitter emitter) {
		this.emitter = emitter;
	}

	@Override
	public void draw(GraphicsContext gc) {
		
		emitter.draw(gc);
		
		gc.setFill(getColor());
		gc.fillArc(getX() - getWidth() / 2, getY(), getWidth(), getHeight() * 2, 
				0, 180, ArcType.ROUND);
		
	}
	
	@Override
	public void tick(double timeElapsed) {
		
		getEmitter().alignWith(this);
		getEmitter().setOn(this.isOn());
		getEmitter().tick(timeElapsed);
		
	}
	
}
