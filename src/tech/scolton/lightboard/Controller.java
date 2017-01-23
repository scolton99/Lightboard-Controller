package tech.scolton.lightboard;

import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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

}
