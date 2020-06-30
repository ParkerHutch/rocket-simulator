import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Translate;
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

	@Override
	public void init() {
		
		world = new World(WIDTH, HEIGHT);
		world.setCenterOnRocketHorizontally(true);
		world.setCenterOnRocketVertically(true);
		
		// Create the rockets
		
		for (int i = 0; i < 1; i++) {

			double x = WIDTH / 2; // Math.random() * (WIDTH - 40) + 20;
			double y = world.getGroundY() - 1000;// Math.random() * (world.getGroundY()) - 80;
			Rocket rocket = new Rocket(x, y, 100, world.getGroundY());
			rocket.getVelocity().setX(Math.random() * 500 - 250);//Math.random() * 300 - 150);
			//rocket.getVelocity().setY(-1000);
			world.getObjects().add(rocket);

		}
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Hoverslam Simulation");

		root = new Group();

		primaryScene = new Scene(root, WIDTH, HEIGHT);

		primaryStage.setWidth(WIDTH); // adding these 2 lines fixes drawing errors
		primaryStage.setHeight(HEIGHT);

		canvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());

		canvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());

		root.getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();
		
		animator = new AnimationTimer() {

			long startTime;
			long currentTime;

			private long lastUpdate;

			@Override
			public void start() {

				gc.scale(1, 1);
				startTime = System.nanoTime();
				lastUpdate = startTime;
				super.start();

			}
			
			public void clearScreen(GraphicsContext gc) {
				
				// TODO: maybe make this implementation better so I don't have to
				// clear the area around all the entities
				gc.clearRect(gc.getTransform().getTy(), gc.getTransform().getTx(),
						WIDTH, HEIGHT);
				
				/*
				for (Entity entity : world.getObjects()) {
					
					gc.clearRect(entity.getX() - WIDTH, entity.getY() - HEIGHT, WIDTH, HEIGHT);
					
				}
				
				gc.clearRect(-WIDTH, 0, 2 * WIDTH, HEIGHT);
				*/
				
			}
			
			@Override
			public void handle(long now) {

				//boolean centerOnRocket = true;

				clearScreen(gc);

				currentTime = System.nanoTime();
				double timeSinceLastUpdateSeconds = (now - lastUpdate) / 1_000_000_000.0;
				
				Rocket rocket = (Rocket) world.getObjects().get(0);

				
				//if (centerOnRocket) {
					
					// TODO After adding random terrain features, add code to 
					// make the camera just follow the Rocket horizontally
					/*
					double xTranslate = -rocket.getX() - gc.getTransform().getTx() + WIDTH / 2;
					double yTranslate = -rocket.getY() + rocket.getHeight() / 2 - gc.getTransform().getTy()
							+ HEIGHT / 2;
					
					gc.translate(xTranslate, yTranslate);
					*/
					
					//world.setCenterOnRocketHorizontally(true);
					//world.setCenterOnRocketVertically(true);
					//world.centerOnRocket(gc, rocket);
					//world.centerOnRocketVertically(gc, rocket);
					
					//world.centerOnRocket(gc, rocket);
				//}
				
				// gc.gettransform.gettx is basically gc x coord
				
				world.draw(gc);
				world.tick(timeSinceLastUpdateSeconds);

				lastUpdate = now;

			}
		};
		animator.start();

		primaryStage.setScene(primaryScene);
		primaryStage.show();
	}

	public static void main(String[] args) {

		launch(args);

	}

}