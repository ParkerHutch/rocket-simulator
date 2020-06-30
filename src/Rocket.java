import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

/**
 * @author Parker Hutchinson
 * Start Date: September 13, 2019
 */

public class Rocket extends Entity {
	
	// Physics variables
	private double width = 40;
	private double centerTankWidth = width / 2.5;
	private double height = 100;
	private double noseConeHeight = 30;
	private double centerTankHeight = height - noseConeHeight - 10;
	private double finHeight = 20;
	private double engineConeWidth = centerTankWidth;
	private double engineConeHeight = 10;
	private double turnRate = 60; // degrees per second (note: 360 works well with 1 engine)
	// TODO Account for turnRate time in maneuverCalculator
	
	// Flight attributes
	private boolean airborne = true;
	private double fuel; // TODO Remove this if I'm not using this concept
	private RocketEngine [] engines;
	private ParticleEmitter [] rcsThrusters;
	
	private RocketComputer computer;
	//private ManeuverCalculator maneuverCalculator;
	
	Rocket() {}
	
	/**
	 * Creates a Rocket with default width, height and color, and parameters
	 * for x and y and fuel.
	 * @param x the middle x coordinate of the Rocket
	 * @param y the top y coordinate of the Rocket
	 * @param fuel the initial fuel amount of the Rocket
	 * @param groundY the y-coordinate of the top of the ground
	 */
	Rocket(double x, double y, double fuel, double groundY, AnimationTimer animator, GraphicsContext gc) {
		
		// TODO Cut down on parameters like animator and gc by moving RocketComputer out of here
		super(x, y);
		this.fuel = fuel;
		this.computer = new RocketComputer(0, 0, 100, 800, this, groundY, animator);
		//maneuverCalculator = new ManeuverCalculator(this, groundY);
		this.engines = new RocketEngine [] {
				new RocketEngine(groundY, getEngineConeWidth(), getEngineConeHeight(), 
						0, getHeight() - getEngineConeHeight())
		};
		
		double rcsYoffset = getNoseConeHeight() + getCenterTankHeight() / 2 
				- getFinHeight();
		double rcsXOffset = getCenterTankWidth() / 2 + 4 / 2;
		this.rcsThrusters = new ParticleEmitter [] {
				
				new ParticleEmitter(4, 8, groundY, 
						new Color[] {Color.WHITE}, -rcsXOffset, rcsYoffset, 
						-90, Color.RED),
				new ParticleEmitter(4, 8, groundY, 
						new Color[] {Color.WHITE}, rcsXOffset, rcsYoffset, 
						90, Color.RED)
				
		};
	}

	/** 
	 * Gets the width of the rocket
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Sets the width of the Rocket
	 * @param width the new width of the Rocket
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	public double getCenterTankWidth() {
		return centerTankWidth;
	}

	public void setCenterTankWidth(double centerTankWidth) {
		this.centerTankWidth = centerTankWidth;
	}

	/**
	 * Gets the height of the Rocket
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Sets the height of the Rocket
	 * @param height the new height of the Rocket
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	public double getCenterTankHeight() {
		return centerTankHeight;
	}

	public void setCenterTankHeight(double centerTankHeight) {
		this.centerTankHeight = centerTankHeight;
	}

	public double getFinHeight() {
		return finHeight;
	}

	public void setFinHeight(double finHeight) {
		this.finHeight = finHeight;
	}

	public double getNoseConeHeight() {
		return noseConeHeight;
	}

	public void setNoseConeHeight(double noseConeHeight) {
		this.noseConeHeight = noseConeHeight;
	}

	public double getEngineConeWidth() {
		return engineConeWidth;
	}

	public void setEngineConeWidth(double engineConeWidth) {
		this.engineConeWidth = engineConeWidth;
	}

	public double getEngineConeHeight() {
		return engineConeHeight;
	}

	public void setEngineConeHeight(double engineConeHeight) {
		this.engineConeHeight = engineConeHeight;
	}

	/**
	 * Gets the Rocket's rotation speed, in degrees per second
	 * @return the turn rate in degrees per second
	 */
	public double getTurnRate() {
		return turnRate;
	}

	/**
	 * Sets the rotation speed of the Rocket
	 * @param turnRate a rotation speed, in degrees per second
	 */
	public void setTurnRate(double turnRate) {
		this.turnRate = turnRate;
	}


	/**
	 * Gets the amount of fuel left in the Rocket
	 * @return the fuel left
	 */
	public double getFuel() {
		return fuel;
	}

	/**
	 * Sets the amount of fuel left in the Rocket
	 * @param fuel the new fuel amount
	 */
	public void setFuel(double fuel) {
		this.fuel = fuel;
	}

	/**
	 * Gets the RocketEngines associated with this Rocket
	 * @return the Rocket's engines
	 */
	public RocketEngine[] getEngines() {
		return engines;
	}

	/**
	 * Sets the RocketEngines associated with this Rocket
	 * @param engines the new engines for the Rocket
	 */
	public void setEngines(RocketEngine[] engines) {
		this.engines = engines;
	}

	public RocketComputer getComputer() {
		return computer;
	}

	public void setComputer(RocketComputer computer) {
		this.computer = computer;
	}

	public ParticleEmitter[] getRCSThrusters() {
		return rcsThrusters;
	}
	
	public void setRCSThrusters(ParticleEmitter[] rcsThrusters) {
		this.rcsThrusters = rcsThrusters;
	}

	/**
	 * Returns true if the Rocket is airborne, false otherwise
	 * @return the airborne status of the Rocket
	 */
	public boolean isAirborne() {
		return airborne;
	}

	/**
	 * Sets the airborne status of the Rocket
	 * @param airborne the new status
	 */
	public void setAirborne(boolean airborne) {
		this.airborne = airborne;
	}
	
	/**
	 * Applies the force of gravity to the Rocket's velocity vector
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public void applyGravity(double timeElapsed) {
		
		getVelocity().setY(getVelocity().getY() + World.GRAVITY * timeElapsed);
		
	}
	
	/**
	 * Applies the force of each engine's thrust to the Rocket's velocity 
	 * vector. If an engine is off or there is no fuel left, no force is added. 
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public void applyThrust(double timeElapsed) {
		
		if (getFuel() > 0) {
			
			for (RocketEngine engine : getEngines()) {
				
				if (engine.isOn()) {
					
					getVelocity().setX(getVelocity().getX() + 
							Math.cos(Math.toRadians(getDirection())) * 
							engine.getThrustPower() * timeElapsed
							);
							
						getVelocity().setY(getVelocity().getY() +
								Math.sin(Math.toRadians(getDirection())) * -1 *
								engine.getThrustPower() * timeElapsed
								);
				
						setFuel(getFuel() - 
								engine.getFuelBurnRate() * timeElapsed
								);
					
				}
				
			}
			
		} else {
			
			setEnginesOn(false);
			
		}
		
	}
	
	/**
	 * Rotates the Rocket as fast as it can(as defined by turnRate) towards
	 * the angle defined by targetAngle
	 * @param targetAngle the angle to rotate towards, in degrees
	 */
	private void pointInDirection(double targetAngle, double timeElapsed) {
		
		double distanceToTargetAngle = targetAngle - getDirection();
		
		if (distanceToTargetAngle < 0 && getVelocity().getY() > 0) {
			
			getRCSThrusters()[0].setOn(true);
			getRCSThrusters()[1].setOn(false);
			
		} else if (distanceToTargetAngle != 0 && getVelocity().getY() > 0){
			
			getRCSThrusters()[0].setOn(false);
			getRCSThrusters()[1].setOn(true);
			
		}
		//if (Math.abs(distanceToTargetAngle) < getTurnRate() * timeElapsed) {
		
		if (Math.abs(distanceToTargetAngle) <= getTurnRate() * timeElapsed) {
			
			setDirection(targetAngle);
			
		} else if (getVelocity().getY() > 0){
			
			if (distanceToTargetAngle < 0) {

				setDirection(getDirection() - getTurnRate() * timeElapsed);

			} else {

				setDirection(getDirection() + getTurnRate() * timeElapsed);

			}
			
		}
		
	}
	
	/**
	 * Sets the 'on' state of all the Rocket's engines to the given state
	 * @param state the new 'on' state for all the Rocket's RocketEngines
	 */
	public void setEnginesOn(boolean state) {
		
		for (RocketEngine engine : getEngines()) {
			
			engine.setOn(state);
			
		}
		
	}
	
	/**
	 * Stops the rocket by setting conditions and variables associated with its
	 * flight to appropriate values
	 */
	public void stop() {
		
		setAirborne(false);
		setEnginesOn(false);
		for (ParticleEmitter rcsThruster : getRCSThrusters()) {
			
			rcsThruster.setOn(false);
			
		}
		//setColor(Color.RED);
		getVelocity().setX(0);
		getVelocity().setY(0);
		setDirection(90);
		
		
	}

	@Override
	public void tick(double timeElapsed) {
		
		if (isAirborne()) {
			
			double safetyMargin = 5; // TODO Implement this better
			setEnginesOn(getComputer().getManeuverCalculator().shouldBurn(safetyMargin));
			
			pointInDirection(getVelocity().getDirection() - 180, timeElapsed);
				
			//applyGravity(timeElapsed); TODO Remove this method completely
			applyThrust(timeElapsed);
			applyForces(timeElapsed); // should be last
			//applyVelocity(timeElapsed); // This should always be last
			
		}
		
		for (RocketEngine engine : getEngines()) {
			
			engine.tick(timeElapsed);
			
		}
		
		for (ParticleEmitter rcsThruster : getRCSThrusters()) {
			
			rcsThruster.tick(timeElapsed);
			
		}
		
		getComputer().tick(timeElapsed);
		
		
	}
	
	/**
	 * Pivots the GraphicsContext around the center of the Rocket so that the
	 * Rocket can be drawn at an angle. This transform should be reversed
	 * (with a save() and restore()) before objects that should be drawn 
	 * normally on the Canvas are drawn.
	 * @param gc the GraphicsContext to rotate
	 */
	public void rotateGraphicsContext(GraphicsContext gc) {
		
		double pivotX = getX();
		double pivotY = getY() + (getHeight() / 2.0);
		Rotate rotate = new Rotate(90 - getDirection(), pivotX, pivotY);
		gc.transform(new Affine(rotate));
		
	}

	public void drawFins(GraphicsContext gc) {
		
		double [] fin1xPoints = new double[] {
				getX() - getCenterTankWidth() / 2,
				getX() - getCenterTankWidth() / 2,
				getX() - getWidth() / 2
		};
		
		double [] fin2xPoints = new double[] {
				getX() + getCenterTankWidth() / 2,
				getX() + getCenterTankWidth() / 2,
				getX() + getWidth() / 2
		};
		
		double finStartY = getY() + getNoseConeHeight() + getCenterTankHeight() - getFinHeight();
		double [] finyPoints = new double[] {
				finStartY,
				finStartY + getFinHeight(),
				finStartY + getFinHeight()
		};
		
		gc.setFill(Color.BLUE);
		gc.fillPolygon(fin1xPoints, finyPoints, finyPoints.length);
		gc.fillPolygon(fin2xPoints, finyPoints, finyPoints.length);

		
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
		getComputer().draw(gc);
		
		gc.save();
		
		rotateGraphicsContext(gc);
		
		for (RocketEngine engine : getEngines()) {
			
			engine.alignWith(this);
			engine.draw(gc);
			
		}

		for (ParticleEmitter thruster : getRCSThrusters()) {

			thruster.alignWith(this);
			thruster.draw(gc);

		}

		gc.setFill(getColor());
		
		// Bounding rectangle
		//gc.strokeRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight());
		
		// Rocket nose cone
		gc.fillArc(getX() - getCenterTankWidth() / 2, getY(), 
				getCenterTankWidth(), getNoseConeHeight() * 2, 
				0, 180, ArcType.ROUND);
		
		// Center tank
		gc.fillRect(getX() - getCenterTankWidth() / 2, 
				getY() + getNoseConeHeight(), 
				getCenterTankWidth(), getCenterTankHeight());
		
		drawFins(gc);
		
		gc.restore();
		
	}
	
}
