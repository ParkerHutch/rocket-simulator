package world;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import util.Entity;
import rocket.Rocket;
import rocket.UserControlledRocket;
import design.ColorPalette;

/**
 * A class used to encapsulate all the bodies that interact in the simulation.
 */
public class World {
	
	private ArrayList<Entity> objects = new ArrayList<Entity>();
	
	public static final double GRAVITY = 100; // pixels/second^2
	private double groundHeight = 100;
	private double groundY;
	
	private double windowWidth;
	private double windowHeight;
	
	private boolean centerOnRocketHorizontally = false;
	private boolean centerOnRocketVertically = false;
	
	private MountainManager mountainManager;

	private Rocket primaryRocket;

	private ColorPalette palette;

	World() {}
	
	/**
	 * Creates a World for a window of given dimensions and a given color 
	 * palette.
	 * @param windowWidth the width of the window this World occupies
	 * @param windowHeight the height of the window this World occupies
	 * @param palette the color palette to use when drawing the World and its
	 * objects
	 */
	public World(double windowWidth, double windowHeight, ColorPalette palette) {
		
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.groundY = windowHeight - getGroundHeight();
		this.mountainManager = new MountainManager(
				windowWidth, 100, 100, groundY, palette);
		this.palette = palette;
	}

	/**
	 * Gets the width of the window this World occupies.
	 * @return the window width
	 */
	private double getWindowWidth() {
		return windowWidth;
	}

	/**
	 * Sets the width of the window this World occupies.
	 * @param windowWidth the window width
	 */
	public void setWindowWidth(double windowWidth) {
		this.windowWidth = windowWidth;
	}

	/**
	 * Gets the height of the window this World occupies.
	 * @return the window height
	 */
	public double getWindowHeight() {
		return windowHeight;
	}

	/**
	 * Sets the height of the window this World occupies.
	 * @param windowHeight the window height
	 */
	public void setWindowHeight(double windowHeight) {
		this.windowHeight = windowHeight;
	}

	/**
	 * Gets the list of Entities associated with this World.
	 * @return the World's objects
	 */
	public ArrayList<Entity> getObjects() {
		return objects;
	}

	/**
	 * Sets the list of Entities associated with this World.
	 * @param objects the World's new objects
	 */
	public void setObjects(ArrayList<Entity> objects) {
		this.objects = objects;
	}

	/**
	 * Gets the MountainManager used to handle mountain creation and drawing.
	 * @return this World's MountainManager
	 */
	private MountainManager getMountainManager() {
		return mountainManager;
	}

	/**
	 * Sets the MountainManager used to handle mountain creation and drawing.
	 * @param mountainManager this World's MountainManager
	 */
	public void setMountainManager(MountainManager mountainManager) {
		this.mountainManager = mountainManager;
	}

	/**
	 * Gets the ground's top y-coordinate.
	 * @return the ground's top y-coordinate
	 */
	public double getGroundY() {
		return groundY;
	}

	/**
	 * Sets the ground's top y-coordinate.
	 * @param groundY the ground's top y-coordinate
	 */
	public void setGroundY(double groundY) {
		this.groundY = groundY;
	}
	
	/**
	 * Gets the height of the rectangle used to represent the ground. Used for
	 * drawing purposes.
	 * @return the ground height
	 */
	public double getGroundHeight() {
		return groundHeight;
	}

	/**
	 * Sets the height of the rectangle used to represent the ground.
	 * @param groundHeight the ground height
	 */
	public void setGroundHeight(double groundHeight) {
		this.groundHeight = groundHeight;
	}

	/**
	 * Returns true if the world 'camera' is currently following the 
	 * <code>primaryRocket</code> horizontally, and false otherwise
	 * @return whether the world camera is centered on the rocket horizontally
	 */
	public boolean centerOnRocketHorizontally() {
		return centerOnRocketHorizontally;
	}

	/**
	 * Sets whether the world 'camera' should follow the
	 * <code>primaryRocket</code> horizontally.
	 * @param centerOnRocketHorizontally whether the world camera should center
	 * on the rocket horizontally
	 */
	public void setCenterOnRocketHorizontally(boolean centerOnRocketHorizontally) {
		this.centerOnRocketHorizontally = centerOnRocketHorizontally;
	}

	/**
	 * Returns true if the world 'camera' is currently following the 
	 * <code>primaryRocket</code> vertically, and false otherwise
	 * @return whether the world camera is centered on the rocket vertically
	 */
	public boolean centerOnRocketVertically() {
		return centerOnRocketVertically;
	}

	/**
	 * Sets whether the world 'camera' should follow the
	 * <code>primaryRocket</code> vertically.
	 * @param centerOnRocketVertically whether the world camera should center
	 * on the rocket vertically
	 */
	public void setCenterOnRocketVertically(boolean centerOnRocketVertically) {
		this.centerOnRocketVertically = centerOnRocketVertically;
	}

	/**
	 * Returns whether the given Rocket is touching the ground, based on
	 * y-coordinates only.
	 * @param rocket the Rocket to check for a collision with the ground
	 * @return true if the Rocket is touching the ground, false otherwise
	 */
	public boolean rocketTouchingGround(Rocket rocket) {
		
		return rocket.getY() + rocket.getHeight() >= getGroundY();
		
	}

	/**
	 * Gets the Rocket currently involved with simulation in this world.
	 * @return this World's primary Rocket
	 */
	public Rocket getPrimaryRocket() {
		return this.primaryRocket;
	}

	/**
	 * Sets the Rocket currently involved with simulation in this world.
	 * @param primaryRocket this World's new primary Rocket
	 */
	public void setPrimaryRocket(Rocket primaryRocket) {
		this.primaryRocket = primaryRocket;
	}

	/**
	 * Gets the color palette to use when drawing this World's objects
	 * @return this World's color palette
	 */
	private ColorPalette getPalette() {
		return this.palette;
	}

	/**
	 * Sets the color palette to use when drawing this World's objects
	 * @param palette this World's color palette
	 */
	public void setPalette(ColorPalette palette) {
		this.palette = palette;
		getMountainManager().setPalette(palette);
	}
	
	/**
	 * Updates all the objects of the World, and increments
	 * the world time.
	 * @param timeElapsed the time, in seconds, since the last tick
	 */
	public void tick(double timeElapsed) {
		
		if (rocketTouchingGround(getPrimaryRocket())) {
			getPrimaryRocket().stop();
		}
		
		for (Entity entity: getObjects()) {
			entity.tick(timeElapsed);
		}
		
	}
	
	/**
	 * Fills the Canvas with a sky color. Other landscape elements, like the
	 * ground and mountains, should be drawn over this background sky.
	 * @param gc the GraphicsContext used to draw the World
	 */
	public void drawSky(GraphicsContext gc) {
		
		gc.setFill(getPalette().getSkyColor());
		
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
		
		gc.setFill(getPalette().getGroundColor());
		double leftX = -gc.getTransform().getTx();
		double topY = getWindowHeight() - getGroundHeight();
		// Stretch the ground rectangle to the bottom of the screen
		double height = topY + getGroundHeight() - gc.getTransform().getTy();
		gc.fillRect(leftX, topY, getWindowWidth(), height);
		
		
	}
	
	/**
	 * Translates the given GraphicsContext so that the Rocket appears in the
	 * center of the window horizontally.
	 * @param gc the GraphicsContext used to draw the World
	 * @param center the Rocket to center the GraphicsContext on horizontally
	 */
	public void centerOnRocketHorizontally(GraphicsContext gc, Rocket center) {

		double xTranslate = centerOnRocketHorizontally() ? 
				-center.getX() - gc.getTransform().getTx() 
				+ getWindowWidth() / 2 : 0;
		
		gc.translate(xTranslate, 0);

	}
	
	/**
	 * Translates the given GraphicsContext so that the Rocket appears in the
	 * center of the window vertically.
	 * @param gc the GraphicsContext used to draw the World
	 * @param center the Rocket to center the GraphicsContext on vertically
	 */
	public void centerOnRocketVertically(GraphicsContext gc, Rocket center) {

		double yTranslate = centerOnRocketVertically()? 
				-center.getY() + center.getHeight() / 2 - 
				gc.getTransform().getTy() + getWindowHeight() / 2 : 0;
		
		gc.translate(0, yTranslate);

	}
	
	/**
	 * Checks whether the GraphicsContext should follow the Rocket horizontally
	 * or vertically and performs the necessary translations.
	 * @param gc the GraphicsContext to translate
	 */
	public void alignGraphicsContext(GraphicsContext gc) {

		if (centerOnRocketHorizontally()) {

			centerOnRocketHorizontally(gc, getPrimaryRocket());

		}

		if (centerOnRocketVertically()) {

			centerOnRocketVertically(gc, getPrimaryRocket());

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
