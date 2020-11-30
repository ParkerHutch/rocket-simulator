package rocket;
public class UserControlledRocket extends Rocket {

	double targetAngle = 90;
	boolean shouldFireEngines = false;
	boolean shouldFireRCS = false;
	
	public UserControlledRocket(double x, double y, double fuel, double groundY) {
		
		super(x, y, fuel, groundY);
		
	}
	
	public double getTargetAngle() {
		return targetAngle;
	}

	public void setTargetAngle(double targetAngle) {
		this.targetAngle = targetAngle;
	}

	public boolean shouldFireEngines() {
		return shouldFireEngines;
	}

	public void setShouldFireEngines(boolean shouldFireEngines) {
		this.shouldFireEngines = shouldFireEngines;
	}

	
	public boolean shouldFireRCS() {
		return shouldFireRCS;
	}

	public void setShouldFireRCS(boolean shouldFireRCS) {
		this.shouldFireRCS = shouldFireRCS;
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
