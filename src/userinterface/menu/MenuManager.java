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

public class MenuManager {
	
	private int width;
	private int height;
	
	int projectButtonColumns = 2;
	
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

	public void showTitleScreen(Stage stage) {
		
		StackPane stackPane = new StackPane();
		
		Scene mainMenuScene = new Scene(stackPane, getWidth(), getHeight());
		stage.setScene(mainMenuScene); // TODO Change
		
		Text author = new Text("Rocketry");
		author.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 50));
		author.setTranslateX(getWidth() / 5);
		
		Text title = new Text("Java Portfolio");
		title.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 50));
		
		author.setTranslateY(-getHeight() / 4);
		stackPane.getChildren().addAll(title, author);
		
		Button startButton = new Button("Projects");
		startButton.setPrefSize(200, 50);
		startButton.setAlignment(Pos.CENTER);
		startButton.setTranslateY(getHeight() / 8);
		//startButton.setOnAction(event -> showProjectsScreen(stage));
		
		stackPane.getChildren().add(startButton);

	}

	public void showErrorScreen(Stage stage, String error) {
		
		StackPane stackPane = new StackPane();
		
		Scene scene = new Scene(stackPane, getWidth(), getHeight());
		stage.setScene(scene);
		
		Text errorLabel = new Text("An error occurred while trying to run the project.");
		errorLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 40));
		errorLabel.setTextAlignment(TextAlignment.CENTER);
		errorLabel.setTranslateY(-getHeight() / 8);
		errorLabel.setWrappingWidth(getWidth()  - getWidth() / 10);
		
		Text rawErrorOutput = new Text("Error output: " + error);
		rawErrorOutput.setFont(Font.font("Times New Roman", 24));
		rawErrorOutput.setTextAlignment(TextAlignment.CENTER);
		rawErrorOutput.setWrappingWidth(errorLabel.getWrappingWidth());
		
		stackPane.getChildren().addAll(errorLabel, rawErrorOutput);
		
		Button backButton = new Button("Back to Projects Screen");
		backButton.setPrefSize(200, 50);
		backButton.setAlignment(Pos.CENTER);
		backButton.setTranslateY(getHeight() / 4);
		//backButton.setOnAction(event -> showProjectsScreen(stage)); TODO fix
		
		stackPane.getChildren().add(backButton);
		
	}

}
