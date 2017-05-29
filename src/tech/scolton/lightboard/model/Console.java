package tech.scolton.lightboard.model;

import tech.scolton.lightboard.Main;
import tech.scolton.lightboard.util.ConsoleListener;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Console {

    private Map<String, Boolean> history = new HashMap<>();
    private List<ConsoleListener> listeners = new ArrayList<>();

    private String tempCommand = "";

    public Console() {

    }

    public void send() {
        int user = Main.getUser();
        int port = Main.getPort();
        String host = Main.getHost();

        this.listeners.forEach(s -> s.setError(""));

        if (this.tempCommand.equals(""))
            return;

        String pCommand = user + "@" + host + ": " + this.tempCommand;
        this.tempCommand = "<U" + user + "> $ " + this.tempCommand + " #";
        byte[] commandBytes = this.tempCommand.getBytes();

        try {
            InetAddress address = InetAddress.getByName(host);
            DatagramSocket ds = new DatagramSocket();

            DatagramPacket packet = new DatagramPacket(commandBytes, commandBytes.length, address, port);

            ds.send(packet);

            this.listeners.forEach(s -> s.addCommand(pCommand, false));
            this.history.put(pCommand, false);

            ds.close();
        } catch (IOException e) {
            this.listeners.forEach(s -> s.addCommand(pCommand, true));

            if (e instanceof SocketException) {
                this.listeners.forEach(s -> s.setError("Error: " + e.getMessage()));
            } else if (e instanceof UnknownHostException) {
                this.listeners.forEach(s -> s.setError("Error: unknown host " + host));
            } else {
                this.listeners.forEach(s -> s.setError("Error: " + e.getMessage()));
            }
        }
    }

    public void sendDirect(String command) {
        try {
            DatagramPacket packet = new DatagramPacket(command.getBytes(), command.getBytes().length, InetAddress.getByName(Main.getHost()), Main.getPort());
            DatagramSocket ds = new DatagramSocket();

            ds.send(packet);

            ds.close();
        } catch (Exception e) {
            if (e instanceof UnknownHostException) {
                this.listeners.forEach(s -> s.setError("Error: unknown host " + Main.getHost()));
            } else {
                this.listeners.forEach(s -> s.setError("Error: " + e.getMessage()));
            }
        }
    }

    public void registerListener(ConsoleListener listener) {
        this.listeners.add(listener);
    }

    public Map<String, Boolean> getHistory() {
        return this.history;
    }

    public void setTempCommand(String tempCommand) {
        this.tempCommand = tempCommand;
        this.listeners.forEach(s -> s.setTempCommand(this.tempCommand));
    }

    public String getTempCommand() {
        return this.tempCommand;
    }

}
