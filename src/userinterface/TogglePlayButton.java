package userinterface;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TogglePlayButton extends CustomButton {
	
	private String state = "PAUSE";
	private AnimationTimer animator;
	
	private Color playButtonColor = Color.GREEN;
	private Color pauseButtonColor = Color.RED;
	
	private double symbolWidth;
	private double symbolHeight;
	
	public TogglePlayButton(double xOffset, double yOffset, double width, double height, 
			AnimationTimer animator) {
		
		super(xOffset, yOffset, width, height);
		this.animator = animator;
		this.symbolWidth = width / 2;
		this.symbolHeight = symbolWidth;
	
	}
	
	public Color getPlayButtonColor() {
		return playButtonColor;
	}

	public void setPlayButtonColor(Color playButtonColor) {
		this.playButtonColor = playButtonColor;
	}

	public Color getPauseButtonColor() {
		return pauseButtonColor;
	}

	public void setPauseButtonColor(Color pauseButtonColor) {
		this.pauseButtonColor = pauseButtonColor;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	private AnimationTimer getAnimator() {
		return animator;
	}

	public void setAnimator(AnimationTimer animator) {
		this.animator = animator;
	}
	
	public void drawPlaySymbol(GraphicsContext gc) {
		
		double [] triangleXPoints = new double[] {
				
				getX() - getSymbolWidth() / 2,
				getX() - getSymbolWidth() / 2,
				getX() + getSymbolWidth() / 2
				
		};

		double[] triangleYPoints = new double[] {

				getCenterY() + getSymbolHeight() / 2,
				getCenterY() - getSymbolHeight() / 2,
				getCenterY() 
				
		};
		
		gc.setFill(getPlayButtonColor());
		gc.fillPolygon(triangleXPoints, triangleYPoints, 3);
		
	}
	
	public void drawPauseSymbol(GraphicsContext gc) {
		
		gc.setFill(getPauseButtonColor());
		
		double pauseBarWidth = getSymbolWidth() / 3;
		gc.fillRect(getX() - getSymbolWidth() / 2, getCenterY() - getSymbolHeight() / 2, 
				pauseBarWidth, getSymbolHeight());
		
		gc.fillRect(getX() + getSymbolWidth() / 2 - pauseBarWidth, getCenterY() - getSymbolHeight() / 2, 
				pauseBarWidth, getSymbolHeight());
		
		
	}
	@Override
	public void draw(GraphicsContext gc) {
		
		if (isVisible()) {
			
			gc.setFill(getBaseColor());
			gc.fillRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);
			gc.setStroke(getStrokeColor());
			gc.strokeRoundRect(getX() - getWidth() / 2, getY(), getWidth(), getHeight(), 10, 10);

			if (getState().equals("PLAY")) {

				drawPlaySymbol(gc);

			} else if (getState().equals("PAUSE")) {

				drawPauseSymbol(gc);

			}
			
		}
		
	}

	@Override
	void onClick() {
		
		if (getAnimator() != null) {
			
			if (getState().equals("PLAY")) {
				
				//getAnimator().start();
				setState("PAUSE");
				//draw(getGraphicsContext());

			} else {

				//getAnimator().stop();
				setState("PLAY");
				//draw(getGraphicsContext());

			}
		} 
		
	}

}
