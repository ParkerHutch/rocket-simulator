package rocket;
import world.World;

public class ManeuverCalculator {
	
	// TODO add a deltav calculation method?
	
	private Rocket rocket;
	private double groundY;
	
	public ManeuverCalculator() {}
	
	/**
	 * Creates a ManeuverCalculator for a given Rocket and stores a given 
	 * y-coordinate as the ground's y-coordinate
	 * @param rocket the Rocket to calculate maneuvers for
	 * @param groundY the y-coordinate of the ground, measured in pixels from
	 * the top of the screen
	 */
	public ManeuverCalculator(Rocket rocket, double groundY) {
		
		this.rocket = rocket;
		this.groundY = groundY;
		
	}

	/**
	 * Gets the Rocket which the ManeuverCalculator is calculating maneuvers
	 * for
	 * @return the ManeuverCalculator's Rocket
	 */
	public Rocket getRocket() {
		return rocket;
	}

	/**
	 * Sets the Rocket which the ManeuverCalculator is calculating maneuvers
	 * for
	 * @param rocket the new Rocket for the ManeuverCalculator
	 */
	public void setRocket(Rocket rocket) {
		this.rocket = rocket;
	}
	
	/**
	 * Gets the top y coordinate of the ground
	 * @return the ground's top y coordinate
	 */
	public double getGroundY() {
		return groundY;
	}
	
	/**
	 * Sets the top y coordinate of the ground
	 * @param groundY the new ground y coordinate
	 */
	public void setGroundY(double groundY) {
		this.groundY = groundY;
	}
	
	/**
	 * Returns true if the Rocket should fire its engines to land at the
	 * specified height with 0 velocity
	 * @param targetHeight the height the Rocket wishes to achieve 0 velocity
	 * at
	 * @return true if the Rocket should fire its engines, false otherwise
	 */
	public boolean shouldBurn(double targetHeight) {
		
		boolean crossedBurnHeight = calculateBurnHeight(targetHeight) >= calculateAltitude();
		double yVelocityThreshold = 10;
		if (crossedBurnHeight && 
				!(getRocket().getVelocity().getY() <= yVelocityThreshold)) {
			return true;
			
			
		} else {
			
			return false;
			
		}
		
	}
	
	/**
	 * Calculates the distance between the bottom of the Rocket and the ground.
	 * @return the Rocket's altitude
	 */
	public double calculateAltitude() {
		
		return getGroundY() - (rocket.getY() + rocket.getHeight());
		
	}
	
	public double calculateTotalThrustPower() {
		
		double sum = 0;
		for (RocketEngine engine : rocket.getEngines()) {
			
			sum += engine.getThrustPower();
			
		}
		return sum;
		
	}
	
	/**
	 * Performs the quadratic formula given coefficients for the x^2, x, and
	 * constant values
	 * @param a the coefficient of the x^2 component
	 * @param b the coefficient of the x component
	 * @param c the coefficient of the constant component
	 * @return a sorted array of the solutions, with the greater solution first
	 */
	public double[] doQuadraticFormula(double a, double b, double c) {
		// x = (-b +- sqrt(b^2 - 4ac)) / 2a
		double solution1 = (-b + Math.sqrt((b * b) - 4 * a * c)) / (2 * a);
		double solution2 = (-b - Math.sqrt((b * b) - 4 * a * c)) / (2 * a);
		double [] solutions = {solution1, solution2};
		
		if (solution1 > solution2) {
			
			solutions[0] = solution1;
			solutions[1] = solution2;
			
		} else {
			
			solutions[0] = solution2;
			solutions[1] = solution1;
			
		}
		
		return solutions;
		
	}
	
	/**
	 * Calculates the height that the Rocket should begin burning its engine at 
	 * full throttle in order to have a y velocity of 0 when it reaches the 
	 * ground. 
	 * @return the height, in pixels from the ground, when the Rocket should
	 * start firing its engine
	 */
	public double calculateBurnHeight(double safetyMargin) {
		
		/*
		double thrustYAccel = (getThrustPower() * 
				Math.sin(Math.toRadians(getDirection()))) - World.GRAVITY;
		*/
		double thrustYAccel = (calculateTotalThrustPower() * 
				Math.sin(Math.toRadians(rocket.getDirection()))) - World.GRAVITY;
		if (thrustYAccel < 0) {
			
			return 0;
			
		} else {
			
			double timeToCounterYvelocity = Math.abs(
					rocket.getVelocity().getY() / thrustYAccel
					);

			double burnYTravel = ((-0.5) * thrustYAccel * 
					(Math.pow(timeToCounterYvelocity, 2)))
					+ rocket.getVelocity().getY() * timeToCounterYvelocity;

			return burnYTravel + safetyMargin;
			
		}
		
	}
	
}
