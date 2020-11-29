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
import userinterface.menu.MenuManager;
import world.World;
import rocket.Rocket;

public class HoverslamSimulator extends Application {

	// TODO: I bet it'd be cool if I graphed altitude vs. time for the rocket
	
	private final int WIDTH = 600;
	private final int HEIGHT = 600;

	private Group root;
	private Scene primaryScene;
	private Canvas canvas;
	private GraphicsContext gc;
	private AnimationTimer animator;

	private UserInterface userInterface;
	
	private KeyboardHandler keyboardHandler;
	
	private MenuManager menuManager;
	
	double maxSpeed = 250;

	private UserControlledRocket userRocket;
	private Rocket autoRocket;

	@Override
	public void init() {
		
		root = new Group();
		World world = new World(WIDTH, HEIGHT);
		world.setCenterOnRocketHorizontally(true);
		world.setCenterOnRocketVertically(true);

		// Create the rocket
		double rocketX = WIDTH  / 2;
		double rocketY = world.getGroundY() - 500;
		//double xVelocity = Math.random() * maxSpeed * 2 - maxSpeed;
		//Vector2D acceleration = new Vector2D(0.0, World.GRAVITY);
		userRocket = new UserControlledRocket(rocketX, rocketY, 10, world.getGroundY());
		//autoRocket = new Rocket(rocketX, rocketY, 10, world.getGroundY());

		keyboardHandler = new KeyboardHandler(userRocket);
		/*
		userRocket.getVelocity().setX(xVelocity);
		userRocket.setAcceleration(acceleration);
		autoRocket.getVelocity().setX(xVelocity);
		autoRocket.setAcceleration(acceleration);

		world.getObjects().add(userRocket);*/
		//world.getObjects().add(userRocket);
		menuManager = new MenuManager(world, userRocket, WIDTH, HEIGHT);

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
		
		//getAnimator().start();
		
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
	
		private World world;
		private UserControlledRocket userRocket;
		private int width;
		private int height;
	
		// Hex code for the menu's background color
		private String backgroundColorHex = "#4BCDFF";
		
		/**
		 * Creates a MenuManager object
		 */
		public MenuManager(World world, UserControlledRocket userRocket, int width, int height) {
			
			this.world = world;
			this.userRocket = userRocket;
			this.width = width;
			this.height = height;
			
		}
		
		public int getWidth() {
			return width;
		}
	
		public void setWidth(int width) {
			this.width = width;
		}
	
		public int getHeight() {
			return height;
		}
	
		public void setHeight(int height) {
			this.height = height;
		}
	
		public void setbackgroundColorHex(String colorCode) {
			this.backgroundColorHex = colorCode;
		}
	
		public String getbackgroundColorHex() {
			return backgroundColorHex;
		}
	
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
			
			Button startComputerButton = new Button("Computer Simulation");
			startComputerButton.setPrefSize(200, 50);
			startComputerButton.setAlignment(Pos.CENTER);
			startComputerButton.setTranslateY(getHeight() / 8);
			startComputerButton.setStyle("-fx-font-size:18");
			startComputerButton.setOnAction(event -> startComputerSimulation(stage));
	
			Button startUserButton = new Button("User Simulation");
			startUserButton.setPrefSize(200, 50);
			startUserButton.setAlignment(Pos.CENTER);
			startUserButton.setTranslateY(2 * (getHeight() / 8));
			startUserButton.setOnAction(event -> startUserControlledSimulation(stage));
			startUserButton.setStyle("-fx-font-size:18");
			stackPane.getChildren().addAll(startComputerButton, startUserButton);
		}
	
		public void startComputerSimulation(Stage stage) {
	
			// Add an auto controlled rocket to the world
			// Create the rocket
			double rocketX = WIDTH  / 2;
			double rocketY = world.getGroundY() - 500;
			double xVelocity = Math.random() * maxSpeed * 2 - maxSpeed;
			Vector2D acceleration = new Vector2D(0.0, World.GRAVITY);
			autoRocket = new Rocket(rocketX, rocketY, 10, world.getGroundY());
			autoRocket.getVelocity().setX(xVelocity);
			autoRocket.setAcceleration(acceleration);
	
			System.out.println("Start computer simulation here");
	
		}
	
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


