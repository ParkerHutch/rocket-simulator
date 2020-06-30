import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class World {
	
	private ArrayList<Entity> objects = new ArrayList<Entity>();
	
	public static final double GRAVITY = 100; // pixels/second (150 is a nice number)
	private double groundHeight = 100;
	private double groundY = -1;
	private double time = 0; // NOTE: Should this be stored here?
	
	private double windowWidth;
	private double windowHeight;
	
	World() {}
	
	public World(double windowWidth, double windowHeight) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.groundY = windowHeight - getGroundHeight();
	}

	public double getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(double windowWidth) {
		this.windowWidth = windowWidth;
	}

	public double getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(double windowHeight) {
		this.windowHeight = windowHeight;
	}

	public ArrayList<Entity> getObjects() {
		return objects;
	}

	public void setObjects(ArrayList<Entity> objects) {
		this.objects = objects;
	}

	public double getGroundY() {
		return groundY;
	}

	public void setGroundY(double groundY) {
		this.groundY = groundY;
	}
	
	
	public double getGroundHeight() {
		return groundHeight;
	}

	public void setGroundHeight(double groundHeight) {
		this.groundHeight = groundHeight;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public boolean rocketTouchingGround(Rocket rocket) {
		
		return rocket.getY() + rocket.getHeight() >= getGroundY();
		
	}
	
	public void handleRocketTouchingGround(Rocket rocket) {
		
		rocket.stop();
		
	}
	
	/**
	 * Updates all the objects of the World, and increments
	 * the world time.
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public void tick(double timeElapsed) {
		
		for (Entity entity: objects) {
			
			if (entity.getClass() == Rocket.class) {
				
				if (rocketTouchingGround((Rocket) entity)) {
					
					handleRocketTouchingGround((Rocket) entity);
					
				} else {
					
					entity.tick(timeElapsed);
					
				}
				
			}
			
			
		}
		
		
		
	}
	
	/**
	 * Draws the ground level with color filled below it
	 * @param gc The GraphicsContext for the Canvas to draw the ground on
	 */
	public void drawGround(GraphicsContext gc) {
		
		gc.setFill(Color.GREEN);
		gc.fillRect(0, getWindowHeight() - getGroundHeight(), 
				getWindowWidth(), getGroundHeight());
		
	}
	
	/**
	 * Draws the World and all its child objects on the Canvas
	 * @param gc the GraphicsContext for the Canvas to draw the objects on
	 */
	public void draw(GraphicsContext gc) {
		
		gc.save();
		
		drawGround(gc);
		
		for (Entity entity : objects) {
			
			entity.draw(gc);
			
		}
		
		gc.restore();
			
	}
	
	
	
}
