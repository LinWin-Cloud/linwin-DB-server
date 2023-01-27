package lib;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import action.LoginAction;

public class InputBox {
    public static TextField textField() {
        TextField textField = new TextField();
        textField.setPrefWidth(400);
        textField.setPrefHeight(30);

        return textField;
    }
    public static HBox InputProject(String text,LoginAction loginAction) {
        Label label = new Label(text);
        label.setPrefWidth(150);
        label.setFont(Font.font(18));
        TextField textField = InputBox.textField();
        HBox hBox = new HBox();
        hBox.setMinHeight(40);
        hBox.getChildren().addAll(label,textField);

        if (loginAction.getId() == 1) {
            LoginAction.Server = textField;
        }if (loginAction.getId() == 2) {
            LoginAction.port = textField;
        }if (loginAction.getId() == 3) {
            LoginAction.UserName = textField;
        }if (loginAction.getId() == 4) {
            LoginAction.Passwd = textField;
        }
        return hBox;
    }
}
