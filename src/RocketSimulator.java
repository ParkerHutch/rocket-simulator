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
import rocket.Vector2D;
import userinterface.CustomButton;
import userinterface.TogglePlayButton;
import userinterface.UserInterface;
import world.World;
import rocket.Rocket;
import design.ColorPalette;

public class RocketSimulator extends Application {

	private final int WIDTH = 800;
	private final int HEIGHT = 750;

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

		world = new World(WIDTH, HEIGHT, getPalette());
		world.setCenterOnRocketHorizontally(true);
		world.setCenterOnRocketVertically(true);
		
		// Initialize a rocket so that keyboard handling and the UI can be configured
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

				clearScreen(gc);

				double timeSinceLastUpdateSeconds = (now - lastUpdate) / 1_000_000_000.0;
				
				world.draw(gc);
				userInterface.draw(gc);
				
				if (shouldUpdateGame()) {
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
	
	private GraphicsContext getGraphicsContext() {
		return gc;
	}

	private AnimationTimer getAnimator() {
		return animator;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public Scene getSimulationScene() {
		return this.simulationScene;
	}

	public void setSimulationScene(Scene simulationScene) {
		this.simulationScene = simulationScene;
	}

	private UserInterface getUserInterface() {
		return userInterface;
	}

	public double getMaxSpeed() {
		return this.maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public double getInitialFuel() {
		return this.initialFuel;
	}

	public void setInitialFuel(double initialFuel) {
		this.initialFuel = initialFuel;
	}

	public double getInitialRocketHeight() {
		return this.initialRocketHeight;
	}

	public void setInitialRocketHeight(double initialRocketHeight) {
		this.initialRocketHeight = initialRocketHeight;
	}

	public void setLandingHandled(boolean landingHandled) {
		this.landingHandled = landingHandled;
	}

	public boolean isLandingHandled() {
		return landingHandled;
	}

	private boolean shouldUpdateGame() {
		
		return getUserInterface().getTogglePlayButton().getState().equals("PAUSE");
		
	}

	private MenuManager getMenuManager() {
		return menuManager;
	}

	public ColorPalette getPalette() {
		return this.palette;
	}

	public void setPalette(ColorPalette palette) {
		this.palette = palette;
		world.setPalette(palette);
	}

	public static void main(String[] args) {

		launch(args);

	}

	private class MenuManager {
	
		private int width;
		private int height;
	
		// Hex code for the menu's background color
		private String backgroundColorHex = "#4BCDFF";
		private String buttonColorHex = "#82E0AA";

		private String headingFontFamily = "Tahoma";
		private int headingFontSize = 50;
		private Font headingFont = Font.font(headingFontFamily, FontWeight.BOLD, FontPosture.REGULAR, headingFontSize);

		private String buttonFontFamily = "Tahoma";
		private int buttonFontSize = 18;
		private Font buttonFont = Font.font(buttonFontFamily, buttonFontSize);

		private String optionFontFamily = "Tahoma";
		private int optionFontSize = 24;
		private Font optionFont = Font.font(optionFontFamily, FontWeight.BOLD, FontPosture.REGULAR, optionFontSize);
		
		private String paletteSelectorFontFamily = "Tahoma";
		private double paletteSelectorFontSize = 18;

		private double buttonWidth = 200;
		private double buttonHeight = 50;

		/**
		 * Creates a MenuManager object configured for the given dimensions
		 * @param width the width of the menu's window
		 * @param height the height of the menu's window
		 */
		public MenuManager(int width, int height) {
			
			this.width = width;
			this.height = height;

			
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

		private double getButtonWidth() {

			return buttonWidth;

		}

		private void setButtonWidth(double buttonWidth) {

			this.buttonWidth = buttonWidth;

		}

		private double getSmallButtonWidth() {

			return getButtonWidth() / 2;

		}

		private double getButtonHeight() {
			return buttonHeight;
		}

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
		 * Shows a title screen menu on the stage given. The menu includes
		 * buttons to start an interactive landing or an automatic one.
		 * @param stage the stage to show the menu on
		 */
		public void showTitleScreen(Stage stage) {
			
			double textMargin = 10; // vertical distance between title and author
			double textButtonMargin = 20; // vertical distance between author and first button
			double buttonMargin = 10; // vertical distance between buttons

			resetConfiguration();
			
			stage.sizeToScene();

			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: " + getBackgroundColorHex());
	
			Scene mainMenuScene = new Scene(stackPane, getWidth(), getHeight());
			stage.setScene(mainMenuScene);
			
			Text title = new Text("Rocket Simulator");
			title.setFont(headingFont);
			title.setTranslateY(-getHeight() / 4);
	
			Text author = new Text("Parker Hutchinson");
			author.setFont(headingFont);
			author.setTranslateY(
				title.getTranslateY() + title.getLayoutBounds().getHeight() + textMargin);
	
			stackPane.getChildren().addAll(title, author);
			
			Button startComputerButton = new Button("Automatic Landing");
			startComputerButton.setPrefSize(getButtonWidth(), getButtonHeight());
			startComputerButton.setAlignment(Pos.CENTER);
			startComputerButton.setTranslateY(
				author.getTranslateY() + author.getLayoutBounds().getHeight() + textButtonMargin);
			startComputerButton.setFont(buttonFont);
			startComputerButton.setStyle("-fx-background-color: " + getButtonColorHex() + ";");
			startComputerButton.setOnAction(event -> startComputerSimulation(stage));
	
			Button startUserButton = new Button("Interactive Landing");
			startUserButton.setPrefSize(getButtonWidth(), getButtonHeight());
			startUserButton.setAlignment(Pos.CENTER); // TODO remove?
			startUserButton.setTranslateY(
				startComputerButton.getTranslateY() + startComputerButton.getPrefHeight() + buttonMargin);
			startUserButton.setOnAction(event -> startUserControlledSimulation(stage));
			startUserButton.setFont(buttonFont);
			startUserButton.setStyle("-fx-background-color: " + getButtonColorHex() + ";");
			stackPane.getChildren().addAll(startComputerButton, startUserButton);

			Button optionsMenuButton = new Button("Options");
			optionsMenuButton.setPrefSize(getSmallButtonWidth(), getButtonHeight());
			optionsMenuButton.setAlignment(Pos.CENTER);
			optionsMenuButton.setTranslateY(
				startUserButton.getTranslateY() + startUserButton.getPrefHeight() + buttonMargin);
			optionsMenuButton.setOnAction(event -> showOptionsMenu(stage));
			optionsMenuButton.setFont(buttonFont);
			optionsMenuButton.setStyle("-fx-background-color: " + getButtonColorHex() + ";");
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
			backToMainMenu.setStyle("-fx-background-color: " + getButtonColorHex() + ";");

			return backToMainMenu;

		}
		
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
				+ "-fx-background-color: " + getButtonColorHex() + ";");

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
				velocityTextBox.setFill(Color.RED);
			} else {
				velocityTextBox.setFill(Color.BLACK);
			}
			
			Text angleTextBox = new Text("Angle: " + (int) world.getPrimaryRocket().getDirection() + "\u00B0");
			angleTextBox.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 20));
			angleTextBox.setTranslateY(
				velocityTextBox.getTranslateY() + 
				velocityTextBox.getLayoutBounds().getHeight() + textMargin);
			angleTextBox.setTranslateX(WIDTH / 2 - angleTextBox.getLayoutBounds().getWidth() / 2);
			if (!acceptableAngle) {
				angleTextBox.setFill(Color.RED);
			} else {
				angleTextBox.setFill(Color.BLACK);
			}

			double fuelConsumedProportion = 
				(getInitialFuel() - world.getPrimaryRocket().getFuel()) / getInitialFuel();
			
			Text fuelUsedText = new Text(
				"Fuel Consumed: " + (int) (fuelConsumedProportion * 100) + "%"
			);
			fuelUsedText.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 20));
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
			backgroundBox.setStroke(Color.BLACK);
			backgroundBox.setFill(Color.WHITE);

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


