import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class HoverslamSimulator extends Application {

	// TODO: I bet it'd be cool if I graphed altitude vs. time for the rocket
	
	private final int WIDTH = 800;
	private final int HEIGHT = 800;

	private Group root;
	private Scene primaryScene;
	private Canvas canvas;
	private GraphicsContext gc;
	private AnimationTimer animator;

	private World world;
	private Rocket rocket;

	@Override
	public void init() {
		
		root = new Group();
		world = new World(WIDTH, HEIGHT);
		world.setCenterOnRocketHorizontally(true);
		world.setCenterOnRocketVertically(true);
		
		animator = new AnimationTimer() {

			long startTime;
			long currentTime;

			private long lastUpdate;

			@Override
			public void start() {

				//gc.scale(1, 1);
				startTime = System.nanoTime();
				lastUpdate = startTime;
				super.start();

			}
			
			public void clearScreen(GraphicsContext gc) {
				/*
				gc.clearRect(gc.getTransform().getTy(), gc.getTransform().getTx(),
						WIDTH, HEIGHT);
						*/
				gc.clearRect(-gc.getTransform().getTx(), -gc.getTransform().getTy(),
						WIDTH, HEIGHT);
				
			}
			
			@Override
			public void handle(long now) {

				clearScreen(gc);

				currentTime = System.nanoTime();
				double timeSinceLastUpdateSeconds = (now - lastUpdate) / 1_000_000_000.0;
				
				// -gc.gettransform.gettx is basically gc x coord
				
				world.draw(gc);
				
				if (shouldUpdateGame(rocket)) {
					world.tick(timeSinceLastUpdateSeconds);
				}
				lastUpdate = now;

			}
		};
		
		// Create the rockets
		
		for (int i = 0; i < 1; i++) {

			double x = WIDTH / 2; // Math.random() * (WIDTH - 40) + 20;
			double y = world.getGroundY() - 1000;// Math.random() * (world.getGroundY()) - 80;
			rocket = new Rocket(x, y, 100, world.getGroundY(), animator, gc);
			double maxSpeed = 250;
			rocket.getVelocity().setX(Math.random() * maxSpeed * 2 - maxSpeed);//Math.random() * 300 - 150);
			//rocket.getVelocity().setX(300);
			rocket.setAcceleration(new Vector2D(0.0, World.GRAVITY));
			//rocket.getVelocity().setY(-1000);
			world.getObjects().add(rocket);

		}
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Hoverslam Simulation");

		primaryScene = new Scene(root, WIDTH, HEIGHT);

		primaryStage.setWidth(WIDTH); 
		primaryStage.setHeight(HEIGHT);
		
		canvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());

		root.getChildren().add(canvas);

		// NOTE: It's important for these buttons to be added to the root
		// after the Canvas: they won't receive MouseEvents otherwise.
		for (CustomButton button : rocket.getComputer().getButtons()) {

			root.getChildren().add(button);

		}
		
		gc = canvas.getGraphicsContext2D();
		
		animator.start();

		addKeyboardHandling(primaryScene);
		addMouseHandling(primaryScene);
		
		
		primaryStage.setScene(primaryScene);
		primaryStage.show();
		
	}
	
	/**
	 * Adds keyboard event(ex. key press) handling to a scene
	 * @param scene the Scene to add keyboard event handling to
	 */
	private void addKeyboardHandling(Scene scene) {
		
		KeyboardHandler keyboardHandler = new KeyboardHandler(this);
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

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public AnimationTimer getAnimator() {
		return animator;
	}

	public void setAnimator(AnimationTimer animator) {
		this.animator = animator;
	}
	
	public boolean shouldUpdateGame(Rocket rocket) {
		
		// TODO I should be getting the buttons from a UIController or
		// TODO something, not the Rocket
		for (CustomButton button : rocket.getComputer().getButtons()) {
			
			if (button.getClass() == TogglePlayButton.class) {
				
				return ((TogglePlayButton) button).getState().equals("PAUSE");
				
			}
			
		}
		
		return true;
		
	}

	public static void main(String[] args) {

		launch(args);

	}

}