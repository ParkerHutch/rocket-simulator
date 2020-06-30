/**
 * @author Parker Hutchinson
 *	A class for storing a momentum vector. The Vector2D holds variables for the
 *	x and y components of this momentum vector, as well as methods for
 *	calculating the direction and magnitude of the vector.
 */
public class Vector2D {
	
	private double x = 0; // The vector's x component
	private double y = 0; // The vector's y component
	
	Vector2D() {}
	
	/**
	 * Creates a Vector2D with given x and y components
	 * @param x the x component of the vector
	 * @param y the y component of the vector
	 */
	Vector2D(double x, double y) {
		
		setX(x);
		setY(y);
		
	}
	
	/**
	 * Creates a Vector2D with x and y components derived from a given angle
	 * and magnitude
	 * @param direction the direction of the vector (in degrees)
	 * @param magnitude the magnitude of the vector (should be positive)
	 */
	Vector2D(int direction, double magnitude) {
		
		setX(magnitude * Math.cos(direction));
		setY(magnitude * Math.sin(direction));
		
	}

	/**
	 * Gets the x component of the vector
	 * @return the x component
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the x component of the vector
	 * @param x the new x component
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the y component of the vector
	 * @return the y component
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the y component of the vector
	 * @param y the new y component
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Computes the magnitude of the vector using the Pythagorean theorem
	 * @return the magnitude of the vector
	 */
	public double getMagnitude() {
		
		return Math.sqrt(x * x + y * y);
		
	}
	
	/**
	 * Gets the angle between the vector and the x-axis on the coordinate plane
	 * @return the angle of the vector (in degrees)
	 */
	public double getDirection() {
		
		double angle = Math.toDegrees(Math.atan2(y, x)) * -1;
		
		if (angle < 0) {
			
			angle += 360;
			
		}
		
		return angle;
		
	}
	
	
}
