package userinterface;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import util.Entity;

public class LandCrashMessage extends Entity {

	private CustomButton backToMenuButton;
	private CustomButton playAgainButton;
	private Color baseColor = Color.LIGHTGRAY;
	
	public LandCrashMessage(double xOffset, double yOffset, 
			double width, double height) {
		
		super(xOffset, yOffset, width, height);
		
	}
	
	public Color getBaseColor() {
		return baseColor;
	}

	public void setBaseColor(Color baseColor) {
		this.baseColor = baseColor;
	}

	public CustomButton getBackToMenuButton() {
		return backToMenuButton;
	}

	public void setBackToMenuButton(CustomButton backToMenuButton) {
		this.backToMenuButton = backToMenuButton;
	}

	public CustomButton getPlayAgainButton() {
		return playAgainButton;
	}

	public void setPlayAgainButton(CustomButton playAgainButton) {
		this.playAgainButton = playAgainButton;
	}

	@Override
	public void draw(GraphicsContext gc) {
		
		gc.fillRoundRect(getX() - getWidth() / 2, 
				getY(), 
				getWidth(), getHeight(), 10, 10);
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick(double timeElapsed) {
		// TODO Auto-generated method stub
		
	}

}
