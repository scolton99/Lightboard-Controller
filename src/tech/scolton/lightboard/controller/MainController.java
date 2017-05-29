package tech.scolton.lightboard.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tech.scolton.lightboard.Main;
import tech.scolton.lightboard.model.Console;
import tech.scolton.lightboard.util.ConsoleListener;

import java.io.IOException;

public class MainController implements ConsoleListener {

    @FXML public Menu viewMenu;
    @FXML private BorderPane root;
    @FXML private BorderPane activity;
    @FXML private Label title;
    @FXML private VBox console;
    @FXML private TextField command;
    @FXML private Button send;
    @FXML private Label hostLabel;
    @FXML private Label portLabel;
    @FXML private Label userLabel;
    @FXML private TextField host;
    @FXML private TextField port;
    @FXML private TextField user;
    @FXML private Text footer;
    @FXML private Label status;
    @FXML private MenuBar menuBar;
    @FXML private ScrollPane consoleContainer;

    public MainController() throws IOException {
        Console c = Main.getConsole();
        c.registerListener(this);
    }

    public void initialize() {
        this.host.focusedProperty().addListener(new OnFocusLost());
        this.port.focusedProperty().addListener(new OnFocusLost());
        this.user.focusedProperty().addListener(new OnFocusLost());

        this.command.textProperty().addListener(new OnTextChange());

        this.console.heightProperty().addListener((observable, oldValue, newValue) -> {
            consoleContainer.setHvalue((Double)newValue);

            consoleContainer.setVvalue(1.0);
        });
    }

    @FXML
    public void sendCommand() {
        Main.getConsole().setTempCommand(this.command.getText());
        update();
        Main.getConsole().send();
    }

    @FXML
    public void showConsole() throws IOException {
        GridPane boardRoot = FXMLLoader.load(getClass().getResource("../view/Board.fxml"));

        for (int i = 0; i <= 16; i++) {
            ColumnConstraints ci = new ColumnConstraints(50);
            boardRoot.getColumnConstraints().add(ci);
        }

        for (int i = 0; i <= 7; i++) {
            RowConstraints ri = new RowConstraints(50);
            boardRoot.getRowConstraints().add(ri);
        }

        Stage consolePane = new Stage();
        consolePane.setTitle("Console");

        Scene boardScene = new Scene(boardRoot);
        boardScene.getStylesheets().add(getClass().getResource("../view/Board.css").toExternalForm());

        consolePane.setScene(boardScene);
        consolePane.show();

        Main.addStage(consolePane);
    }

    @Override
    public void addCommand(String response, boolean error) {
        Label label = new Label(response);

        if (error)
            label.setTextFill(Color.RED);
        else
            label.setTextFill(Color.WHITE);

        this.console.getChildren().add(label);
    }

    @Override
    public void setError(String error) {
        this.status.setTextFill(Color.RED);
        this.status.setText(error);
    }

    @Override
    public void setTempCommand(String tempCommand) {
        this.command.setText(tempCommand);
    }

    @FXML
    private void update() {
        Main.setHost(this.host.getText());
        Main.setPort(Integer.parseInt(this.port.getText()));
        Main.setUser(Integer.parseInt(this.user.getText()));
    }

    private class OnFocusLost implements ChangeListener<Boolean> {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (!newValue) {
                update();
            }
        }
    }

    private class OnTextChange implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            Main.getConsole().setTempCommand(newValue);
        }
    }

}
