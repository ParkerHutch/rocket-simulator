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

	public void setMountainXPoints(ArrayList<Double> mountainXPoints) {
		this.mountainXPoints = mountainXPoints;
	}

	public ArrayList<Double> getMountainYPoints() {
		return mountainYPoints;
	}

	public void setMountainYPoints(ArrayList<Double> mountainYPoints) {
		this.mountainYPoints = mountainYPoints;
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

	private void setGroundY(double groundY) {
		this.groundY = groundY;
	}

	public double getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(double windowWidth) {
		this.windowWidth = windowWidth;
	}

	private double getNextY(double lastY) {
		
		return (lastY - maxShiftMagnitude) + 
				Math.random() * (2 * maxShiftMagnitude);
		
	}
	
	public void fillPoints(double windowWidth, int initialGenSize) {
		
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
	
	/*
	public void getMountainPolygon(double leftX, double rightX) {
		
		// TODO Maybe don't do this, just draw one parallelogram at a time?
		double margin = xStep;
		
		int totalPointsNeeded = 0;
		int indexStart = -1;
		int indexEnd = -1;
		
		for (int i = 0; i < getMountainXPoints().length && indexEnd == -1; i++) {
			
			if (getMountainXPoints()[i] >= leftX - margin) {
				
				totalPointsNeeded++;
				if (indexStart == -1) {
					
					indexStart = i;
					
				}
				
			} else if (getMountainXPoints()[i] > rightX + margin) {
				
				indexEnd = i - 1;
				
			}
			
		}
		
		double [] actualXPoints = new double[totalPointsNeeded];
		actualXPoints[0] = getMountainXPoints()[indexStart];
		
		double [] actualYPoints = new double[totalPointsNeeded];
		actualYPoints[0] = groundY;
		
		int actualPointsIterator = 1;
		
		for (int j = indexStart; j <= indexEnd; j++) {
			
			actualXPoints[actualPointsIterator] = 
					getMountainXPoints()[j];
			actualYPoints[actualPointsIterator] = 
					getMountainYPoints()[j];
			actualPointsIterator++;
			
		}
		
		actualXPoints[actualXPoints.length - 1] = actualXPoints[0];
		actualYPoints[actualYPoints.length - 1] = groundY;
		
	}*/
	
	public void draw(GraphicsContext gc) {
		
		fillViewingWindow(-gc.getTransform().getTx(), 500);
		gc.setFill(Color.SADDLEBROWN);
		
		/*
		 * TODO Get rid of this
		gc.fillPolygon(new double [] {0, 0, 1000, 1000, 0},  
				new double [] {groundY, groundY -100, groundY - 500, groundY, groundY},  5);
			*/	
		
		// TODO Stroke tops of mountains so they look cool
		
		for (int i = 0; i < getMountainXPoints().size() - 1; i++) {
			//double a = getMountainYPoints()[i+1];
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
			
			
		}
		
	}
	
}
