
import action.LoginAction;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lib.InputBox;

public class Logon extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initOwner(ClientUI.stage);

        primaryStage.setWidth(550);
        primaryStage.setHeight(400);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ClientUI.stage.close();
            }
        });

        primaryStage.setResizable(false);
        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);

        VBox box = new VBox();
        box.setSpacing(10);
        box.setPadding(new Insets(40));

        Label title = new Label("Linwin SQL");
        title.setFont(Font.font(30));
        title.setPrefHeight(80);

        HBox server = InputBox.InputProject("Server: ",new LoginAction(1));
        HBox port = InputBox.InputProject("Port: ",new LoginAction(2));
        HBox user = InputBox.InputProject("UsersName: ",new LoginAction(3));
        HBox passwd = InputBox.InputProject("Passwd: ",new LoginAction(4));

        Button logon = lib.buttonUI.buttonUI("Logon");

        box.getChildren().addAll(title,server,port,user,passwd,logon);
        gridPane.getChildren().addAll(box);

        logon.setOnAction((ActionEvent e) -> {
            String remote = LoginAction.Server.getText();
            String Login = LoginAction.UserName.getText();
            String Passwd = LoginAction.Passwd.getText();
        });

        primaryStage.xProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ClientUI.stage.setX(primaryStage.getX()-ClientUI.stage.getWidth()/4);
            }
        });
        primaryStage.yProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ClientUI.stage.setY(primaryStage.getY()-ClientUI.stage.getHeight()/4);
            }
        });
    }
    public static void main(String[] args) {
        Logon.launch();
    }
}
