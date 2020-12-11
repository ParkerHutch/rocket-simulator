package world;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import design.ColorPalette;

/**
 * A class that handles the creation and drawing of mountains, used in the 
 * <code>World</code> class.
 */
public class MountainManager {
	
	private ArrayList<Double> mountainXPoints = new ArrayList<Double>();
	private ArrayList<Double> mountainYPoints = new ArrayList<Double>();
	
	private double xStep; 
	private double maxShiftMagnitude; 
	
	private double groundY;
	private double windowWidth;

	private ColorPalette palette;

	/**
	 * Creates a MountainManager for a window of given width with arguments for
	 * xStep, maxShiftMagnitude, the ground's top y-coordinate, and the color 
	 * palette.
	 * @param windowWidth the width of the Canvas the simulation takes place in
	 * @param xStep how far apart mountain vertices should be
	 * @param maxShiftMagnitude the maximum deviation between one mountain
	 * vertex y-coordinate and the next
	 * @param groundY the ground's top y-coordinate
	 * @param palette the color palette to use when drawing the mountains
	 */
	public MountainManager(double windowWidth, double xStep, 
			double maxShiftMagnitude, double groundY, ColorPalette palette) {
		
		this.xStep = xStep;
		this.maxShiftMagnitude = maxShiftMagnitude;
		this.windowWidth = windowWidth;
		this.groundY = groundY;
		this.palette = palette;
		fillPoints(20);
		
	}
	
	/**
	 * Gets an ordered list of the mountain range's x-coordinates.
	 * @return the mountain range's x-coordinates
	 */
	public ArrayList<Double> getMountainXPoints() {
		return mountainXPoints;
	}

	/**
	 * Gets an ordered list of the mountain range's y-coordinates.
	 * @return the mountain range's y-coordinates
	 */
	public ArrayList<Double> getMountainYPoints() {
		return mountainYPoints;
	}
	
	/**
	 * Gets the horizontal distance between mountain vertices.
	 * @return the x step
	 */
	public double getxStep() {
		return xStep;
	}

	/**
	 * Sets the horizontal distance between mountain vertices.
	 * @param xStep the new x step
	 */
	public void setxStep(double xStep) {
		this.xStep = xStep;
	}

	/**
	 * Gets the maximum deviation between one mountain vertex y-coordinate and 
	 * the next
	 * @return the maximum shift magnitude
	 */
	public double getMaxShiftMagnitude() {
		return maxShiftMagnitude;
	}

	/**
	 * Sets the maximum deviation between one mountain vertex y-coordinate and 
	 * the next
	 * @param maxShiftMagnitude the maximum shift magnitude
	 */
	public void setMaxShiftMagnitude(double maxShiftMagnitude) {
		this.maxShiftMagnitude = maxShiftMagnitude;
	}

	/**
	 * Gets the ground's top y-coordinate
	 * @return the ground's top y-coordinate
	 */
	private double getGroundY() {
		return groundY;
	}

	/**
	 * Gets the width of the window that this MountainManager occupies.
	 * @return the window width
	 */
	private double getWindowWidth() {
		return windowWidth;
	}

	/**
	 * Sets the width of the window that this MountainManager occupies.
	 * @param windowWidth the window width
	 */
	public void setWindowWidth(double windowWidth) {
		this.windowWidth = windowWidth;
	}

	/**
	 * Generates the y-coordinate of the next mountain vertex based on the last
	 * one.
	 * @param lastY the last mountain vertex's y-coordinate
	 * @return the next mountain vertex's y-coordinate
	 */
	private double getNextY(double lastY) {
		
		return (lastY - maxShiftMagnitude) + 
				Math.random() * (2 * maxShiftMagnitude);
		
	}

	/**
	 * Gets the color palette to use when drawing the mountains.
	 * @return the MountainManager's color palette
	 */
	private ColorPalette getPalette() {
		return this.palette;
	}

	/**
	 * Sets the color palette to use when drawing the mountains
	 * @param palette the MountainManager's color palette
	 */
	public void setPalette(ColorPalette palette) {
		this.palette = palette;
	}
	
	/**
	 * Fills the lists storing mountain x and y points by continuously 
	 * generating vertex x and y coordinates.
	 * @param initialGenSize the number of mountain vertices to generate
	 */
	private void fillPoints(int initialGenSize) {
		
		for (int i = 0; i < initialGenSize; i ++) {
			
			getMountainXPoints().add(i * xStep);
			
		}
		
		double baseY = getGroundY() - 300;
		
		for (int j = 0; j < initialGenSize; j++) {
			
			double lastY = j == 0 ? baseY : getMountainYPoints().get(j-1);
			
			double nextY = getNextY(lastY);
			
			if (nextY > baseY) {
				
				nextY = baseY;
				
			}
			
			getMountainYPoints().add(nextY);
			
		}
		
	}
	
	/**
	 * Makes sure that there are enough mountain vertices to fill the Canvas
	 * that the MountainManager occupies.
	 * @param canvasLeftX the left x-coordinate of the Canvas used to draw the 
	 * mountains
	 * @param margin the area outside of the Canvas to generate mountain 
	 * vertices in
	 */
	private void fillViewingWindow(double canvasLeftX, double margin) {
		if (getMountainXPoints().size() == 0 || getMountainYPoints().size() == 0) {
			
			System.out.println("still loading mountains");

		} else {
			
			if (getMountainXPoints().get(0) > canvasLeftX - margin) {
				
				double leftMountainX = getMountainXPoints().get(0);
				getMountainXPoints().add(0, leftMountainX - xStep);
				
				double leftMountainY = getMountainYPoints().get(0);
				getMountainYPoints().add(0, getNextY(leftMountainY));
				
			}

			double canvasRightX = canvasLeftX + getWindowWidth();
			if (getMountainXPoints().get(getMountainXPoints().size() - 1) < canvasRightX + margin) {

				double rightMountainX = getMountainXPoints().get(getMountainXPoints().size() - 1);
				double rightMountainY = getMountainYPoints().get(getMountainYPoints().size() - 1);
				
				getMountainXPoints().add(rightMountainX + xStep);
				getMountainYPoints().add(getNextY(rightMountainY));

			}

		}
		
	}
	
	/**
	 * Draws mountains on a Canvas with the given GraphicsContext.
	 * @param gc the GraphicsContext to use when drawing the mountains
	 */
	public void draw(GraphicsContext gc) {
		
		fillViewingWindow(-gc.getTransform().getTx(), 500);
		gc.setFill(getPalette().getMountainColor());
		
		for (int i = 0; i < getMountainXPoints().size() - 1; i++) {
			
			gc.fillPolygon(
					new double [] {
							
							getMountainXPoints().get(i),
							getMountainXPoints().get(i),
							getMountainXPoints().get(i+1) + 1,
							getMountainXPoints().get(i+1) + 1,
							getMountainXPoints().get(i)
									
					},
					new double [] {
							getMountainYPoints().get(i),
							getMountainYPoints().get(i),
							getMountainYPoints().get(i+1),
							groundY,
							groundY
					},
					5
					
			);
			
			gc.setStroke(getPalette().getMountainColor().darker());
			gc.setLineWidth(3);
			gc.strokeLine(getMountainXPoints().get(i), getMountainYPoints().get(i), 
					getMountainXPoints().get(i+1), getMountainYPoints().get(i+1));
			
		}
		
	}
	
}
