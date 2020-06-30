package rocket;
import java.util.Iterator;
import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ParticleEmitter extends Entity {
	
	private double width = 0; 
	private double height = 0;
	
	LinkedList<Particle> particles = new LinkedList<Particle>();
	private double particleSpawnRate = 0; // TODO Rename to secondsPerParticle
	private double particleBurstAmount = 10;
	private double particleLaunchAngle = 0;
	private double particleSpreadAngle = 20;
	private double particleRadius = 6;
	private double initialParticleSpeed = 50;
	private double timeSinceLastParticleSpawned = particleSpawnRate;
	private Color [] particleColorRange;
	private double groundY;
	
	private boolean on = false;
	
	public ParticleEmitter(double groundY, Color[] particleColorRange) {
		super();
		this.groundY = groundY;
		this.particleColorRange = particleColorRange;
	}
	
	public ParticleEmitter(double groundY, Color[] particleColorRange, 
			double xOffset, double yOffset) {
		
		super(0, 0, Color.RED, xOffset, yOffset);
		this.groundY = groundY;
		this.particleColorRange = particleColorRange;
		
	}
	
	public ParticleEmitter(double width, double height, double groundY, 
			Color [] particleColorRange, double xOffset, double yOffset, 
			double particleLaunchAngle, Color color) {
		
		super(0, 0, color, xOffset, yOffset);
		this.groundY = groundY;
		this.width = width;
		this.height = height;
		this.particleColorRange = particleColorRange;
		this.particleLaunchAngle = particleLaunchAngle;
		this.particleRadius = 1;
		this.particleSpreadAngle = 5;
		this.particleBurstAmount = 10;
		
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	public LinkedList<Particle> getParticles() {
		return particles;
	}

	public void setParticles(LinkedList<Particle> particles) {
		this.particles = particles;
	}

	public Color [] getParticleColorRange() {
		return particleColorRange;
	}

	public void setParticleColorRange(Color [] particleColorRange) {
		this.particleColorRange = particleColorRange;
	}

	public double getParticleRadius() {
		return particleRadius;
	}

	public void setParticleRadius(double particleRadius) {
		this.particleRadius = particleRadius;
	}

	public double getInitialParticleSpeed() {
		return initialParticleSpeed;
	}

	public void setInitialParticleSpeed(double initialParticleSpeed) {
		this.initialParticleSpeed = initialParticleSpeed;
	}

	public double getParticleLaunchAngle() {
		return particleLaunchAngle;
	}

	public void setParticleLaunchAngle(double particleLaunchAngle) {
		this.particleLaunchAngle = particleLaunchAngle;
	}

	public double getParticleSpawnRate() {
		return particleSpawnRate;
	}

	public void setParticleSpawnRate(double particleSpawnRate) {
		this.particleSpawnRate = particleSpawnRate;
	}

	public double getParticleBurstAmount() {
		return particleBurstAmount;
	}

	public void setParticleBurstAmount(double particleBurstAmount) {
		this.particleBurstAmount = particleBurstAmount;
	}

	public double getParticleSpreadAngle() {
		return particleSpreadAngle;
	}

	public void setParticleSpreadAngle(double particleSpreadAngle) {
		this.particleSpreadAngle = particleSpreadAngle;
	}

	public double getTimeSinceLastParticleSpawned() {
		return timeSinceLastParticleSpawned;
	}

	public void setTimeSinceLastParticleSpawned(double timeSinceLastParticleSpawned) {
		this.timeSinceLastParticleSpawned = timeSinceLastParticleSpawned;
	}
	
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
