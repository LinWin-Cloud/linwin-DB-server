package lib;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class buttonUI {
    public static Button buttonUI(String text) {
        Button button = new Button(text);
        button.setPrefWidth(150);
        button.setPrefHeight(35);
        button.setFont(Font.font(19));

        return button;
    }
}
