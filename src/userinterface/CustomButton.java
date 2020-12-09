package userinterface;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import util.Entity;

public abstract class CustomButton extends Button {

	private double x = 0; // Middle x coordinate
	private double y = 0;
	private double xOffset = 0;
	private double yOffset = 0;
	
	private Color baseColor = Color.WHITE;
	private Color strokeColor = Color.BLACK;
	
	public CustomButton(double xOffset, double yOffset, double width, double height) {
		
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		setPrefSize(width, height);
		
		this.setOnMouseClicked(event -> onClick());
		this.setOpacity(0); // hides the node while allowing inputs to register
		
	}

	public CustomButton(double xOffset, double yOffset, double width, double height, 
			Color baseColor, Color strokeColor) {

		this.xOffset = xOffset;
		this.yOffset = yOffset;
		setWidth(width);
		setHeight(height);
		this.baseColor = baseColor;
		this.strokeColor = strokeColor;
		this.setOnMouseClicked(event -> onClick());
		this.setOpacity(0); // hides the node while allowing inputs to register

	}
	
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double getCenterY() {
		
		return getY() + getHeight() / 2;
		
	}

	public double getxOffset() {
		return xOffset;
	}

	public void setxOffset(double xOffset) {
		this.xOffset = xOffset;
	}

	public double getyOffset() {
		return yOffset;
	}

	public void setyOffset(double yOffset) {
		this.yOffset = yOffset;
	}

	public Color getBaseColor() {
		return baseColor;
	}

	public void setBaseColor(Color baseColor) {
		this.baseColor = baseColor;
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}
	
	public void alignWith(double x, double y) {
		
		setLayoutX(getxOffset() - getWidth() / 2);
		setLayoutY(getyOffset());
		
		// Update the drawing coordinates TODO Name them as such?
		setX(x + getxOffset());
		setY(y + getyOffset());
		
	}
	
	public void alignWith(Entity target) {

		alignWith(target.getX(), target.getY());

	}

	abstract void onClick();
	
	abstract void draw(GraphicsContext gc);
	
}
