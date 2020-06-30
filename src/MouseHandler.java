import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

class MouseHandler implements EventHandler<MouseEvent> {
	
		HoverslamSimulator simulator;

		MouseHandler(HoverslamSimulator simulator) {
			this.simulator = simulator;
		}

		public void handle(MouseEvent arg0) {
			//System.out.println(arg0.getEventType());
			/*
			mouseX = arg0.getX() + getGameCamera().getxOffset();
			mouseY = arg0.getY() + getGameCamera().getyOffset();
			if (arg0.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
				
				if (arg0.getButton() == MouseButton.PRIMARY) {
					// if the left click button is pressed
					for (Tile[] column : map) {
						for (Tile t : column) {
							if (t.isSelected()) {
								// TODO: Put the below code in a "destroy" method in tile
								// The method shouldn't destroy the block if it is invincible
								// so maybe call it "attemptDestroy"?
								if (t.getState().equals("SOLID") && !(t.getType().equals("INVINCIBLE"))) {
									// if the block is solid, destroy it and add it to the inventory

									// add the block to the player's inventory
									player.getInventory().put(t.getType(), player.getInventory().get(t.getType()) + 1);
									t.setState("AIR");
									// t.setColor(Color.TRANSPARENT);
									t.setColor(Color.SKYBLUE); // make this a method inside the tile,
									// so that the tile always returns a color based on its type
								} else if (!t.getType().equals("INVINCIBLE")
										&& (player.getInventory().get(getHeldTileType()) > 0)) {
									// if the spot selected isn't solid or invincible(probably air tile), create a
									// tile there and remove 1 of it from the inventory

									player.getInventory().put(getHeldTileType(),
											player.getInventory().get(getHeldTileType()) - 1);
									t.setType(getHeldTileType());
								}
							}
						}
					}
				}
				if (arg0.getButton() == MouseButton.SECONDARY) {
					// if the right click button is pressed
					buildModeEnabled = !buildModeEnabled;
				}

				// leftClickPressed = arg0.isPrimaryButtonDown();
				// rightClickPressed = arg0.isSecondaryButtonDown();
			}
			*/
		}

	}