import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		Game gamer=new Game();
		gamer.Initialize(primaryStage);
		
		
	}

}
