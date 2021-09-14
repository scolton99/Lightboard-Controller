package tech.scolton.lightboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import tech.scolton.lightboard.model.Console;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static List<Stage> stages = new ArrayList<>();

    private static Console console;

    private static String host;
    private static int port;
    private static int user;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Font.loadFont(getClass().getResourceAsStream("/tech/scolton/lightboard/view/UbuntuMono-R.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("/tech/scolton/lightboard/view/Roboto-Regular.ttf"), 16);

        console = new Console();

        primaryStage.setTitle("Lightboard Controller");

        BorderPane root = FXMLLoader.load(getClass().getResource("view/Main.fxml"));

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("view/Main.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        stages.add(primaryStage);


    }

    public static Console getConsole() {
        return console;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static int getUser() {
        return user;
    }

    public static void setHost(String host) {
        Main.host = host;
    }

    public static void setUser(int user) {
        Main.user = user;
    }

    public static void setPort(int port) {
        Main.port = port;
    }

    public static void addStage(Stage stage) {
        stages.add(stage);
    }

    public static void closeAll() {
        stages.forEach(Stage::close);
    }

}
