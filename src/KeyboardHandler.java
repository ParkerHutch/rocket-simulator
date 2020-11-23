import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import rocket.UserControlledRocket;

/**
 * A class for handling keyboard input
 */
public class KeyboardHandler implements EventHandler<KeyEvent> {

	String currentKeysPressed = "";
	
	UserControlledRocket userRocket;

	KeyboardHandler(UserControlledRocket userRocket) {
		
		this.userRocket = userRocket;
	
	}

	public void handle(KeyEvent arg0) {

		String code = arg0.getCode().toString().toUpperCase();
		
		if (arg0.getEventType() == KeyEvent.KEY_PRESSED) {
			
			
			if ("WAD".contains(code) && !currentKeysPressed.contains(code)) {
				
				currentKeysPressed += code;
				
			}
			
			if (code.equals("I")) {
				
				System.out.println("target: " + userRocket.getTargetAngle());
				System.out.println("direction: " + userRocket.getDirection());
				
			}

		}
		
		if (arg0.getEventType() == KeyEvent.KEY_RELEASED) {
			
			currentKeysPressed = currentKeysPressed.replace(code, "");
			
			if ("AD".contains(code)) {
				
				// stop rocket rotation
				userRocket.setTargetAngle(userRocket.getDirection());
				userRocket.setShouldFireRCS(false);
				
			}
			
		}

		if (currentKeysPressed.contains("W")) {

			userRocket.setShouldFireEngines(true);

		} else {
			
			userRocket.setShouldFireEngines(false);
			
		}

		
		if (currentKeysPressed.contains("A") || currentKeysPressed.contains("D")) {
			
			userRocket.setShouldFireRCS(true);
			
			if (currentKeysPressed.contains("A")) {
				
				userRocket.setTargetAngle(userRocket.getDirection() + 2 * userRocket.getTurnRate());
				
			}
			
			if (currentKeysPressed.contains("D")) {

				userRocket.setTargetAngle(userRocket.getDirection() - 2 * userRocket.getTurnRate());

			}
			
		} else {
			
			userRocket.setShouldFireRCS(false);
			// stop rocket rotation
			userRocket.setTargetAngle(userRocket.getDirection());
			userRocket.setShouldFireRCS(false);
			
			
		}
		
	}

	public UserControlledRocket getUserRocket() {
		return userRocket;
	}

	public void setUserRocket(UserControlledRocket userRocket) {
		this.userRocket = userRocket;
	}

}