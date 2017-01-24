package tech.scolton.lightboard.controller;

import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import tech.scolton.lightboard.Main;

public class BoardController {

    public GridPane boardRoot;
    public Slider grandmaster, sub1, sub2;

    public Button macro, learn, labelNote, delete, help, effect;
    public Button part, cue, record, intPalette, focusPalette, recordOnly, colorPalette, beamPalette, update, preset, sub, group, shift, delay, time;
    public Button about, copyTo, recallFrom, goToCue, block, undo;
    public Button plus, thru, underscore, seven, eight, nine, four, five, six, one, two, three, clear, zero, point;
    public Button park, last, capture, next;
    public Button slash, sneak, remDim, home, out, select, full, qOnly, at, enter;
    public Button stopBack, go;

    @FXML
    public void addTextToCommand(ActionEvent ae) {
        EventTarget b = ae.getTarget();

        assert(b instanceof Button);

        Button button = (Button) b;

        String already = Main.getConsole().getTempCommand();

        String text = (String) button.getUserData();
        int l = text.length();

        if (l > 0 && !text.substring(l-1).equals(" ")) {
            text += " ";
        }

        Main.getConsole().setTempCommand(already + text + " ");
    }

    @FXML
    public void clear() {
        String text = Main.getConsole().getTempCommand();
        String newText = null;
        int l = text.length();

        if (l == 0)
            return;

        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(text.substring(l-1));

            Main.getConsole().setTempCommand(text.substring(0, l-1));
            return;
        } catch (NumberFormatException ignored) {

        }

        for (int i = l - 2; i >= 0; i--) {
            String test = text.substring(i, i + 1);
            if (test.equals(" ")) {
                Main.getConsole().setTempCommand(text.substring(0, i + 1) + " ");
                return;
            }
        }

        Main.getConsole().setTempCommand("");
    }

}
