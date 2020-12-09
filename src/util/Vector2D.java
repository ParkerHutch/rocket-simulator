package util;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Parker Hutchinson
 *	A class for storing a momentum vector. The Vector2D holds variables for the
 *	x and y components of this momentum vector, as well as methods for
 *	calculating the direction and magnitude of the vector.
 */
public class Vector2D {
	
	private double x = 0; // The vector's x component
	private double y = 0; // The vector's y component
	
	public Vector2D() {}
	
	/**
	 * Creates a Vector2D with given x and y components
	 * @param x the x component of the vector
	 * @param y the y component of the vector
	 */
	public Vector2D(double x, double y) {
		
		setX(x);
		setY(y);
		
	}
	
	/**
	 * Creates a Vector2D with x and y components derived from a given angle
	 * and magnitude
	 * @param direction the direction of the vector (in degrees)
	 * @param magnitude the magnitude of the vector (should be positive)
	 */
	public Vector2D(int direction, double magnitude) {
		
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
	 * @return the angle of the vector (in degrees) between 0 and 360
	 */
	public double getDirection() {
		
		double angle = Math.toDegrees(Math.atan2(y, x)) * -1;
		
		if (angle < 0) {
			
			angle += 360;
			
		}
		
		return angle;
		
	}
	
	/**
	 * Assigns the Vector2D's x and y components from a vector in a given
	 * direction and with a given magnitude
	 * @param magnitude the magnitude of the given vector
	 * @param angle the angle of the given vector(counterclockwise from
	 * positive x-axis)
	 */
	public void createVector(double magnitude, double angle) {
		
		setX(magnitude * Math.cos(Math.toRadians(angle)));
		setY(magnitude * Math.sin(Math.toRadians(angle)));
		
	}
	
	public void draw(double tailX, double tailY, Color color, 
			double lineWidth, GraphicsContext gc) {
		
		// TODO Implement arrow head for vector
		gc.setStroke(color);
		gc.setLineWidth(lineWidth);
		gc.strokeLine(tailX, tailY, tailX + getX(), tailY + getY());
		
	}
	
	
}
