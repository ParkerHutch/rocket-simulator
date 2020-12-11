package userinterface;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Draws a pause/play button that allows the user to start or stop the 
 * simulation. The TogglePlayButton stores a <code>state</code> value, which
 * the simulator class reads to determine if the simulation should be paused.
 */
public class TogglePlayButton extends CustomButton {
	
	private String state = "PAUSE";
	
	private Color playButtonColor = Color.GREEN;
	private Color pauseButtonColor = Color.RED;
	
	private double symbolWidth;
	private double symbolHeight;
	
	/**
	 * Creates a TogglePlayButton with the given x and y offsets and 
	 * dimensions.
	 * @param xOffset the TogglePlayButton's x offset
	 * @param yOffset the TogglePlayButton's y offset
	 * @param width the TogglePlayButton's width
	 * @param height the TogglePlayButton's height
	 */
	public TogglePlayButton(double xOffset, double yOffset, double width, double height) {
		
		super(xOffset, yOffset, width, height);
		this.symbolWidth = width / 2;
		this.symbolHeight = symbolWidth;
	
	}
	
	/**
	 * Gets the color of the 'play' symbol.
	 * @return the play button color
	 */
	public Color getPlayButtonColor() {
		return playButtonColor;
	}

	/**
	 * Sets the color of the 'play' symbol.
	 * @param playButtonColor the play button color
	 */
	public void setPlayButtonColor(Color playButtonColor) {
		this.playButtonColor = playButtonColor;
	}

	/**
	 * Gets the color of the 'pause' symbol.
	 * @return the pause button color
	 */
	public Color getPauseButtonColor() {
		return pauseButtonColor;
	}

	/**
	 * Sets the color of the 'pause' symbol.
	 * @param pauseButtonColor the pause button color
	 */
	public void setPauseButtonColor(Color pauseButtonColor) {
		this.pauseButtonColor = pauseButtonColor;
	}

	/**
	 * Gets the width used for the 'pause' and 'play' symbols
	 * @return the symbol width
	 */
	public double getSymbolWidth() {
		return symbolWidth;
	}

	/**
	 * Sets the width used for the 'pause' and 'play' symbols
	 * @param symbolWidth the symbol width
	 */
	public void setSymbolWidth(double symbolWidth) {
		this.symbolWidth = symbolWidth;
	}

	/**
	 * Gets the height used for the 'pause' and 'play' symbols
	 * @return the symbol height
	 */
	public double getSymbolHeight() {
		return symbolHeight;
	}

	/**
	 * Sets the height used for the 'pause' and 'play' symbols
	 * @param symbolHeight the symbol height
	 */
	public void setSymbolHeight(double symbolHeight) {
		this.symbolHeight = symbolHeight;
	}

	/**
	 * Returns the textual representation of the symbol that the 
	 * TogglePlayButton is currently displaying. In other words, returns 
	 * "PAUSE" if the TogglePlayButton is currently displaying the pause 
	 * symbol, and "PLAY" if the TogglePlayButton is currently displaying the 
	 * play symbol
	 * @return the TogglePlayButton's state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state of the TogglePlayButton. This should correlate to the 
	 * symbol that the TogglePlayButton should be displaying. In other words,
	 * if the TogglePlayButton should display the pause symbol, 
	 * setState("PAUSE") should be called, and if it should display the play
	 * symbol, setState("PLAY") should be called.
	 * @param state the new state of the TogglePlayButton
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Draws the 'play' symbol within the bounds of the TogglePlayButton.
	 * @param gc the GraphicsContext used to draw the TogglePlayButton
	 */
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
	
	/**
	 * Draws the 'pause' symbol within the bounds of the TogglePlayButton.
	 * @param gc the GraphicsContext used to draw the TogglePlayButton
	 */
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
		
		if (getState().equals("PLAY")) {
				
			setState("PAUSE");

		} else {

			setState("PLAY");

		}
	}

}
