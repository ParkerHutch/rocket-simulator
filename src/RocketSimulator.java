import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import rocket.UserControlledRocket;
import util.Vector2D;
import userinterface.CustomButton;
import userinterface.TogglePlayButton;
import userinterface.UserInterface;
import world.World;
import rocket.Rocket;
import design.ColorPalette;
import util.KeyboardHandler;

public class RocketSimulator extends Application {

	private final int WIDTH = 800;
	private final int HEIGHT = 700;

	private Group root;
	private Stage primaryStage;
	private Scene simulationScene;
	private GraphicsContext gc;
	private AnimationTimer animator;

	private UserInterface userInterface;
	
	private KeyboardHandler keyboardHandler;
	
	private MenuManager menuManager = new MenuManager(WIDTH, HEIGHT);
	
	private double maxSpeed = 250;
	private double initialFuel = 10;
	private double initialRocketHeight = 500;

	private UserControlledRocket userRocket;
	private World world;
	private boolean landingHandled = false;

	private Group landingSummary;

	private ColorPalette palette = ColorPalette.EARTH; // default color palette

	ObservableList<ColorPalette> paletteOptions = 
    			FXCollections.observableArrayList(
					ColorPalette.EARTH,
					ColorPalette.MARS,
					ColorPalette.NIGHT
	);

	@Override
	public void init() {
		
		root = new Group();

		// Create the World and center the camera on its Rocket
		world = new World(WIDTH, HEIGHT, getPalette());
		world.setCenterOnRocketHorizontally(true);
		world.setCenterOnRocketVertically(true);
		
		// Initialize a rocket so that keyboard handling can be configured
		double rocketX = WIDTH  / 2;
		userRocket = new UserControlledRocket(rocketX, 
			world.getGroundY() - getInitialRocketHeight(), 
			getInitialFuel(), 
			world.getGroundY());
		world.setPrimaryRocket(userRocket);
		keyboardHandler = new KeyboardHandler(userRocket);
		
		animator = new AnimationTimer() {
			
			long startTime;

			private long lastUpdate;

			@Override
			public void start() {
				
				startTime = System.nanoTime();
				lastUpdate = startTime;
				super.start();

			}
			
			public void clearScreen(GraphicsContext gc) {
				
				gc.clearRect(-gc.getTransform().getTx(), -gc.getTransform().getTy(),
						WIDTH, HEIGHT);
				
			}
			
			@Override
			public void handle(long now) {

				// SIMULATION LOOP

				clearScreen(gc);

				double timeSinceLastUpdateSeconds = (now - lastUpdate) / 1_000_000_000.0;
				
				world.draw(gc);
				userInterface.draw(gc);
				
				if (shouldUpdateSimulator()) {
					world.tick(timeSinceLastUpdateSeconds);
				}
				
				if (!world.getPrimaryRocket().isAirborne() && !isLandingHandled()) {

					/*
						If a Rocket just landed, show the landing summary
						and mark the landing as handled
					*/
					landingSummary = getMenuManager().getLandingSummary();
					root.getChildren().add(landingSummary);
					getUserInterface().getTimeIndicator().setForcePaused(true);
					setLandingHandled(true);
					
				}
				userInterface.tick(timeSinceLastUpdateSeconds);
				
				lastUpdate = now;

			}
		};
		
		userInterface = new UserInterface(0, 0, 100, HEIGHT, 
			userRocket, 
			world.getGroundY(),
			getInitialRocketHeight());
		
	}

	/**
	 * Reset objects so that everything is cleared for a new simulation.
	 */
	public void resetConfiguration() {

		world.getObjects().clear();
		getUserInterface().reset();
		if (root.getChildren().contains(landingSummary)) {
			root.getChildren().remove(landingSummary);
		}

	}

	@Override
	public void start(Stage stage) throws Exception {

		setPrimaryStage(stage);
		getPrimaryStage().setTitle("Rocket Simulator");

		setSimulationScene(new Scene(root, WIDTH, HEIGHT));

		getPrimaryStage().setWidth(WIDTH); 
		getPrimaryStage().setHeight(HEIGHT);

		Canvas canvas = new Canvas(getPrimaryStage().getWidth(), getPrimaryStage().getHeight());
		gc = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		
		getPrimaryStage().setScene(getSimulationScene());

		getMenuManager().showTitleScreen(getPrimaryStage());

		getPrimaryStage().show();

		addKeyboardHandling(getSimulationScene());

		// These buttons should be added last so they can receive events
		for (CustomButton button : getUserInterface().getButtons()) {

			root.getChildren().add(button);

		}
		
	}
	
	/**
	 * Adds keyboard event(ex. key press) handling to a scene
	 * @param scene the Scene to add keyboard event handling to
	 */
	private void addKeyboardHandling(Scene scene) {
		
		scene.setOnKeyPressed(keyboardHandler);
		scene.setOnKeyReleased(keyboardHandler);
	}
	
	/**
	 * Gets the GraphicsContext used to draw on the primary Stage's Canvas.
	 * @return the primary Stage's Canvas's GraphicsContext
	 */
	private GraphicsContext getGraphicsContext() {
		return gc;
	}

	/**
	 * Gets the AnimationTimer which runs the simulation loop.
	 * @return the simulation AnimationTimer
	 */
	private AnimationTimer getAnimator() {
		return animator;
	}

	/**
	 * Gets the stage used to display the simulation.
	 * @return the primary stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	/**
	 * Sets the stage used to display the simulation.
	 * @param primaryStage the new primary stage. 
	 */
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	/**
	 * Gets the Scene associated with the simulation.
	 * @return the simulation Scene
	 */
	public Scene getSimulationScene() {
		return this.simulationScene;
	}

	/**
	 * Sets the Scene associated with the simulation.
	 * @param simulationScene the new simulation Scene
	 */
	public void setSimulationScene(Scene simulationScene) {
		this.simulationScene = simulationScene;
	}

	/**
	 * Gets the UserInterface used to control various aspects of the simulation.
	 * @return the simulation's UserInterface
	 */
	private UserInterface getUserInterface() {
		return userInterface;
	}

	/**
	 * Gets the max speed the Rocket should have on initialization.
	 * @return the max initial speed of the Rocket
	 */
	public double getMaxSpeed() {
		return this.maxSpeed;
	}

	/**
	 * Sets the max speed the Rocket should have on initialization.
	 * @param maxSpeed the new max initial speed of the Rocket
	 */
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/**
	 * Gets the fuel amount the Rocket has(or had) on initialization.
	 * @return the Rocket's initial fuel amount
	 */
	public double getInitialFuel() {
		return this.initialFuel;
	}

	/**
	 * Sets the fuel amount the Rocket has on intialization.
	 * @param initialFuel the Rocket's new initial fuel amount
	 */
	public void setInitialFuel(double initialFuel) {
		this.initialFuel = initialFuel;
	}

	/**
	 * Gets the height of the Rocket when the simulation is(was)
	 * started. The height is the distance between the Rocket and the ground.
	 * @return the Rocket's initial height
	 */
	public double getInitialRocketHeight() {
		return this.initialRocketHeight;
	}

	/**
	 * Sets the Rocket's distance from the ground on initialization.
	 * @param initialRocketHeight the new initial height
	 */
	public void setInitialRocketHeight(double initialRocketHeight) {
		this.initialRocketHeight = initialRocketHeight;
	}

	/**
	 * Sets the status of the last landing handling.
	 * @param landingHandled whether the landing was handled
	 */
	public void setLandingHandled(boolean landingHandled) {
		this.landingHandled = landingHandled;
	}

	/**
	 * Returns the status of the last landing's handling.
	 * @return whether the last landing was handled
	 */
	public boolean isLandingHandled() {
		return landingHandled;
	}

	/**
	 * Determines if the simulator should update by checking the state of the
	 * UserInterface's TogglePlayButton. Will return true if the button's state
	 * is "PAUSE".
	 * @return whether the simulator should update
	 */
	private boolean shouldUpdateSimulator() {
		
		return getUserInterface().getTogglePlayButton().getState().equals("PAUSE");
		
	}

	/**
	 * Gets the primary MenuManager.
	 * @return the MenuManager
	 */
	private MenuManager getMenuManager() {
		return menuManager;
	}

	/**
	 * Gets the ColorPalette associated with the simulation.
	 * @return the simulation's color palette
	 */
	public ColorPalette getPalette() {
		return this.palette;
	}

	/**
	 * Sets the simulation's and world's color palette to the one given.
	 * @param palette the new color palette
	 */
	public void setPalette(ColorPalette palette) {
		this.palette = palette;
		world.setPalette(palette);
	}

	public static void main(String[] args) {

		launch(args);

	}

	/**
	 * A class for serving user menus before and after simulations are started.
	 */
	private class MenuManager {
	
		private int width;
		private int height;
	
		// Hex code for the menu's background color
		private String backgroundColorHex = "#0033A0";
		private String buttonColorHex = "#E4002B";
		private String textColorHex = "#8A8D8F";

		private String headingFontFamily = "Tahoma";
		private int headingFontSize = 50;
		private Font headingFont = Font.font(headingFontFamily, FontWeight.BOLD, FontPosture.REGULAR, headingFontSize);

		private String subheadingFontFamily = "Arial";
		private int subheadingFontSize = 26;
		private Font subheadingFont = Font.font(subheadingFontFamily, FontWeight.THIN, FontPosture.ITALIC, subheadingFontSize);

		private String buttonFontFamily = "Tahoma";
		private int buttonFontSize = 18;
		private Font buttonFont = Font.font(buttonFontFamily, FontWeight.BOLD, FontPosture.REGULAR, buttonFontSize);

		private String optionFontFamily = "Tahoma";
		private int optionFontSize = 24;
		private Font optionFont = Font.font(optionFontFamily, FontWeight.BOLD, FontPosture.REGULAR, optionFontSize);
		
		private String paletteSelectorFontFamily = "Tahoma";
		private double paletteSelectorFontSize = 18;

		private double buttonWidth = 210;
		private double buttonHeight = 50;

		/**
		 * Creates a MenuManager object configured for the given dimensions
		 * @param width the width of the menu's window
		 * @param height the height of the menu's window
		 */
		public MenuManager(int width, int height) {
			
			this.width = width;
			this.height = height;

			setButtonWidth(Math.max(buttonWidth, width / 4.0));
			
		}

		/**
		 * Gets the width of the menu's window
		 * @return the width
		 */
		private int getWidth() {
			return width;
		}
	
		/**
		 * Gets the height of the menu's window
		 * @return the height
		 */
		private int getHeight() {
			return height;
		}

		/**
		 * Gets the width associated with every normal button in the menus.
		 * @return the normal button width
		 */
		private double getButtonWidth() {

			return buttonWidth;

		}

		/**
		 * Sets the width associated with every normal button in the menus.
		 * @param buttonWidth the new button width
		 */
		private void setButtonWidth(double buttonWidth) {

			this.buttonWidth = buttonWidth;

		}

		/**
		 * Gets the width of the menu's small buttons.
		 * @return the small button width
		 */
		private double getSmallButtonWidth() {

			return getButtonWidth() / 2;

		}

		/**
		 * Gets the height of every button in the menus.
		 * @return the button height
		 */
		private double getButtonHeight() {
			return buttonHeight;
		}

		/**
		 * Sets the height used by every button in the menus.
		 * @param buttonHeight the new button height
		 */
		private void setButtonHeight(double buttonHeight) {
			this.buttonHeight = buttonHeight;
		}
	
		/**
		 * Sets the background color of the menu
		 * @param colorCode the Hex code for the background color
		 */
		public void setBackgroundColorHex(String colorCode) {
			this.backgroundColorHex = colorCode;
		}
		
		/**
		 * Gets the background color of the menu.
		 * @return the background color's Hex code
		 */
		public String getBackgroundColorHex() {
			return backgroundColorHex;
		}

		/**
		 * Sets the fill color for all Buttons.
		 * @param colorCode the Hex code for the Button color
		 */
		public void setButtonColorHex(String colorCode) {
			this.buttonColorHex = colorCode;
		}
		
		/**
		 * Gets the fill color used for all Buttons.
		 * @return the Button fill color's Hex code
		 */
		public String getButtonColorHex() {
			return buttonColorHex;
		}

		/**
		 * Sets the fill color for all text.
		 * @param colorCode the Hex code for the text color
		 */
		public void setTextColorHex(String colorCode) {
			this.textColorHex = colorCode;
		}
		
		/**
		 * Gets the fill color used for all text.
		 * @return the text fill color's Hex code
		 */
		public String getTextColorHex() {
			return textColorHex;
		}
		
		/**
		 * Shows a title screen menu on the stage given. The menu includes
		 * buttons to start an interactive landing or an automatic one.
		 * @param stage the stage to show the menu on
		 */
		public void showTitleScreen(Stage stage) {
			
			double textButtonMargin = 30; // vertical distance between author and first button
			double buttonMargin = 10; // vertical distance between buttons

			resetConfiguration();
			
			stage.sizeToScene();

			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: " + getBackgroundColorHex());
	
			Scene mainMenuScene = new Scene(stackPane, getWidth(), getHeight());
			stage.setScene(mainMenuScene);
			
			Text title = new Text("Rocket Simulator");
			title.setFont(headingFont);
			title.setFill(Color.web(getTextColorHex()));
			title.setTranslateY(-getHeight() / 4);
	
			Text author = new Text("Parker Hutchinson");
			author.setFont(subheadingFont);
			author.setFill(Color.web(getTextColorHex()));
			author.setTranslateY(
				title.getTranslateY() + title.getLayoutBounds().getHeight());
	
			stackPane.getChildren().addAll(title, author);
			
			Button startComputerButton = new Button("Automatic Landing");
			startComputerButton.setPrefSize(getButtonWidth(), getButtonHeight());
			startComputerButton.setTranslateY(
				author.getTranslateY() + author.getLayoutBounds().getHeight() + textButtonMargin);
			startComputerButton.setFont(buttonFont);
			startComputerButton.setStyle(
				"-fx-background-color: " + getButtonColorHex() + ";" + 
				"-fx-text-fill: " + getTextColorHex() + ";");
			startComputerButton.setOnAction(event -> startComputerSimulation(stage));
	
			Button startUserButton = new Button("Interactive Landing");
			startUserButton.setPrefSize(getButtonWidth(), getButtonHeight());
			startUserButton.setTranslateY(
				startComputerButton.getTranslateY() + startComputerButton.getPrefHeight() + buttonMargin);
			startUserButton.setOnAction(event -> startUserControlledSimulation(stage));
			startUserButton.setFont(buttonFont);
			startUserButton.setStyle(
				"-fx-background-color: " + getButtonColorHex() + ";" + 
				"-fx-text-fill: " + getTextColorHex() + ";");
			stackPane.getChildren().addAll(startComputerButton, startUserButton);

			Button optionsMenuButton = new Button("Options");
			optionsMenuButton.setPrefSize(getSmallButtonWidth(), getButtonHeight());
			optionsMenuButton.setTranslateY(
				startUserButton.getTranslateY() + startUserButton.getPrefHeight() + buttonMargin);
			optionsMenuButton.setOnAction(event -> showOptionsMenu(stage));
			optionsMenuButton.setFont(buttonFont);
			optionsMenuButton.setStyle(
				"-fx-background-color: " + getButtonColorHex() + ";" +
				"-fx-text-fill: " + getTextColorHex() + ";");
			stackPane.getChildren().add(optionsMenuButton);

		}

		/**
		 * Returns a button of the given dimensions that returns the user to 
		 * the main menu when clicked.
		 * @param width the width of the button
		 * @param height the height of the button
		 * @return a 'Back to Main Menu' button
		 */
		public Button getBackToMainMenuButton(double width, double height) {

			Button backToMainMenu = new Button("Back to Main Menu");
			backToMainMenu.setPrefSize(width, height);
			backToMainMenu.setOnAction(event -> showTitleScreen(getPrimaryStage()));
			backToMainMenu.setFont(buttonFont);
			backToMainMenu.setStyle(
				"-fx-background-color: " + getButtonColorHex() + ";" +
				"-fx-text-fill: " + getTextColorHex() + ";");

			return backToMainMenu;

		}
		
		/**
		 * Shows a menu allowing the user to customize options associated with
		 * the simulation.
		 * @param stage the stage to show the menu on
		 */
		public void showOptionsMenu(Stage stage) {

			double textButtonMargin = 20;
			double titleOptionMargin = 20;
			double optionBackToMainMenuMargin = 50;

			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: " + getBackgroundColorHex());
	
			Scene optionsMenuScene = new Scene(stackPane, getWidth(), getHeight());
			stage.setScene(optionsMenuScene);
			
			Text title = new Text("Options");
			title.setFont(headingFont);
			title.setTranslateY(-getHeight() / 4);

			stackPane.getChildren().add(title);
			
			Text paletteSelectorText = new Text("Color Palette");
			paletteSelectorText.setFont(optionFont);
			paletteSelectorText.setTranslateY(
				title.getTranslateY() + title.getLayoutBounds().getHeight() + titleOptionMargin);
			paletteSelectorText.setTranslateX(-paletteSelectorText.getLayoutBounds().getWidth() / 2 - textButtonMargin / 2);
			stackPane.getChildren().add(paletteSelectorText);

			ComboBox paletteSelector = new ComboBox<ColorPalette>(paletteOptions);
			paletteSelector.setMinWidth(100);
			paletteSelector.setMinHeight(paletteSelectorText.getLayoutBounds().getHeight());
			paletteSelector.getSelectionModel().select(getPalette());
			paletteSelector.setOnAction((Event event) -> {
				setPalette((ColorPalette) paletteSelector.getSelectionModel().getSelectedItem());
			});
			paletteSelector.setStyle(
				"-fx-font: " + paletteSelectorFontSize + "px \"" + paletteSelectorFontFamily + "\";"
			);
			
			paletteSelector.setTranslateX(paletteSelector.getMinWidth()/2 + textButtonMargin / 2);
			paletteSelector.setTranslateY(paletteSelectorText.getTranslateY());

			stackPane.getChildren().add(paletteSelector);

			Button backToMainMenu = getBackToMainMenuButton(200, 50);
			backToMainMenu.setTranslateY(
				paletteSelectorText.getTranslateY() + 
				paletteSelectorText.getLayoutBounds().getHeight() + 
				optionBackToMainMenuMargin);

			stackPane.getChildren().add(backToMainMenu);

		}

		/**
		 * Gets a round rectangle containing textualized information about the
		 * most recent Rocket landing.
		 * @return a Group containing all relevant Nodes that contain the 
		 * landing information
		 */
		public Group getLandingSummary() {

			boolean acceptableVelocity = 
				world.getPrimaryRocket().getLandingVelocity() < world.getPrimaryRocket().getAcceptableLandingVelocity();
			boolean acceptableAngle = 
				Math.abs(world.getPrimaryRocket().getDirection() - 90) <= world.getPrimaryRocket().getLandingAngleMargin();
			boolean crash = !(acceptableVelocity && acceptableAngle);

			// distance between largest element and the box edge
			double boxMargin = 16; 
			double boxY = HEIGHT / 4 - 25; // top y coordinate of the box
			double textMargin = 5;
			
			String landingMessage = crash ? "Crash" : "Successful Landing";

			Text landingMessageText = new Text(landingMessage);
			landingMessageText.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 26));
			landingMessageText.setFill(Color.web(getTextColorHex()));
			landingMessageText.setStyle("-fx-text-fill: " + getTextColorHex() + ";");
			landingMessageText.setTranslateY(boxY + 20 + boxMargin);
			landingMessageText.setTranslateX(WIDTH / 2 - 
			landingMessageText.getLayoutBounds().getWidth() / 2);

			Text velocityTextBox = new Text("Velocity: " + (int) world.getPrimaryRocket().getLandingVelocity());
			velocityTextBox.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 20));
			velocityTextBox.setTranslateY(
				landingMessageText.getTranslateY() + 
				landingMessageText.getLayoutBounds().getHeight() + textMargin);
			velocityTextBox.setTranslateX(WIDTH / 2 - velocityTextBox.getLayoutBounds().getWidth() / 2);
			if (!acceptableVelocity) {
				velocityTextBox.setFill(Color.YELLOW);
			} else {
				velocityTextBox.setFill(Color.web(getTextColorHex()));
			}
			
			Text angleTextBox = new Text("Angle: " + (int) world.getPrimaryRocket().getDirection() + "\u00B0");
			angleTextBox.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 20));
			angleTextBox.setTranslateY(
				velocityTextBox.getTranslateY() + 
				velocityTextBox.getLayoutBounds().getHeight() + textMargin);
			angleTextBox.setTranslateX(WIDTH / 2 - angleTextBox.getLayoutBounds().getWidth() / 2);
			if (!acceptableAngle) {
				angleTextBox.setFill(Color.YELLOW);
			} else {
				angleTextBox.setFill(Color.web(getTextColorHex()));
			}

			double fuelConsumedProportion = 
				(getInitialFuel() - world.getPrimaryRocket().getFuel()) / getInitialFuel();
			
			Text fuelUsedText = new Text(
				"Fuel Consumed: " + (int) (fuelConsumedProportion * 100) + "%"
			);
			fuelUsedText.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 20));
			fuelUsedText.setFill(Color.web(getTextColorHex()));
			fuelUsedText.setTranslateY(
				angleTextBox.getTranslateY() + 
				angleTextBox.getLayoutBounds().getHeight() + textMargin
			);
			fuelUsedText.setTranslateX(WIDTH / 2 - 
				fuelUsedText.getLayoutBounds().getWidth() / 2
			);

			Button backToMainMenu = getBackToMainMenuButton(getButtonWidth(), getButtonHeight());
			backToMainMenu.setTranslateY(
				fuelUsedText.getTranslateY() + 
				fuelUsedText.getLayoutBounds().getHeight()
			);
			backToMainMenu.setTranslateX(WIDTH / 2 - getButtonWidth() / 2);
			
			double backToMainMenuBottomY = backToMainMenu.getTranslateY() + getButtonHeight();
			double backgroundBoxWidth = Math.max(
				Math.max(landingMessageText.getLayoutBounds().getWidth(),
					fuelUsedText.getLayoutBounds().getWidth()), 
				Math.max(velocityTextBox.getLayoutBounds().getWidth(), backToMainMenu.getWidth())
				) + boxMargin;
			double backgroundBoxHeight = backToMainMenuBottomY - boxY + boxMargin;

			Rectangle backgroundBox = new Rectangle(WIDTH / 2 - backgroundBoxWidth / 2, 
														boxY, 
														backgroundBoxWidth, 
														backgroundBoxHeight);
			backgroundBox.setArcWidth(10); // round edges
			backgroundBox.setArcHeight(10); // round edges
			backgroundBox.setStrokeWidth(3);
			backgroundBox.setStroke(Color.BLACK); // TODO use declared variables here
			backgroundBox.setFill(Color.web(getBackgroundColorHex()));

			Group landingSummary = new Group();
			landingSummary.getChildren().addAll(
				backgroundBox, landingMessageText, velocityTextBox, angleTextBox, 
				fuelUsedText, backToMainMenu);
			return landingSummary;

		}

		/**
		 * Starts an automatic rocket landing by creating a computer-controlled
		 * rocket, adding it to the world, reverting the stage's scene to the
		 * simulation scene, and starting the animator.
		 * @param stage the menu's stage
		 */
		public void startComputerSimulation(Stage stage) {
	
			// Create the rocket
			double rocketX = WIDTH  / 2;
			double xVelocity = Math.random() * getMaxSpeed() * 2 - getMaxSpeed();
			Vector2D acceleration = new Vector2D(0.0, World.GRAVITY);
			Rocket autoRocket = new Rocket(rocketX, 
				world.getGroundY() - getInitialRocketHeight(),
				getInitialFuel(), 
				world.getGroundY());
			autoRocket.getVelocity().setX(xVelocity);
			autoRocket.setAcceleration(acceleration);
			setLandingHandled(false);

			world.getObjects().add(autoRocket);
			world.setPrimaryRocket(autoRocket);
			userInterface.calibrateElements(autoRocket);

			stage.setScene(getSimulationScene()); 
			animator.start();
	
		}
		
		/**
		 * Starts a user-controlled rocket landing by setting velocity values
		 * for the user-controlled rocket, adding the rocket to the world, 
		 * reverting the stage's scene to the simulation scene, and starting 
		 * the animator.
		 * @param stage the menu's stage
		 */
		public void startUserControlledSimulation(Stage stage) {
	
			// Create the rocket
			double xVelocity = Math.random() * getMaxSpeed() * 2 - getMaxSpeed();
			Vector2D acceleration = new Vector2D(0.0, World.GRAVITY);
			double rocketX = WIDTH  / 2;
			
			userRocket.reset(rocketX, world.getGroundY() - getInitialRocketHeight(), getInitialFuel());
			
			userRocket.getVelocity().setX(xVelocity);
			
			userRocket.setAcceleration(acceleration);

			setLandingHandled(false);
	
			world.getObjects().add(userRocket);
			world.setPrimaryRocket(userRocket);
			userInterface.calibrateElements(userRocket);

			stage.setScene(getSimulationScene());
			animator.start();
	
		}
	
	}

}


