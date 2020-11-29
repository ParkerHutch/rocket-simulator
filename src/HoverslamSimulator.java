import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import rocket.UserControlledRocket;
import rocket.Vector2D;
import userinterface.CustomButton;
import userinterface.TogglePlayButton;
import userinterface.UserInterface;
import world.World;
import rocket.Rocket;

public class HoverslamSimulator extends Application {

	// TODO: I bet it'd be cool if I graphed altitude vs. time for the rocket
	
	private final int WIDTH = 900;
	private final int HEIGHT = 800;

	private Group root;
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

	@Override
	public void init() {
		
		root = new Group();
		world = new World(WIDTH, HEIGHT);
		world.setCenterOnRocketHorizontally(true);
		world.setCenterOnRocketVertically(true);

		
		// Initialize a rocket so that keyboard handling and the UI can be configured
		double rocketX = WIDTH  / 2;
		double rocketY = world.getGroundY() - 500;
		userRocket = new UserControlledRocket(rocketX, rocketY, 10, world.getGroundY());

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
				
				userInterface.tick(timeSinceLastUpdateSeconds);
				
				lastUpdate = now;

			}
		};
		
		userInterface = new UserInterface(0, 0, 100, 800, 
				userRocket, 
				world.getGroundY(), getAnimator());
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

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
			// TODO add LandCrashMessage buttons here

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
	
	public GraphicsContext getGraphicsContext() {
		return gc;
	}

	public void setGraphicsContext(GraphicsContext gc) {
		this.gc = gc;
	}

	public AnimationTimer getAnimator() {
		return animator;
	}

	public void setAnimator(AnimationTimer animator) {
		this.animator = animator;
	}
	
	public UserInterface getUserInterface() {
		return userInterface;
	}

	public void setUserInterface(UserInterface userInterface) {
		this.userInterface = userInterface;
	}

	public boolean shouldUpdateGame() {
		
		for (CustomButton button : getUserInterface().getButtons()) {
			
			if (button.getClass() == TogglePlayButton.class) {
				
				return ((TogglePlayButton) button).getState().equals("PAUSE");
				
			}
			
		}
		
		return true;
		
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}

	public void setMenuManager(MenuManager menuManager) {
		this.menuManager = menuManager;
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
			
			StackPane stackPane = new StackPane();
			stackPane.setStyle("-fx-background-color: " + getbackgroundColorHex());
	
			Scene mainMenuScene = new Scene(stackPane, getWidth(), getHeight());
			stage.setScene(mainMenuScene);
			
			Text title = new Text("Hoverslam Simulator");
			title.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 50));
			title.setTranslateY(-getHeight() / 4);
	
			Text author = new Text("Parker Hutchinson");
			author.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 50));
			author.setTranslateY(-getHeight() / 8);
	
			stackPane.getChildren().addAll(title, author);
			
			Button startComputerButton = new Button("Automatic Landing");
			startComputerButton.setPrefSize(200, 50);
			startComputerButton.setAlignment(Pos.CENTER);
			startComputerButton.setTranslateY(getHeight() / 8);
			startComputerButton.setStyle("-fx-font-size:18");
			startComputerButton.setOnAction(event -> startComputerSimulation(stage));
	
			Button startUserButton = new Button("Interactive Landing");
			startUserButton.setPrefSize(200, 50);
			startUserButton.setAlignment(Pos.CENTER);
			startUserButton.setTranslateY(2 * (getHeight() / 8));
			startUserButton.setOnAction(event -> startUserControlledSimulation(stage));
			startUserButton.setStyle("-fx-font-size:18");
			stackPane.getChildren().addAll(startComputerButton, startUserButton);
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

			world.getObjects().add(autoRocket);
	
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
			userRocket.getVelocity().setX(xVelocity);
			userRocket.setAcceleration(acceleration);
	
			world.getObjects().add(userRocket);

			stage.setScene(primaryScene);
			animator.start();
	
		}
	
	}

}


