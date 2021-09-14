package tech.scolton.lightboard.util;

public interface ConsoleListener {

    void setTempCommand(String command);
    void addCommand(String response, boolean error);
    void setError(String error);

}
