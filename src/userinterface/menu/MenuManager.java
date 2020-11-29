package userinterface.menu;
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

public class MenuManager {
	
	private int width;
	private int height;

	// Hex code for the menu's background color
	private String backgroundColorHex = "#4BCDFF";
	
	/**
	 * Creates a MenuManager object
	 * @param gameObject
	 */
	public MenuManager(int width, int height) {
		
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
		System.out.println("Start computer simulation here");
	}

	public void startUserControlledSimulation(Stage stage) {

		System.out.println("Start user controlled simulation here");

	}

}
