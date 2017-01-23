package tech.scolton.lightboard;

import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;

public class Controller {

    public FlowPane buttons;
    public BorderPane root;
    public BorderPane activity;
    public Label title;
    public VBox console;
    public TextField command;
    public Button send;
    public Label hostLabel;
    public Label portLabel;
    public Label userLabel;
    public TextField host;
    public TextField port;
    public TextField user;
    public Text footer;
    public Label status;

    public Controller() {

    }

    @FXML
    public void addCommand(ActionEvent ae) {
        String text = this.command.getText();
        int l = text.length();

        EventTarget target = ae.getTarget();
        assert (target instanceof Button);

        Button b = (Button) target;

        if (l > 0 && !text.substring(l-1).equals(" ")) {
            text += " ";
        }

        text += b.getUserData();

        this.command.setText(text);
    }

    @FXML
    public void sendCommand() {
        this.status.setText("");

        String command = this.command.getText();
        String user = this.user.getText();
        String host = this.host.getText();
        int port = Integer.parseInt(this.port.getText());

        if (command.equals(""))
            return;

        String pCommand = user + "@" + host + ": " + command;
        command = "<U" + user + "> $ " + command + " #";
        byte[] commandBytes = command.getBytes();

        try {
            InetAddress address = InetAddress.getByName(host);
            DatagramSocket ds = new DatagramSocket();

            DatagramPacket packet = new DatagramPacket(commandBytes, commandBytes.length, address, port);

            ds.send(packet);

            Label label = new Label(pCommand);
            label.setTextFill(Color.WHITE);
            this.console.getChildren().add(label);

            ds.close();
        } catch (IOException e) {
            this.status.setTextFill(Color.RED);

            Label label = new Label(pCommand);
            label.setTextFill(Color.RED);
            this.console.getChildren().add(label);

            if (e instanceof SocketException) {
                this.status.setText("Error: " + e.getMessage());
            } else if (e instanceof UnknownHostException) {
                this.status.setText("Error: unknown host " + host);
            } else {
                this.status.setText("Error: " + e.getMessage());
            }
        }
    }

    @FXML
    public void showConsole() throws IOException {
        GridPane boardRoot = FXMLLoader.load(getClass().getResource("Board.fxml"));

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
        boardScene.getStylesheets().add(getClass().getResource("Board.css").toExternalForm());

        consolePane.setScene(boardScene);
        consolePane.show();
    }

}
