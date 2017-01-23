package tech.scolton.lightboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Lightboard Controller");

        BorderPane root = FXMLLoader.load(getClass().getResource("Main.fxml"));

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
