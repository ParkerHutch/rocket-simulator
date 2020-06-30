import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * A class for handling keyboard input
 */
public class KeyboardHandler implements EventHandler<KeyEvent> {

	HoverslamSimulator simulator;
	
	boolean toggleAnimator = true;
	
	KeyboardHandler(HoverslamSimulator simulator) {
		this.simulator = simulator;
	}

	private HoverslamSimulator getSimulator() {
		return simulator;
	}

	private void setSimulator(HoverslamSimulator simulator) {
		this.simulator = simulator;
	}

	public void handle(KeyEvent arg0) {
		
		if (arg0.getEventType() == KeyEvent.KEY_PRESSED) {
			
			String code = arg0.getCode().toString().toUpperCase();
			System.out.println(code);
			
			if (code.equals("SPACE")) {
				
				if (toggleAnimator) {
					getSimulator().getAnimator().stop();
					toggleAnimator = !toggleAnimator;
				} else {
					
					getSimulator().getAnimator().start();
					toggleAnimator = !toggleAnimator;
					
				}
				
			}
			
		}
			/*
			if (arg0.getEventType() == KeyEvent.KEY_PRESSED) {
				
				String code = arg0.getCode().toString().toUpperCase();
				
				if (code.equals("W")) {
					
					if (getPlayer().isJumpReady() && !getPlayer().isInsidePortal()) {
						
						getPlayer().setyVelocity(-4);
						getPlayer().setJumpReady(false);
					}
					
					
				} else if (code.equals("A")) {
					
					player.setxVelocity(player.getxVelocity() - 2);
					
				} else if (code.equals("D")) {
					
					player.setxVelocity(player.getxVelocity() + 2);
					
				} else if (code.equals("DIGIT1")) {
					
					getLevelManager().switchLevel(0);
				}
				
				if (code.equals("F")) {
					
					System.out.println(player);
					
				}
				if (code.equals("P")) {
					
					menuManager.showMainMenu();
					//animator.stop();
					
				}
				if (code.equals("R")) {
					
					animator.start();
					
				} 
				if (code.equals("SPACE")) {
					
					getLevelManager().getCurrentLevel().restartLevel();
					
					
				}
				
			}*/ 
		}

	}