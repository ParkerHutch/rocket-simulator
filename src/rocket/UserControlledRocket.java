package rocket;

/**
 * A class used to add user interaction functionality to the Rocket class.
 */
public class UserControlledRocket extends Rocket {

	double targetAngle = 90;
	boolean shouldFireEngines = false;
	boolean shouldFireRCS = false;
	
	/**
	 * Creates a UserControlledRocket at (x, y) with the given fuel and ground
	 * y-coordinate.
	 * @param x the UserControlledRocket's center x-coordinate
	 * @param y the UserControlledRocket's center y-coordinate
	 * @param fuel the UserControlledRocket's fuel amount
	 * @param groundY the ground's top y-coordinate
	 */
	public UserControlledRocket(double x, double y, double fuel, double groundY) {
		
		super(x, y, fuel, groundY);
		
	}
	
	/**
	 * Gets the angle towards which the UserControlledRocket should be rotating.
	 * @return the target angle
	 */
	public double getTargetAngle() {
		return targetAngle;
	}

	/**
	 * Sets the angle towards which the UserControlledRocket should be rotating.
	 * @param targetAngle the target angle
	 */
	public void setTargetAngle(double targetAngle) {
		this.targetAngle = targetAngle;
	}

	/**
	 * Returns whether the UserControlledRocket's engines should currently be
	 * firing.
	 * @return whether the UserControlledRocket's engines should be on.
	 */
	public boolean shouldFireEngines() {
		return shouldFireEngines;
	}

	/**
	 * Sets whether the UserControlledRocket's should currently be firing.
	 * @param shouldFireEngines whether the UserControlledRocket's engines 
	 * should be on.
	 */
	public void setShouldFireEngines(boolean shouldFireEngines) {
		this.shouldFireEngines = shouldFireEngines;
	}

	/**
	 * Returns whether the UserControlledRocket's RCS thrusters should
	 * currently be firing.
	 * @return whether the UserControlledRocket's RCS thrusters should be
	 * firing
	 */
	public boolean shouldFireRCS() {
		return shouldFireRCS;
	}

	/**
	 * Sets whether the UserControlledRocket's RCS thrusters should
	 * currently be firing.
	 * @param shouldFireRCS whether the UserControlledRocket's RCS thrusters 
	 * should be firing
	 */
	public void setShouldFireRCS(boolean shouldFireRCS) {
		this.shouldFireRCS = shouldFireRCS;
	}

	/**
	 * Resets various position, flight, and visual variables so that the 
	 * UserControlledRocket is ready to begin another simulation.
	 * @param x the new x-coordinate of the UserControlledRocket
	 * @param y the new y-coordinate of the UserControlledRocket
	 * @param fuel the new fuel amount of the UserControlledRocket
	 */
	public void reset(double x, double y, double fuel) {

		setDirection(90);
		setTargetAngle(90);
		getRCSThrusters()[0].setOn(false);
		getRCSThrusters()[1].setOn(false);
		setX(x);
		setY(y);
		getVelocity().setX(0);
		getVelocity().setY(0);
		setFuel(fuel);
		setAirborne(true);

	}
	@Override
	protected void pointInDirection(double targetAngle, double timeElapsed) {
		
		double distanceToTargetAngle = targetAngle - getDirection();
		
		if (distanceToTargetAngle < 0) {
			
			getRCSThrusters()[0].setOn(true);
			getRCSThrusters()[1].setOn(false);
			
		} else if (distanceToTargetAngle != 0){
			
			getRCSThrusters()[0].setOn(false);
			getRCSThrusters()[1].setOn(true);
			
		}
		
		if (Math.abs(distanceToTargetAngle) >= getTurnRate() * timeElapsed) {
			
			if (distanceToTargetAngle < 0) {

				setDirection(getDirection() - getTurnRate() * timeElapsed);

			} else {

				setDirection(getDirection() + getTurnRate() * timeElapsed);

			}
			
		}
		
	}
	
	@Override
	public void tick(double timeElapsed) {

		if (isAirborne()) {

			setEnginesOn(shouldFireEngines());
			pointInDirection(getTargetAngle(), timeElapsed);

			applyThrust(timeElapsed);
			applyForces(timeElapsed); // should be last

		} 
		
		for (RocketEngine engine : getEngines()) {
			
			engine.tick(timeElapsed);
			
		}
		
		for (ParticleEmitter rcsThruster : getRCSThrusters()) {
			
			rcsThruster.tick(timeElapsed);
			if (!shouldFireRCS) {
				
				rcsThruster.setOn(false);
				
			}
			
		}
		
	}
	
	
	
}
