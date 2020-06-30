
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class HoverslamSimulator extends Application {

	private final int WIDTH = 800;
	private final int HEIGHT = 800;
	
	private Group root;
	private Scene primaryScene;
	private Canvas canvas;
	//private KeyboardHandler keyboardHandler;
	private GraphicsContext gc;
	private AnimationTimer animator;
	
	private World world;
	@Override 
	public void init() {
		
		world = new World(WIDTH, HEIGHT);
		// Initialize the world
		
		for (int i = 0; i < 20; i++) {
			
			double x = WIDTH / 2; //Math.random() * (WIDTH - 40) + 20;
			double y = world.getGroundY() - 540;//Math.random() * (world.getGroundY()) - 80;
			Rocket rocket = new Rocket(x, y, 100, world.getGroundY());
			rocket.getVelocity().setX(Math.random() * 200 - 100);
			world.getObjects().add(rocket);
			System.out.println(rocket.toString());
			
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
			double timeFromStartSeconds; 
			
			private long lastUpdate;
			
			@Override
			public void start() {
				
				startTime = System.nanoTime();
				lastUpdate = startTime;
				super.start();
				for (Entity entity : world.getObjects()) {
					
					if (entity.getClass() == Rocket.class) {
						
						((Rocket) entity).setMotionStartTime(
								System.nanoTime());
						
					}
					
				}
				
			}
			
			
			@Override
			public void handle(long now) {
				
				
				gc.clearRect(0, 0, WIDTH, HEIGHT); // clear the screen
				
				currentTime = System.nanoTime();
				timeFromStartSeconds = (currentTime - startTime) 
						/ 1_000_000_000.0; 
				double timeSinceLastUpdateSeconds = (now - lastUpdate)
						/ 1_000_000_000.0;
				
				world.setTime(timeFromStartSeconds);
				world.tick(timeSinceLastUpdateSeconds);
				world.draw(gc);
				
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