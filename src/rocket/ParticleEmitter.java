package rocket;
import java.util.Iterator;
import java.util.LinkedList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import util.Entity;

/**
 * A class used to create a Rocket exhaust effect through the creation of
 * Particles.
 */
public class ParticleEmitter extends Entity {
	
	private double width = 0; 
	private double height = 0;
	
	LinkedList<Particle> particles = new LinkedList<Particle>();
	private double particleSpawnRate = 0; 
	private double particleBurstAmount = 10;
	private double particleLaunchAngle = 0;
	private double particleSpreadAngle = 20;
	private double particleRadius = 6;
	private double initialParticleSpeed = 50;
	private double timeSinceLastParticleSpawned = particleSpawnRate;
	private Color [] particleColorRange;
	private double groundY;
	
	private boolean on = false;
	
	/**
	 * Creates a ParticleEmitter with the given ground y-coordinate and 
	 * particle color range.
	 * @param groundY the ground y-coordinate
	 * @param particleColorRange a two-dimensional array of Colors representing
	 * the minimum and maximum RGB values particles can take on, respectively
	 */
	public ParticleEmitter(double groundY, Color[] particleColorRange) {
		super();
		this.groundY = groundY;
		this.particleColorRange = particleColorRange;
	}
	
	/**
	 * Creates a ParticleEmitter with the given ground y-coordinate, particle 
	 * color range, and x and y offsets.
	 * @param groundY the ground y-coordinate
	 * @param particleColorRange a two-dimensional array of Colors representing
	 * the minimum and maximum RGB values particles can take on, respectively
	 * @param xOffset the ParticleEmitter's xOffset, used in drawing
	 * @param yOffset the ParticleEmitter's yOffset, used in drawing
	 */
	public ParticleEmitter(double groundY, Color[] particleColorRange, 
			double xOffset, double yOffset) {
		
		super(0, 0, Color.RED, xOffset, yOffset);
		this.groundY = groundY;
		this.particleColorRange = particleColorRange;
		
	}
	/**
	 * Creates a ParticleEmitter with the given dimensions, ground y-coordinate, 
	 * particle color range, x and y offsets, particle launch angle, and color.
	 * This constructor is useful if the ParticleEmitter will be visible.
	 * @param width the width of the ParticleEmitter
	 * @param height
	 * @param groundY the ground y-coordinate
	 * @param particleColorRange a two-dimensional array of Colors representing
	 * the minimum and maximum RGB values particles can take on, respectively
	 * @param xOffset the ParticleEmitter's xOffset, used in drawing
	 * @param yOffset the ParticleEmitter's yOffset, used in drawing
	 * @param particleLaunchAngle the direction Particles should be emitted at,
	 * in degrees
	 * @param color the ParticleEmitter's color
	 */
	public ParticleEmitter(double width, double height, double groundY, 
			Color [] particleColorRange, double xOffset, double yOffset, 
			double particleLaunchAngle, Color color) {
		
		super(0, 0, color, xOffset, yOffset);
		this.groundY = groundY;
		setWidth(width);
		setHeight(height);
		this.particleColorRange = particleColorRange;
		this.particleLaunchAngle = particleLaunchAngle;
		this.particleRadius = 1;
		this.particleSpreadAngle = 5;
		this.particleBurstAmount = 10;
		
	}
	
	/**
	 * Returns whether the ParticleEmitter is currently emitting Particles.
	 * @return the ParticleEmitter's 'on' status
	 */
	public boolean isOn() {
		return on;
	}

	/**
	 * Sets the status of whether the ParticleEmitter is currently emitting
	 * Particles.
	 * @param on the ParticleEmitter's 'on' status
	 */
	public void setOn(boolean on) {
		this.on = on;
	}

	/**
	 * Gets a LinkedList of this ParticleEmitter's emitted Particles.
	 * @return a list of emitted Particles.
	 */
	public LinkedList<Particle> getParticles() {
		return particles;
	}

	/**
	 * Sets the list of Particles emitted by this ParticleEmitter.
	 * @param particles the list of Particles emitted
	 */
	public void setParticles(LinkedList<Particle> particles) {
		this.particles = particles;
	}

	/**
	 * Gets the range of Colors that Particles can take on when emitted.
	 * @return the Particle color range
	 */
	public Color [] getParticleColorRange() {
		return particleColorRange;
	}

	/**
	 * Sets the range of Colors that Particles can take on when emitted.
	 * @param particleColorRange the new Particle color range
	 */
	public void setParticleColorRange(Color [] particleColorRange) {
		this.particleColorRange = particleColorRange;
	}

	/**
	 * Gets the radius used for new Particles.
	 * @return the Particle radius
	 */
	public double getParticleRadius() {
		return particleRadius;
	}

	/**
	 * Sets the radius used for new Particles.
	 * @param particleRadius the Particle radius
	 */
	public void setParticleRadius(double particleRadius) {
		this.particleRadius = particleRadius;
	}

	/**
	 * Gets the speed Particles have when emitted.
	 * @return the initial Particle speed
	 */
	public double getInitialParticleSpeed() {
		return initialParticleSpeed;
	}

	/**
	 * Sets the speed Particles have when emitted.
	 * @param initialParticleSpeed the initial Particle speed
	 */
	public void setInitialParticleSpeed(double initialParticleSpeed) {
		this.initialParticleSpeed = initialParticleSpeed;
	}

	/**
	 * Gets the direction that Particles should be emitted at, in degrees.
	 * @return the particle launch angle
	 */
	public double getParticleLaunchAngle() {
		return particleLaunchAngle;
	}

	/**
	 * Sets the direction that Particles should be emitted at, in degrees.
	 * @return the particle launch angle
	 */
	public void setParticleLaunchAngle(double particleLaunchAngle) {
		this.particleLaunchAngle = particleLaunchAngle;
	}

	/**
	 * Gets the amount of time, in seconds, that should elapse between 
	 * Particle burst spawns
	 * @return the particle spawn rate
	 */
	public double getParticleSpawnRate() {
		return particleSpawnRate;
	}

	/**
	 * Gets the amount of time, in seconds, that should elapse between 
	 * Particle burst spawns
	 * @param particleSpawnRate the particle spawn rate
	 */
	public void setParticleSpawnRate(double particleSpawnRate) {
		this.particleSpawnRate = particleSpawnRate;
	}

	/**
	 * Sets the number of Particles that should be emitted in every burst
	 * @return the number of Particles per burst
	 */
	public double getParticleBurstAmount() {
		return particleBurstAmount;
	}

	/**
	 * Sets the number of Particles that should be emitted in every burst
	 * @param particleBurstAmount the number of Particles per burst
	 */
	public void setParticleBurstAmount(double particleBurstAmount) {
		this.particleBurstAmount = particleBurstAmount;
	}

	/**
	 * Gets the maximum deviation, in degrees, of launch angle from the 
	 * <code>particleLaunchAngle</code> Particles can have when emitted.
	 * @return the particle angle spread
	 */
	public double getParticleSpreadAngle() {
		return particleSpreadAngle;
	}

	/**
	 * Sets the maximum deviation, in degrees, of launch angle from the 
	 * <code>particleLaunchAngle</code> Particles can have when emitted.
	 * @param particleSpreadAngle the particle angle spread
	 */
	public void setParticleSpreadAngle(double particleSpreadAngle) {
		this.particleSpreadAngle = particleSpreadAngle;
	}

	/**
	 * Gets the amount of time, in seconds, since the last burst of Particles
	 * was emitted.
	 * @return the time since the last Particle burst
	 */
	public double getTimeSinceLastParticleSpawned() {
		return timeSinceLastParticleSpawned;
	}

	/**
	 * Sets the amount of time, in seconds, since the last burst of Particles
	 * was emitted.
	 * @param timeSinceLastParticleSpawned the time since the last Particle 
	 * burst
	 */
	public void setTimeSinceLastParticleSpawned(double timeSinceLastParticleSpawned) {
		this.timeSinceLastParticleSpawned = timeSinceLastParticleSpawned;
	}
	
	/**
	 * Gets a random Color within the range of the 
	 * <code>particleColorRange</code>. If the length of 
	 * <code>particleColorRange</code> is less than 2, its first element is
	 * returned.
	 * @return a random Color within the <code>particleColorRange</code>
	 */
	private Color generateRandomParticleColor() {
		
		if (getParticleColorRange().length == 1 || 
				getParticleColorRange()[0].equals(
						getParticleColorRange()[1])) {
			
			return getParticleColorRange()[0];
			
		} else {
			
			Color color1 = getParticleColorRange()[0];
			Color color2 = getParticleColorRange()[1];
			
			double lowerR = Math.min(color1.getRed(), color2.getRed());
			double upperR = Math.max(color1.getRed(), color2.getRed());
			
			double lowerG = Math.min(color1.getGreen(), color2.getGreen());
			double upperG = Math.max(color1.getGreen(), color2.getGreen());
			
			double lowerB = Math.min(color1.getBlue(), color2.getBlue());
			double upperB = Math.max(color1.getBlue(), color2.getBlue());
			
			double particleR = lowerR + (Math.random() * (upperR - lowerR));
			double particleG = lowerG + (Math.random() * (upperG - lowerG));
			double particleB = lowerB + (Math.random() * (upperB - lowerB));
			
			return new Color(particleR, particleG, particleB, 1);
			
		}
		
	}
	
	/**
	 * Emits a single Particle by creating one and adding it to the
	 * ParticleEmitter's list of Particles.
	 */
	private void emitParticle() {
		
		double minAngle = getDirection() - getParticleSpreadAngle() + 
				getParticleLaunchAngle();
		double maxAngle = getDirection() + getParticleSpreadAngle() + 
				getParticleLaunchAngle();
		
		getParticles().add(new Particle(
				getX(), getY() + getHeight() / 2, getParticleRadius(), 
				generateRandomParticleColor(), minAngle, maxAngle, 
				getVelocity().getMagnitude(), getInitialParticleSpeed(),
				groundY
				));
		
	}
	
	/**
	 * Spawns particles to simulate an engine plume if enough time since the
	 * last particle spawning has passed
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	private void emitParticles(double timeElapsed) {
		
		if (getTimeSinceLastParticleSpawned() > getParticleSpawnRate()) {
			
			for (int i = 0; i < getParticleBurstAmount(); i++) {
				emitParticle();
			}

			setTimeSinceLastParticleSpawned(0);
			
			
		} else {
			
			setTimeSinceLastParticleSpawned(
					getTimeSinceLastParticleSpawned() + timeElapsed);
			
		}
		
	}
	
	/**
	 * Removes the dead(lifetime = 0) Particles from the Rocket's ArrayList of
	 * particles for the engine plume
	 */
	private void cleanParticles() {
		
		Iterator<Particle> particleIterator = 
				getParticles().iterator();
		
		while (particleIterator.hasNext()) {

			if (particleIterator.next().getLifetime() <= 0) {

				particleIterator.remove();

			}

		}
		
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
		gc.setFill(getColor());
		gc.fillRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight());
		
		for (Particle particle : getParticles()) {
			
			particle.draw(gc);
			
		}
		
	}
	
	@Override
	public void tick(double timeElapsed) {
		
		if (isOn()) {
			emitParticles(timeElapsed);
		}
		
		for (Particle particle : getParticles()) {
			
			particle.tick(timeElapsed);
			
		}
		
		cleanParticles();
		
	}
	
}
