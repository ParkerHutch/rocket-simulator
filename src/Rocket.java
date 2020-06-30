import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

/**
 * @author Parker Hutchinson
 * Start Date: September 13, 2019
 */

public class Rocket extends Entity {
	
	Shape collisionShape; // should be a polygon which can also be drawn
	
	// Physics variables
	private double direction = 90; // the angle between the ground and the top of the rocket
	private Vector2D velocity = new Vector2D();
	private double groundY;
	private double initialHeight;
	private double turnRate = 10; // degrees per second
	
	// Flight attributes
	private boolean airborne = true;
	private double fuel; // TODO Remove this if I'm not using this concept
	private double fuelBurnRate = 1; // units per second
	private double thrustPower = 200; // pixels per second
	private boolean engineOn = false;
	private double burnTime = Double.MAX_VALUE; // TODO Change default value?
	
	// Time variables
	long motionStartTime = -1;
	double secondsSinceMotionStart = 0;
	
	Rocket() {}
	
	/**
	 * Creates a Rocket with default width, height and color, and parameters
	 * for x and y and fuel.
	 * @param x the middle x coordinate of the Rocket
	 * @param y the top y coordinate of the Rocket
	 * @param fuel the initial fuel amount of the Rocket
	 * @param groundY the y-coordinate of the top of the ground
	 */
	Rocket(double x, double y, double fuel, double groundY) {
		super(x, y);
		this.collisionShape = new Rectangle(
				getX() - getWidth() / 2, getY(), getWidth(), getHeight());
		this.fuel = fuel;
		this.groundY = groundY;
		this.initialHeight = calculateDistanceToGround();
		setBurnTime(calculateBurnHeight());// TODO Reenable this if you want
		this.motionStartTime = System.nanoTime(); 
		// motionStartTime should be updated to a more accurate time (i.e. 
		// closer to actual animation start time) in the class running the 
		// simulation
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}


	/**
	 * Gets the angle between the top of the rocket and the positive x-axis
	 * in degrees.
	 * @return the rocket's direction in degrees
	 */
	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	public double getTurnRate() {
		return turnRate;
	}

	public void setTurnRate(double turnRate) {
		this.turnRate = turnRate;
	}

	public double getInitialHeight() {
		return initialHeight;
	}

	public void setInitialHeight(double initialHeight) {
		this.initialHeight = initialHeight;
	}

	public double getFuel() {
		return fuel;
	}

	public void setFuel(double fuel) {
		this.fuel = fuel;
	}
	
	public double getFuelBurnRate() {
		return fuelBurnRate;
	}

	public void setFuelBurnRate(double fuelBurnRate) {
		this.fuelBurnRate = fuelBurnRate;
	}

	public boolean isEngineOn() {
		return engineOn;
	}

	public void setEngineOn(boolean engineOn) {
		this.engineOn = engineOn;
	}

	public double getThrustPower() {
		return thrustPower;
	}

	public void setThrustPower(double thrustPower) {
		this.thrustPower = thrustPower;
	}

	public double getBurnTime() {
		return burnTime;
	}

	public void setBurnTime(double burnTime) {
		this.burnTime = burnTime;
	}

	public double getGroundY() {
		return groundY;
	}

	public void setGroundY(double groundY) {
		this.groundY = groundY;
	}

	public boolean isAirborne() {
		return airborne;
	}

	public void setAirborne(boolean crashed) {
		this.airborne = crashed;
	}
	
	public long getMotionStartTime() {
		return motionStartTime;
	}

	public void setMotionStartTime(long motionStartTime) {
		this.motionStartTime = motionStartTime;
	}

	public double getSecondsSinceMotionStart() {
		return secondsSinceMotionStart;
	}

	public void setSecondsSinceMotionStart(double secondsSinceMotionStart) {
		this.secondsSinceMotionStart = secondsSinceMotionStart;
	}

	public double calculateSecondsSinceMotionStart() {
		
		long currentTime = System.nanoTime();
		long timeElapsedNano = currentTime - motionStartTime;
		return timeElapsedNano / 1_000_000_000.0;
		
	}

	/**
	 * Calculates the remaining change in velocity that the Rocket is able to 
	 * produce with the fuel remaining.
	 */
	public double calculateDeltaV() {
		
		return (getFuel() / getFuelBurnRate()) * getThrustPower();
		
	}
	
	/**
	 * Applies the force of gravity to the Rocket's velocity vector
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public void applyGravity(double timeElapsed) {
		
		getVelocity().setY(getVelocity().getY() + World.GRAVITY * timeElapsed);
		
	}
	
	/**
	 * Applies the force of engine thrust to the Rocket's velocity vector.
	 * If the engine is off, no force is added. 
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public void applyEngineThrust(double timeElapsed) {
		
		// Propel the rocket in the direction it is facing at max thrust
		// Decrease fuel
		
		if (getFuel() > 0) {
			
			if (isEngineOn()) {
				
				
				getVelocity().setX(getVelocity().getX() + 
					Math.cos(Math.toRadians(getDirection())) * getThrustPower() * timeElapsed);
					
				getVelocity().setY(getVelocity().getY() +
						Math.sin(Math.toRadians(getDirection())) * 
						-1 *
						getThrustPower() 
						* timeElapsed);
		
				setFuel(getFuel() - getFuelBurnRate() * timeElapsed);
				
			}
			
		}
		
		
	}
	
	/**
	 * Adds the x and y components of the Rocket's velocity vector to its x and
	 * y position.
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public void applyVelocity(double timeElapsed) {
		
		setX(getX() + getVelocity().getX() * timeElapsed);
		setY(getY() + getVelocity().getY() * timeElapsed);
		
	}
	
	/**
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
	
	public double calculateDistanceToGround() {
		
		return getGroundY() - (getY() + getHeight());
		
	}
	
	/**
	 * Uses kinematic equations to determine the time(in seconds since program
	 * start) when the rocket will make contact with the ground.
	 * dY = yVelocity * time + (1/2) * accelerationY * time^2
	 * @param currentTime the current time, in seconds since program start
	 * @return the time when the rocket will hit the ground, in seconds since 
	 * program start
	 */
	public double calculateExpectedImpactTime() {
		
		// The values below should be initial values of the rocket
		// TODO This needs to be fixed, use an online calculator
		// TODO I think I fixed this, explain what I changed(the signs)
		double [] impactTimes;
		
		if (isEngineOn()) {

			double acceleration = getThrustPower() - World.GRAVITY;
			impactTimes =
					doQuadraticFormula((-0.5) * acceleration, 
							-getVelocity().getY(), 
							calculateDistanceToGround());
			
		} else {
			
			impactTimes =
					doQuadraticFormula((-0.5) * World.GRAVITY, 
							-getVelocity().getY(), 
							calculateDistanceToGround());
			
		}
		
		return impactTimes[0];
		
	}
	
	/**
	 * Calculates the time, in seconds after start of motion, that the Rocket
	 * should begin burning its engine at full throttle power in order to 
	 * have a y velocity of 0 when it reaches the ground. 
	 * @return the time to start firing the engine
	 */
	public double calculateBurnHeight() {

		// TODO: Do a check so that rocket doesn't think it will have to burn
		// at 170 degrees to land correctly. Maybe check that Yvelocity > xvel
		// or something like that
		double thrustYAccel = (getThrustPower() * 
				Math.sin(Math.toRadians(getDirection()))) - World.GRAVITY;
		if (thrustYAccel< 0) {
			
			/*
			System.out.println("Thrust y component is negative. " + 
					"Calculation is probably being performed very early in the " +
					"launch.");
			System.out.println("Direction: " + getDirection());
			*/
			return 0;
		} else {
			
			double timeToCounterY = Math.abs(
					getVelocity().getY() / thrustYAccel
					); // this is time DURATION

			// calculate distance that would be travelled while Rocketis countering y

			// I don't know why, but putting a negative in the thing below seemed to fix
			// the stuff TODO Explain or delete this comment
			double burnYTravel = ((-0.5) * thrustYAccel * (Math.pow(timeToCounterY, 2)))
					+ getVelocity().getY() * timeToCounterY;

			return burnYTravel;
			
		}
		
		
		
	}
	
	/**
	 * Rotates the Rocket as fast as it can(as defined by turnRate) towards
	 * the angle defined by targetAngle
	 * @param targetAngle the angle to rotate towards, in degrees
	 */
	public void pointInDirection(double targetAngle, double timeElapsed) {
		
		double distanceToTargetAngle = targetAngle - getDirection();
		
		if (Math.abs(distanceToTargetAngle) < getTurnRate() * timeElapsed) {
			
			setDirection(targetAngle);
			
		} else {
			
			// TODO: Show an RCS animation for these cases below
			if (distanceToTargetAngle < 0) {
				
				setDirection(getDirection() - getTurnRate() * timeElapsed);
				
			} else {
				
				setDirection(getDirection() + getTurnRate() * timeElapsed);
				
			}
			
		}
		
	}
	
	/**
	 * Sets the velocity of the Rocket to 0, crashed to true.
	 */
	public void stop() {
		
		setAirborne(false);
		setColor(Color.RED);
		getVelocity().setX(0);
		getVelocity().setY(0);
		setDirection(90);
		
		
	}

	public void tick(double timeElapsed) {
		// Apply user input (Or do this in runner file/ use one of the 
		// keyboardinput classes i made)
		if (isAirborne()) {
			
			
			if (calculateBurnHeight() >= calculateDistanceToGround() 
					&& Math.sin(Math.toRadians(getVelocity().getDirection())) < 0) {
				
				setEngineOn(true);
				
			} else if (Math.sin(Math.toRadians(getVelocity().getDirection())) > 0){
				
				setEngineOn(false);
				
			}
			
			// Rocket points in direction opposite of its velocity vector
			// to get max efficiency
			// TODO: If I get errors when implementing turnRate (Rocket may
			// not be able to turn fast enough), maybe only do this direction
			// change when engine is off
			if (Math.abs(getVelocity().getX()) <= 0.03) {
				
				getVelocity().setX(0);
				
			}
			
			if (getVelocity().getDirection() <= 225 || 
					getVelocity().getDirection() >= 315 ||
					getVelocity().getY() <= 0) {
				
				setDirection(90);
				
			} else {
				
				pointInDirection(getVelocity().getDirection() - 180, timeElapsed);
				
			}
			
			
			
			applyGravity(timeElapsed);
			applyEngineThrust(timeElapsed);
			applyVelocity(timeElapsed); // This should always be last
			
		}
		
		setSecondsSinceMotionStart(calculateSecondsSinceMotionStart());
		
		
	}
	
	public void drawEnginePlume(GraphicsContext gc) {
		
		double [] triangleXPoints =  {
				getX(),
				getX() - getWidth() / 2,
				getX() + getWidth() / 2
				
		};
		double [] triangleYPoints = {
				getY() + getHeight(), 
				getY() + getHeight() + 10,
				getY() + getHeight() + 10
		};
		
		gc.setFill(Color.ORANGE);
		gc.fillPolygon(triangleXPoints, triangleYPoints, 3);
		
	}
	
	
	public void draw(GraphicsContext gc) {
		
		// TODO: Make the rocket a polygon, use gc.fillPolygon
		// TODO: Also use this polygon to check for collisions?
		
		gc.save();
		
		double pivotX = getX();
		double pivotY = getY() + (getHeight() / 2.0);
		Rotate rotate = new Rotate(90 - getDirection(), pivotX, pivotY);
		gc.setTransform(new Affine(rotate));
		
		gc.setFill(getColor());
		
		gc.fillRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight());
		
		if (isEngineOn() && isAirborne()) {
			
			drawEnginePlume(gc);
			
		}
		gc.restore();
		
	}
	
	@Override
	public String toString() {
		
		String summary = "X: " + getX() + " Y: " + getY() + "\n" +
				"Distance to ground: " + (calculateDistanceToGround()) +
				"\n" +
				"xVelocity: " + getVelocity().getX() + " yVelocity: " +
				 getVelocity().getY() + "\n" + 
				"Airborne: " + isAirborne() + "\n" + 
				"Expected impact time: " + calculateExpectedImpactTime() + "\n" + 
				"Burn time: " + getBurnTime() + "\n" +
				"Current internal time: " + getSecondsSinceMotionStart();
		return summary;
		
	}
	
}
