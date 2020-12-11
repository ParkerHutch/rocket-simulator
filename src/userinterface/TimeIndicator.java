package userinterface;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import util.Entity;

/**
 * A clock element that shows the user how much time has passed since the start
 * of the simulation.
 */
public class TimeIndicator extends Entity {
	
	private boolean forcePaused = false;
	private boolean paused = false;
	private double internalTime = 0;
	
	private double clockHandAngle = 90;
	private double clockRadius;
	private double knobRadius = 3;
	
	private double timeLabelWidth = 30; 
	private double timeLabelHeight = 15;
	
	/**
	 * Creates a TimeIndicator with the given x and y offsets and dimensions.
	 * @param xOffset the TimeIndicator's x offset
	 * @param yOffset the TimeIndicator's y offset
	 * @param width the TimeIndicator's width
	 * @param height the TimeIndicator's height
	 */
	public TimeIndicator(double xOffset, double yOffset, 
			double width, double height) {
		
		super(xOffset, yOffset, width, height);
		this.timeLabelHeight = height / 4 + 5;
		this.clockRadius = (height - timeLabelHeight) / 2;
		
	}

	/**
	 * Creates a TimeIndicator with the given x offset and dimensions.
	 * @param xOffset the TimeIndicator's x offset
	 * @param width the TimeIndicator's width
	 * @param height the TimeIndicator's height
	 */
	public TimeIndicator(double xOffset, double width, double height) {
		
		super(xOffset, 0, width, height);
		this.timeLabelHeight = height / 4 + 5;
		this.clockRadius = (height - timeLabelHeight) / 2;
		
	}

	/**
	 * Gets the time that has elapsed since the simulation was started.
	 * @return the TimeIndicator's internal time
	 */
	public double getInternalTime() {
		return internalTime;
	}

	/**
	 * Sets the stored time value of the TimeIndicator.
	 * @param internalTime the TimeIndicator's new internal time value
	 */
	public void setInternalTime(double internalTime) {
		this.internalTime = internalTime;
	}

	/**
	 * Returns whether the TimeIndicator has been force paused. This property 
	 * allows the TimeIndicator to be paused even if the TogglePlayButton state 
	 * implies that it should be unpaused.
	 * @return the TimeIndicator's force paused state
	 */
	public boolean isForcePaused() {
		return this.forcePaused;
	}

	/**
	 * Sets the forcePaused state of the TimeIndicator. This property allows
	 * the TimeIndicator to be paused even if the TogglePlayButton state 
	 * implies that it should be unpaused.
	 * @param forcePaused the new force paused state of the TimeIndicator
	 */
	public void setForcePaused(boolean forcePaused) {
		this.forcePaused = forcePaused;
	}

	/**
	 * Returns whether the TimeIndicator is currently paused.
	 * @return the TimeIndicator's paused state
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * Sets whether the TimeIndicator is currently paused.
	 * @param paused the new paused state of the TimeIndicator
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	/**
	 * Gets the current angle of the clock hand
	 * @return the clock hand angle
	 */
	private double getClockHandAngle() {
		return clockHandAngle;
	}

	/**
	 * Sets the current angle of the clock hand
	 * @param clockHandAngle the new clock hand angle
	 */
	private void setClockHandAngle(double clockHandAngle) {
		this.clockHandAngle = clockHandAngle;
	}

	/**
	 * Gets the radius of the TimeIndicator's clock face.
	 * @return the clock face radius
	 */
	private double getClockRadius() {
		return clockRadius;
	}

	/**
	 * Sets the radius of the TimeIndicator's clock face.
	 * @param clockRadius the new clock face radius
	 */
	private void setClockRadius(double clockRadius) {
		this.clockRadius = clockRadius;
	}

	/**
	 * Gets the radius of the TimeIndicator's clock's central knob.
	 * @return the clock's knob radius
	 */
	private double getKnobRadius() {
		return knobRadius;
	}

	/**
	 * Sets the radius of the TimeIndicator's clock's central knob.
	 * @param knobRadius the clock's new knob radius
	 */
	private void setKnobRadius(double knobRadius) {
		this.knobRadius = knobRadius;
	}

	/**
	 * Gets the width of the rectangle bounding the time label text.
	 * @return the time label width
	 */
	private double getTimeLabelWidth() {
		return timeLabelWidth;
	}

	/**
	 * Sets the width of the rectangle bounding the time label text.
	 * @param timeLabelWidth the new time label width
	 */
	private void setTimeLabelWidth(double timeLabelWidth) {
		this.timeLabelWidth = timeLabelWidth;
	}

	/**
	 * Gets the height of the rectangle bounding the time label text.
	 * @return the time label height
	 */
	private double getTimeLabelHeight() {
		return timeLabelHeight;
	}

	/**
	 * Sets the height of the rectangle bounding the time label text.
	 * @param timeLabelHeight the new time label height
	 */
	private void setTimeLabelHeight(double timeLabelHeight) {
		this.timeLabelHeight = timeLabelHeight;
	}

	/**
	 * Draws a clock hand from the center of the TimeIndicator extending 
	 * to the radius defined by clockRadius. the x and y coordinates of the end
	 * of the clock hand are determined by the cosine and sine values of the
	 * clock hand's angle, which is incremented in the 'tick' method of the
	 * TimeIndicator.
	 * @param gc the GraphicsContext to draw the clock hand on
	 */
	private void drawClockHand(GraphicsContext gc) {
		
		double x1 = getX();
		//double y1 = getY() + getHeight() / 2 - getTimeLabelHeight();
		double y1 = getY() + (getHeight() - getTimeLabelHeight()) / 2;
		double x2 = getX() + Math.cos(Math.toRadians(getClockHandAngle())) 
				* getClockRadius();
		
		double y2 = (y1) + 
				Math.sin(Math.toRadians(getClockHandAngle())) 
				* getClockRadius();
		
		gc.setStroke(Color.BLACK);
		gc.strokeLine(x1, y1, x2, y2);
		
	}
	
	/**
	 * Draws the circle representing the clock face.
	 * @param gc The GraphicsContext used to draw the TimeIndicator
	 */
	private void drawClockFace(GraphicsContext gc) {
		
		double x1 = getX();
		double y1 = getY() + (getHeight() - getTimeLabelHeight()) / 2;
		
		gc.setFill(Color.WHITE);
		gc.fillArc(x1 - getClockRadius(), y1 - getClockRadius(), 
				getClockRadius() * 2, getClockRadius() * 2, 
				0, 360, ArcType.CHORD);
		
		gc.setStroke(Color.BLACK);
		gc.strokeArc(x1 - getClockRadius(), y1 - getClockRadius(), 
				getClockRadius() * 2, getClockRadius() * 2, 
				0, 360, ArcType.CHORD);
		
	}
	
	/**
	 * Draws a circle representing the clock's central knob.
	 * @param gc the GraphicsContext used to draw the TimeIndicator
	 */
	private void drawKnob(GraphicsContext gc) {
		
		double x1 = getX();
		double y1 = getY() + (getHeight() - getTimeLabelHeight()) / 2;
		
		gc.setFill(Color.RED);
		gc.fillArc(x1 - getKnobRadius(), y1 - getKnobRadius(), 
				getKnobRadius() * 2, getKnobRadius() * 2, 
				0, 360, ArcType.CHORD);
		
	}
	
	/**
	 * Draws a textual representation of the time elapsed with a rectangle
	 * behind it.
	 * @param gc the GraphicsContext used to draw the TimeIndicator
	 */
	private void drawTimeLabel(GraphicsContext gc) {
		
		gc.setFill(Color.WHITE);
		
		gc.fillRoundRect(getX() - getWidth() / 2, 
				getY() + getHeight() - getTimeLabelHeight(), 
				getWidth(), getTimeLabelHeight(), 10, 10);
		
		gc.setStroke(Color.BLACK);
		gc.strokeRoundRect(getX() - getWidth() / 2, 
				getY() + getHeight() - getTimeLabelHeight(), 
				getWidth(), getTimeLabelHeight(), 10, 10);
		
		gc.setFill(Color.BLACK);
		
		gc.setTextAlign(TextAlignment.CENTER);
		
		gc.setFont(new Font(20));
		
		gc.setTextBaseline(VPos.CENTER);
		
		gc.fillText("T=" + (Math.round(getInternalTime())), getX(), 
				getY() + getHeight() - getTimeLabelHeight() / 2 - 1);

	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
		if (isVisible()) {
			
			// Draw base of the UI element
			gc.setLineWidth(3);
			gc.setFill(Color.LIGHTGRAY);
			gc.fillRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);
			gc.setStroke(Color.BLACK);
			gc.strokeRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);

			drawClockFace(gc);
			drawClockHand(gc);
			drawKnob(gc);
			drawTimeLabel(gc);
			
		}

	}
	
	@Override
	public void tick(double timeElapsed) {

		if (!isPaused() && !isForcePaused()) {

			setClockHandAngle(getClockHandAngle() + (timeElapsed * 360));
			
			if (getInternalTime() > 999) {
				
				setInternalTime(0);
				
			}
			
			setInternalTime(getInternalTime() + timeElapsed);
		} 

	}
	
}
