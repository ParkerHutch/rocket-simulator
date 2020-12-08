package userinterface;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import rocket.Entity;

public class TimeIndicator extends Entity {
	
	private boolean forcePaused = false;
	private boolean paused = false;
	private double internalTime = 0;
	
	private double clockHandAngle = 90;
	private double clockRadius;
	private double knobRadius = 3;
	
	private double timeLabelWidth = 30; 
	private double timeLabelHeight = 15;
	
	public TimeIndicator(double xOffset, double yOffset, 
			double width, double height) {
		
		super(xOffset, yOffset, width, height);
		this.timeLabelHeight = height / 4 + 5;
		this.clockRadius = (height - timeLabelHeight) / 2;
		
	}

	public TimeIndicator(double xOffset, double width, double height) {
		
		super(xOffset, 0, width, height);
		this.timeLabelHeight = height / 4 + 5;
		this.clockRadius = (height - timeLabelHeight) / 2;
		
	}

	public double getInternalTime() {
		return internalTime;
	}

	public void setInternalTime(double internalTime) {
		this.internalTime = internalTime;
	}


	public boolean isForcePaused() {
		return this.forcePaused;
	}

	public boolean getForcePaused() {
		return this.forcePaused;
	}

	public void setForcePaused(boolean forcePaused) {
		this.forcePaused = forcePaused;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public double getClockHandAngle() {
		return clockHandAngle;
	}

	public void setClockHandAngle(double clockHandAngle) {
		this.clockHandAngle = clockHandAngle;
	}

	public double getClockRadius() {
		return clockRadius;
	}

	public void setClockRadius(double clockRadius) {
		this.clockRadius = clockRadius;
	}

	public double getKnobRadius() {
		return knobRadius;
	}

	public void setKnobRadius(double knobRadius) {
		this.knobRadius = knobRadius;
	}

	public double getTimeLabelWidth() {
		return timeLabelWidth;
	}

	public void setTimeLabelWidth(double timeLabelWidth) {
		this.timeLabelWidth = timeLabelWidth;
	}

	public double getTimeLabelHeight() {
		return timeLabelHeight;
	}

	public void setTimeLabelHeight(double timeLabelHeight) {
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
	
	private void drawKnob(GraphicsContext gc) {
		
		double x1 = getX();
		double y1 = getY() + (getHeight() - getTimeLabelHeight()) / 2;
		
		gc.setFill(Color.RED);
		gc.fillArc(x1 - getKnobRadius(), y1 - getKnobRadius(), 
				getKnobRadius() * 2, getKnobRadius() * 2, 
				0, 360, ArcType.CHORD);
		
	}
	
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
		
		gc.setFont(new Font(20)); // TODO use tahoma font here
		
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
