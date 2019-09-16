
//import JavaFXApplicationTemplate.KeyboardHandler;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HoverslamSimulator extends Application {

	private final int WIDTH = 600;
	private final int HEIGHT = 600;
	
	private Group root;
	private Scene primaryScene;
	private Canvas canvas;
	//private KeyboardHandler keyboardHandler;
	private GraphicsContext gc;
	private AnimationTimer animator;
	
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(new Group(), 400, 300));
        //primaryStage.show();
        
        root = new Group();

        primaryScene = new Scene(root, WIDTH, HEIGHT);
        
        /*
		keyboardHandler = new KeyboardHandler();
		primaryScene.setOnKeyPressed(keyboardHandler);
		primaryScene.setOnKeyReleased(keyboardHandler);
		*/

		primaryStage.setWidth(WIDTH); // adding these 2 lines fixes drawing errors
		primaryStage.setHeight(HEIGHT);

		canvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());
		
        //theStage = gameStage;
		//theStage.setTitle("JavaFX Application Template");

		canvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());
		// also, adding numbers moves the canvas down further on the screen or further
		// to the right

		root.getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();

		animator = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				
				// NOTE: Game loop

				gc.clearRect(0, 0, WIDTH, HEIGHT); // clear the screen

				// fill a rectangle
				gc.setFill(Color.BLACK);
				gc.fillRect(WIDTH / 2 - 50, HEIGHT / 2 - 50, 50, 50);

			}
		};
		animator.start();

		primaryStage.setScene(primaryScene); // stuff won't show up without this
		primaryStage.show();
    }


    public static void main(String[] args) {
    	
    	
        launch(args);
    }
}