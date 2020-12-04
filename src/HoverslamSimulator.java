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

public class HoverslamSimulator extends Application {

	private final int WIDTH = 800;
	private final int HEIGHT = 750;

	private Group root;
	private Stage primaryStage;
	private Scene primaryScene;
	private Canvas canvas;
	private GraphicsContext gc;
	private AnimationTimer animator;

	private UserInterface userInterface;
	
	private KeyboardHandler keyboardHandler;
	
	private MenuManager menuManager = new MenuManager(WIDTH, HEIGHT);
	
	double maxSpeed = 250;

	private UserControlledRocket userRocket;
	private Rocket autoRocket;
	private World world;
	boolean landingHandled = false;

	Group autoLandingMenu;
	Group userLandingMenu = new Group();


	private ColorPalette palette = ColorPalette.EARTH;

	ObservableList<ColorPalette> paletteOptions = 
    			FXCollections.observableArrayList(
					ColorPalette.EARTH,
					ColorPalette.MARS,
					ColorPalette.NIGHT
	);

	@Override
	public void init() {
		
		root = new Group();

		autoLandingMenu = getMenuManager().getAutoLandingMenu();
		root.getChildren().add(autoLandingMenu);

		world = new World(WIDTH, HEIGHT, getPalette());
		world.setCenterOnRocketHorizontally(true);
		world.setCenterOnRocketVertically(true);
		
		// Initialize a rocket so that keyboard handling and the UI can be configured
		double rocketX = WIDTH  / 2;
		double rocketY = world.getGroundY() - 500;
		userRocket = new UserControlledRocket(rocketX, rocketY, 10, world.getGroundY());
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
				
				if (!world.getPrimaryRocket().isAirborne() && !landingHandled) {

					if (world.getPrimaryRocket().getClass() == UserControlledRocket.class) {

						userLandingMenu = getMenuManager().getUserLandingMenu();
						root.getChildren().add(userLandingMenu);
						userLandingMenu.toFront();
						userLandingMenu.setVisible(true);

					} else {
						autoLandingMenu.toFront();
						autoLandingMenu.setVisible(true);
					}
					getUserInterface().getTogglePlayButton().setState("PLAY");
					
					landingHandled = true;
					
				}
				userInterface.tick(timeSinceLastUpdateSeconds);
				
				lastUpdate = now;

			}
		};
		
		userInterface = new UserInterface(0, 0, 100, HEIGHT, 
				userRocket, 
				world.getGroundY(), getAnimator());
		
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void resetConfiguration() {

		world.getObjects().clear();
		getUserInterface().reset();
		autoLandingMenu.setVisible(false);
		if (root.getChildren().contains(userLandingMenu)) {
			root.getChildren().remove(userLandingMenu);
		}

	}

	@Override
	public void start(Stage stage) throws Exception {

		primaryStage = stage;
		primaryStage.setTitle("Hoverslam Simulation");

		primaryScene = new Scene(root, WIDTH, HEIGHT);

		primaryStage.setWidth(WIDTH); 
		primaryStage.setHeight(HEIGHT);

		canvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());
		gc = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		
		primaryStage.setScene(primaryScene);

		getMenuManager().showTitleScreen(primaryStage);

		primaryStage.show();

		addKeyboardHandling(primaryScene);
		addMouseHandling(primaryScene);
		

		// NOTE: It's important for these buttons to be added to the root
		// after the Canvas: they won't receive MouseEvents otherwise.
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
	 * Adds mouse event handling to the given Scene
	 * @param scene the Scene to add mouse event handling to
	 */
	private void addMouseHandling(Scene scene) {
		// adds mouseEvent handling to the given scene
		MouseHandler mouseHandler = new MouseHandler(this);
		scene.setOnMouseMoved(mouseHandler);
		scene.setOnMouseDragged(mouseHandler);
		scene.setOnMousePressed(mouseHandler);
		scene.setOnMouseClicked(mouseHandler);
		scene.setOnMouseReleased(mouseHandler);
	}
	
	private GraphicsContext getGraphicsContext() {
		return gc;
	}

	private AnimationTimer getAnimator() {
		return animator;
	}

	private UserInterface getUserInterface() {
		return userInterface;
	}

	private boolean shouldUpdateGame() {
		
		for (CustomButton button : getUserInterface().getButtons()) {
			
			if (button.getClass() == TogglePlayButton.class) {
				
				return ((TogglePlayButton) button).getState().equals("PAUSE");
				
			}
			
		}

		return true;
		
	}

	private MenuManager getMenuManager() {
		return menuManager;
	}

	public ColorPalette getPalette() {
		return this.palette;
	}

	public void setPalette(ColorPalette palette) {
		this.palette = palette;
		// Set the palette for all relevant objects
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
	
		/**
		 * Sets the background color of the menu
		 * @param colorCode the Hex code for the background color
		 */
		public void setbackgroundColorHex(String colorCode) {
			this.backgroundColorHex = colorCode;
		}
		
		/**
		 * Gets the background color of the menu
		 * @return the background color's Hex code
		 */
		public String getbackgroundColorHex() {
			return backgroundColorHex;
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
			stackPane.setStyle("-fx-background-color: " + getbackgroundColorHex());
	
			Scene mainMenuScene = new Scene(stackPane, getWidth(), getHeight());
			stage.setScene(mainMenuScene);
			
			Text title = new Text("Hoverslam Simulator");
			title.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 50));
			title.setTranslateY(-getHeight() / 4);
	
			Text author = new Text("Parker Hutchinson");
			author.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 50));
			author.setTranslateY(
				title.getTranslateY() + title.getLayoutBounds().getHeight() + textMargin);
	
			stackPane.getChildren().addAll(title, author);
			
			Button startComputerButton = new Button("Automatic Landing");
			startComputerButton.setPrefSize(200, 50);
			startComputerButton.setAlignment(Pos.CENTER);
			startComputerButton.setTranslateY(
				author.getTranslateY() + author.getLayoutBounds().getHeight() + textButtonMargin);
			startComputerButton.setStyle("-fx-font-size:18");
			startComputerButton.setOnAction(event -> startComputerSimulation(stage));
	
			Button startUserButton = new Button("Interactive Landing");
			startUserButton.setPrefSize(200, 50);
			startUserButton.setAlignment(Pos.CENTER);
			startUserButton.setTranslateY(
				startComputerButton.getTranslateY() + startComputerButton.getPrefHeight() + buttonMargin);
			startUserButton.setOnAction(event -> startUserControlledSimulation(stage));
			startUserButton.setStyle("-fx-font-size:18");
			stackPane.getChildren().addAll(startComputerButton, startUserButton);

			Button optionsMenuButton = new Button("Options");
			optionsMenuButton.setPrefSize(200, 50);
			optionsMenuButton.setAlignment(Pos.CENTER);
			optionsMenuButton.setTranslateY(
				startUserButton.getTranslateY() + startUserButton.getPrefHeight() + buttonMargin);
			optionsMenuButton.setOnAction(event -> showOptionsMenu(stage));
			optionsMenuButton.setStyle("-fx-font-size:18");
			stackPane.getChildren().add(optionsMenuButton);

		}
		

		public void showOptionsMenu(Stage stage) {

			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: " + getbackgroundColorHex());
	
			Scene optionsMenuScene = new Scene(stackPane, getWidth(), getHeight());
			stage.setScene(optionsMenuScene);
			
			Text title = new Text("Options");
			title.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 50));
			title.setTranslateY(-getHeight() / 4);

			stackPane.getChildren().add(title);

			Button backToMainMenu = new Button("Back to Main Menu");
			backToMainMenu.setTranslateY(-getHeight() / 8);
			backToMainMenu.setPrefSize(200, 50);
			backToMainMenu.setAlignment(Pos.CENTER);
			backToMainMenu.setOnAction(event -> showTitleScreen(getPrimaryStage()));

			stackPane.getChildren().add(backToMainMenu);

			ComboBox paletteSelector = new ComboBox<ColorPalette>(paletteOptions);
			paletteSelector.setMinWidth(200);
			paletteSelector.setOnAction((Event event) -> {
				setPalette((ColorPalette) paletteSelector.getSelectionModel().getSelectedItem());
			});
			stackPane.getChildren().add(paletteSelector);

			// TODO the setPalette() function should probably call the setPalette() function
			// for every relevant object

		}

		public Group getAutoLandingMenu() {

			Group autoLandingMenu = new Group();
			autoLandingMenu.setVisible(false);

			double boxY = HEIGHT / 4 - 25; // top y coordinate of the box
			double buttonWidth = 160;
			double buttonHeight = 50;
			double backgroundBoxWidth = 200;
			
			Text autoLandingMessage = new Text("Successful Landing");
			autoLandingMessage.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 16));
			autoLandingMessage.setTranslateY(boxY + 25);
			autoLandingMessage.setTranslateX(WIDTH / 2 - 
				autoLandingMessage.getLayoutBounds().getWidth() / 2);

			Button backToMainMenu = new Button("Back to Main Menu");
			backToMainMenu.setTranslateY(
				autoLandingMessage.getTranslateY() + autoLandingMessage.getLayoutBounds().getHeight());
			backToMainMenu.setTranslateX(WIDTH / 2 - buttonWidth / 2);
			backToMainMenu.setPrefSize(buttonWidth, 50);
			backToMainMenu.setAlignment(Pos.CENTER);
			backToMainMenu.setOnAction(event -> showTitleScreen(getPrimaryStage()));
			
			double backToMainMenuBottomY = backToMainMenu.getTranslateY() + buttonHeight;
			double backgroundBoxHeight = backToMainMenuBottomY - boxY + 8;

			Rectangle backgroundBox = new Rectangle(WIDTH / 2 - backgroundBoxWidth / 2, 
														boxY, 
														backgroundBoxWidth, 
														backgroundBoxHeight);
			backgroundBox.setArcWidth(10); // round edges
			backgroundBox.setArcHeight(10); // round edges
			backgroundBox.setStrokeWidth(3);
			backgroundBox.setStroke(Color.BLACK);
			backgroundBox.setFill(Color.WHITE);

			autoLandingMenu.getChildren().addAll(backgroundBox, autoLandingMessage, backToMainMenu);
			return autoLandingMenu;

		}

		public Group getUserLandingMenu() {

			boolean acceptableVelocity = 
				userRocket.getLandingVelocity() < userRocket.getAcceptableLandingVelocity();
			boolean acceptableAngle = 
				Math.abs(userRocket.getDirection() - 90) <= userRocket.getLandingAngleMargin();
			boolean crash = !(acceptableVelocity && acceptableAngle);
			
			Group userLandingMenu = new Group();

			double boxY = HEIGHT / 4 - 25; // top y coordinate of the box
			// approximate width of the text message, must be manually adjusted
			double buttonWidth = 160;
			double buttonHeight = 50;
			double backgroundBoxWidth = 200;
			double lineWidth = 3;
			double textMargin = 5;
			
			String landingMessage = crash ? "Crash" : "Successful Landing";

			Text landingTypeTextBox = new Text(landingMessage);
			landingTypeTextBox.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 16));
			landingTypeTextBox.setTranslateY(boxY + lineWidth + textMargin + 10);
			landingTypeTextBox.setTranslateX(WIDTH / 2 - landingTypeTextBox.getLayoutBounds().getWidth() / 2);
			if (crash) {
				landingTypeTextBox.setFill(Color.RED);
			} else {
				landingTypeTextBox.setFill(Color.BLACK);
			}

			Text velocityTextBox = new Text("Velocity: " + (int) userRocket.getLandingVelocity());
			velocityTextBox.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 16));
			velocityTextBox.setTranslateY(
				landingTypeTextBox.getTranslateY() + 
				landingTypeTextBox.getLayoutBounds().getHeight() + textMargin);
			velocityTextBox.setTranslateX(WIDTH / 2 - velocityTextBox.getLayoutBounds().getWidth() / 2);
			if (!acceptableVelocity) {
				velocityTextBox.setFill(Color.RED);
			} else {
				velocityTextBox.setFill(Color.BLACK);
			}
			
			Text angleTextBox = new Text("Angle: " + (int) userRocket.getDirection() + "\u00B0");
			angleTextBox.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 16));
			angleTextBox.setTranslateY(
				velocityTextBox.getTranslateY() + 
				velocityTextBox.getLayoutBounds().getHeight() + textMargin);
			angleTextBox.setTranslateX(WIDTH / 2 - angleTextBox.getLayoutBounds().getWidth() / 2);
			if (!acceptableAngle) {
				angleTextBox.setFill(Color.RED);
			} else {
				angleTextBox.setFill(Color.BLACK);
			}

			Button backToMainMenu = new Button("Back to Main Menu");
			backToMainMenu.setTranslateY(
				angleTextBox.getTranslateY() + 
				angleTextBox.getLayoutBounds().getHeight() - 5);
			backToMainMenu.setTranslateX(WIDTH / 2 - buttonWidth / 2);
			backToMainMenu.setPrefSize(buttonWidth, buttonHeight);
			backToMainMenu.setAlignment(Pos.CENTER);
			backToMainMenu.setOnAction(event -> showTitleScreen(getPrimaryStage()));
			
			double backToMainMenuBottomY = backToMainMenu.getTranslateY() + buttonHeight;
			
			double backgroundBoxHeight = backToMainMenuBottomY - boxY + textMargin;

			Rectangle backgroundBox = new Rectangle(WIDTH / 2 - backgroundBoxWidth / 2, 
														boxY, 
														backgroundBoxWidth, 
														backgroundBoxHeight);
			backgroundBox.setArcWidth(10); // round edges
			backgroundBox.setArcHeight(10); // round edges
			backgroundBox.setStrokeWidth(lineWidth);
			backgroundBox.setStroke(Color.BLACK);
			backgroundBox.setFill(Color.WHITE);

			userLandingMenu.getChildren().addAll(
				backgroundBox, landingTypeTextBox, velocityTextBox, angleTextBox, backToMainMenu);

			return userLandingMenu;

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
			double rocketY = world.getGroundY() - 500;
			double xVelocity = Math.random() * maxSpeed * 2 - maxSpeed;
			Vector2D acceleration = new Vector2D(0.0, World.GRAVITY);
			autoRocket = new Rocket(rocketX, rocketY, 10, world.getGroundY());
			autoRocket.getVelocity().setX(xVelocity);
			autoRocket.setAcceleration(acceleration);
			landingHandled = false;

			world.getObjects().add(autoRocket);
			world.setPrimaryRocket(autoRocket);
			userInterface.focusElements(autoRocket);

			stage.setScene(primaryScene); 
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
			double xVelocity = Math.random() * maxSpeed * 2 - maxSpeed;
			Vector2D acceleration = new Vector2D(0.0, World.GRAVITY);
			double rocketX = WIDTH  / 2;
			double rocketY = world.getGroundY() - 500;
			
			userRocket.reset(rocketX, rocketY, 10);
			
			userRocket.getVelocity().setX(xVelocity);
			
			userRocket.setAcceleration(acceleration);

			landingHandled = false;
	
			world.getObjects().add(userRocket);
			world.setPrimaryRocket(userRocket);
			userInterface.focusElements(userRocket);

			stage.setScene(primaryScene);
			animator.start();
	
		}
	
	}

}


