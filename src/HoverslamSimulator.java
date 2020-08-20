import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import rocket.UserControlledRocket;
import rocket.Vector2D;
import userinterface.CustomButton;
import userinterface.TogglePlayButton;
import userinterface.UserInterface;
import userinterface.menu.MenuManager;
import world.World;

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
	
	private MenuManager menuManager = new MenuManager(WIDTH, HEIGHT);
	
	double maxSpeed = 250;

	@Override
	public void init() {
		
		root = new Group();
		World world = new World(WIDTH, HEIGHT);
		world.setCenterOnRocketHorizontally(true);
		world.setCenterOnRocketVertically(true);
		
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
		
		// Create the rocket
		double rocketX = WIDTH  / 2;
		double rocketY = world.getGroundY() - 500;
		UserControlledRocket rocket = new UserControlledRocket(rocketX, rocketY, 10, world.getGroundY());
		keyboardHandler = new KeyboardHandler(rocket);
		
		rocket.getVelocity().setX(Math.random() * maxSpeed * 2 - maxSpeed);
		rocket.setAcceleration(new Vector2D(0.0, World.GRAVITY));
		world.getObjects().add(rocket);
		
		userInterface = new UserInterface(0, 0, 100, 800, 
				rocket, 
				world.getGroundY(), getAnimator());
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Rocketry Simulation");

		primaryScene = new Scene(root, WIDTH, HEIGHT);

		primaryStage.setWidth(WIDTH); 
		primaryStage.setHeight(HEIGHT);
		
		primaryStage.setScene(primaryScene);
		primaryStage.show();
		
		//getMenuManager().showTitleScreen(primaryStage);
		
		
		addKeyboardHandling(primaryScene);
		addMouseHandling(primaryScene);
		
		canvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());
		gc = canvas.getGraphicsContext2D();
		
		root.getChildren().add(canvas);

		// NOTE: It's important for these buttons to be added to the root
		// after the Canvas: they won't receive MouseEvents otherwise.
		for (CustomButton button : getUserInterface().getButtons()) {

			root.getChildren().add(button);
			// TODO add LandCrashMessage buttons here

		}
		
		
		
		getAnimator().start();
		
		
		
		
		
		
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

}