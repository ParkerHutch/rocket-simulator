package world;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import rocket.Entity;
import rocket.Rocket;
import rocket.UserControlledRocket;

public class World {
	
	private ArrayList<Entity> objects = new ArrayList<Entity>();
	
	public static final double GRAVITY = 100; // pixels/second (150 is a nice number)
	private double groundHeight = 100;
	private double groundY;
	private double time = 0; // NOTE: Should this be stored here?
	
	private double windowWidth;
	private double windowHeight;
	
	private boolean centerOnRocketHorizontally = false;
	private boolean centerOnRocketVertically = false;
	
	private MountainManager mountainManager;
			
	World() {}
	
	public World(double windowWidth, double windowHeight) {
		
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.groundY = windowHeight - getGroundHeight();
		this.mountainManager = new MountainManager(
				windowWidth, 100, 100, groundY);
		
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

	public MountainManager getMountainManager() {
		return mountainManager;
	}

	public void setMountainManager(MountainManager mountainManager) {
		this.mountainManager = mountainManager;
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

	public boolean centerOnRocketHorizontally() {
		return centerOnRocketHorizontally;
	}

	public void setCenterOnRocketHorizontally(boolean centerOnRocketHorizontally) {
		this.centerOnRocketHorizontally = centerOnRocketHorizontally;
	}

	public boolean centerOnRocketVertically() {
		return centerOnRocketVertically;
	}

	public void setCenterOnRocketVertically(boolean centerOnRocketVertically) {
		this.centerOnRocketVertically = centerOnRocketVertically;
	}

	public boolean rocketTouchingGround(Rocket rocket) {
		
		return rocket.getY() + rocket.getHeight() >= getGroundY();
		
	}
	
	/**
	 * Updates all the objects of the World, and increments
	 * the world time.
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public void tick(double timeElapsed) {
		
		for (Entity entity: objects) {
			
			if (entity.getClass() == Rocket.class || 
					entity.getClass() == UserControlledRocket.class) {
				
				Rocket rocket = (Rocket) entity;
				
				if (rocketTouchingGround(rocket)) {
					
					rocket.stop();
					
				} 
				
			} 
			
			entity.tick(timeElapsed);
			
		}
		
	}
	
	public void drawSky(GraphicsContext gc) {
		
		gc.setFill(Color.DEEPSKYBLUE);
		
		double leftX = -gc.getTransform().getTx();
		double topY = -gc.getTransform().getTy();
		
		// topY is essentially the top Y coordinate of the moving Canvas window
		// that the player sees
		gc.fillRect(leftX, 
				topY, 
				getWindowWidth(), getWindowHeight());

	}
	
	/**
	 * Draws the ground level with color filled below it
	 * @param gc The GraphicsContext for the Canvas to draw the ground on
	 */
	public void drawGround(GraphicsContext gc) {
		
		gc.setFill(Color.GREEN);
		double leftX = -gc.getTransform().getTx();
		double topY = getWindowHeight() - getGroundHeight();
		// Stretch the ground rectangle to the bottom of the screen
		double height = topY + getGroundHeight() - gc.getTransform().getTy();
		gc.fillRect(leftX, topY, getWindowWidth(), height);
		
		
	}
	
	public void centerOnRocketHorizontally(GraphicsContext gc, Rocket center) {

		// TODO After adding random terrain features, add code to
		// make the camera just follow the Rocket horizontally
		double xTranslate = centerOnRocketHorizontally() ? 
				-center.getX() - gc.getTransform().getTx() 
				+ getWindowWidth() / 2 : 0;
		
				/*
		double yTranslate = centerOnRocketVertically()? 
				-center.getY() + center.getHeight() / 2 - 
				gc.getTransform().getTy() + getWindowHeight() / 2 : 0;
		*/
		gc.translate(xTranslate, 0);

	}
	
	public void centerOnRocketVertically(GraphicsContext gc, Rocket center) {

		// TODO After adding random terrain features, add code to
		// make the camera just follow the Rocket horizontally
		/*
		double xTranslate = centerOnRocketHorizontally() ? 
				-center.getX() - gc.getTransform().getTx() 
				+ getWindowWidth() / 2 : 0;*/
		
		double yTranslate = centerOnRocketVertically()? 
				-center.getY() + center.getHeight() / 2 - 
				gc.getTransform().getTy() + getWindowHeight() / 2 : 0;
		gc.translate(0, yTranslate);

	}
	
	public void centerOnRocket(GraphicsContext gc, Rocket center) {

		// TODO After adding random terrain features, add code to
		// make the camera just follow the Rocket horizontally
		
		
		double xTranslate = centerOnRocketHorizontally() ? 
				-center.getX() - gc.getTransform().getTx() 
				+ getWindowWidth() / 2 : 0;
		
		double yTranslate = centerOnRocketVertically()? 
				-center.getY() + center.getHeight() / 2 - 
				gc.getTransform().getTy() + getWindowHeight() / 2 : 0;
		
		gc.translate(xTranslate, yTranslate);

	}
	
	public void alignGraphicsContext(GraphicsContext gc) {

		if (centerOnRocketHorizontally() || centerOnRocketVertically()) {

			for (Entity entity : getObjects()) {

				if (entity.getClass() == Rocket.class || 
						entity.getClass() == UserControlledRocket.class) {

					Rocket rocket = (Rocket) entity;

					if (centerOnRocketHorizontally()) {

						centerOnRocketHorizontally(gc, rocket);

					}

					if (centerOnRocketVertically()) {

						centerOnRocketVertically(gc, rocket);

					}

				}

			}

		}
		
	}
	
	/**
	 * Draws the World and all its child objects on the Canvas
	 * @param gc the GraphicsContext for the Canvas to draw the objects on
	 */
	public void draw(GraphicsContext gc) {
		
		alignGraphicsContext(gc);
		
		drawSky(gc);
		
		getMountainManager().draw(gc); 
		
		for (Entity entity : getObjects()) {
			
			entity.draw(gc);
			
		}
		
		drawGround(gc);
		
	}
	
}
