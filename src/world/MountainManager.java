package world;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MountainManager {
	
	private ArrayList<Double> mountainXPoints = new ArrayList<Double>();
	private ArrayList<Double> mountainYPoints = new ArrayList<Double>();
	
	private double xStep;
	private double maxShiftMagnitude;
	
	private double groundY;
	private double windowWidth;
	
	public MountainManager(double windowWidth, double xStep, 
			double maxShiftMagnitude, double groundY) {
		
		this.xStep = xStep;
		this.maxShiftMagnitude = maxShiftMagnitude;
		this.windowWidth = windowWidth;
		this.groundY = groundY;
		fillPoints(windowWidth, 10);
		
	}
	
	public ArrayList<Double> getMountainXPoints() {
		return mountainXPoints;
	}

	public ArrayList<Double> getMountainYPoints() {
		return mountainYPoints;
	}

	public double getxStep() {
		return xStep;
	}

	public void setxStep(double xStep) {
		this.xStep = xStep;
	}

	public double getMaxShiftMagnitude() {
		return maxShiftMagnitude;
	}

	public void setMaxShiftMagnitude(double maxShiftMagnitude) {
		this.maxShiftMagnitude = maxShiftMagnitude;
	}

	private double getGroundY() {
		return groundY;
	}

	private double getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(double windowWidth) {
		this.windowWidth = windowWidth;
	}

	private double getNextY(double lastY) {
		
		return (lastY - maxShiftMagnitude) + 
				Math.random() * (2 * maxShiftMagnitude);
		
	}
	
	private void fillPoints(double windowWidth, int initialGenSize) {
		
		//int arrayLength = (int) ((windowWidth / xStep) * coefficient);
		//setMountainXPoints(new double[arrayLength]);
		//setMountainYPoints(new double[arrayLength]);
		
		// TODO don't forget to make sure that mountainXPoints starts and ends with 0
		// TODO and that mountainYPoints starts and ends with groundY
		// TODO Maybe ignore this when generating the points, and use 
		// TODO variables like actualXPoints where the neccessary ones have been
		// inserted at beginning and end of the arrays when returning the polygon?
		
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
	
	private void fillViewingWindow(double canvasLeftX, double margin) {
		if (getMountainXPoints().size() == 0 || getMountainYPoints().size() == 0) {
			
			
			System.out.println("still loading mountains");

		} else {
			
			if (getMountainXPoints().get(0) > canvasLeftX - margin) {
				
				double leftMountainX = getMountainXPoints().get(0);
				getMountainXPoints().add(0, leftMountainX - xStep);
				
				double leftMountainY = getMountainYPoints().get(0);
				getMountainYPoints().add(0, getNextY(leftMountainY));
				
				//double leftMountainX = getMountainXPoints()[0];
				//double leftMountainY = getMountainYPoints()[0];
				
				
				//setMountainXPoints(insertAtBeginning(getMountainXPoints(), leftMountainX - xStep));
				//setMountainYPoints(insertAtBeginning(getMountainYPoints(), getNextY(leftMountainY)));

				// TODO Call fillViewingWindow again(recursion?)

			}

			double canvasRightX = canvasLeftX + getWindowWidth();
			if (getMountainXPoints().get(getMountainXPoints().size() - 1) < canvasRightX + margin) {

				double rightMountainX = getMountainXPoints().get(getMountainXPoints().size() - 1);
				double rightMountainY = getMountainYPoints().get(getMountainYPoints().size() - 1);
				
				getMountainXPoints().add(rightMountainX + xStep);
				getMountainYPoints().add(getNextY(rightMountainY));
				//setMountainXPoints(append(getMountainXPoints(), rightMountainX + xStep));
				//setMountainYPoints(append(getMountainYPoints(), getNextY(rightMountainY)));

			}

		}
		
		
	}
	
	public void draw(GraphicsContext gc) {
		
		fillViewingWindow(-gc.getTransform().getTx(), 500);
		gc.setFill(Color.SADDLEBROWN);
		
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
			
			
			gc.setStroke(Color.GRAY.darker());
			gc.setLineWidth(3);
			gc.strokeLine(getMountainXPoints().get(i), getMountainYPoints().get(i), 
					getMountainXPoints().get(i+1), getMountainYPoints().get(i+1));
			
			
		}
		
	}
	
}
