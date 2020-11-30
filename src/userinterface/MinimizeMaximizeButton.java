package userinterface;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MinimizeMaximizeButton extends CustomButton {
	
	private boolean maximized = true;
	
	private double symbolWidth;
	private double symbolHeight;
	
	private Color symbolColor = Color.RED;
	
	public MinimizeMaximizeButton(double xOffset, double yOffset, 
			double width, double height) {
		super(xOffset, yOffset, width, height);

		this.symbolWidth = width / 2;
		this.symbolHeight = 4 * height / 5;
	
	}
	
	public boolean isMaximized() {
		return maximized;
	}

	public void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}

	public double getSymbolWidth() {
		return symbolWidth;
	}

	public void setSymbolWidth(double symbolWidth) {
		this.symbolWidth = symbolWidth;
	}

	public double getSymbolHeight() {
		return symbolHeight;
	}

	public void setSymbolHeight(double symbolHeight) {
		this.symbolHeight = symbolHeight;
	}

	public Color getSymbolColor() {
		return symbolColor;
	}

	public void setSymbolColor(Color symbolColor) {
		this.symbolColor = symbolColor;
	}

	public void drawPlaySymbol(GraphicsContext gc) {

		double[] triangleXPoints = new double[] {

				getX() - getSymbolWidth() / 2, getX() - getSymbolWidth() / 2, getX() + getSymbolWidth() / 2

		};

		double[] triangleYPoints = new double[] {

				getCenterY() + getSymbolHeight() / 2, getCenterY() - getSymbolHeight() / 2, getCenterY()

		};

		gc.fillPolygon(triangleXPoints, triangleYPoints, 3);

	}

	public void drawTriangle(GraphicsContext gc, boolean pointedLeft) {

		double [] triangleXPoints;
		
		if (pointedLeft) {
			
			triangleXPoints = new double[] {

				getX() + getSymbolWidth() / 2, getX() + getSymbolWidth() / 2, 
				getX() - getSymbolWidth() / 2

			};
			
		} else {
			
			triangleXPoints = new double[] {

					getX() - getSymbolWidth() / 2, getX() - getSymbolWidth() / 2, 
					getX() + getSymbolWidth() / 2

			};
			
		}
		
		double[] triangleYPoints = new double[] {

				getCenterY() + getSymbolHeight() / 2, getCenterY() - getSymbolHeight() / 2, getCenterY()

		};

		gc.setFill(getSymbolColor());
		gc.fillPolygon(triangleXPoints, triangleYPoints, 3);

	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
		gc.setFill(getBaseColor());
		gc.fillRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);
		gc.setStroke(getStrokeColor());
		gc.strokeRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);
		
		if (isMaximized()) {
			
			drawTriangle(gc, true);
			
		} else {
			
			drawTriangle(gc, false);
			
		}

	}

	@Override
	void onClick() {
		
		setMaximized(!isMaximized());
		
	}

}
