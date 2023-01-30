import action.Json;
import action.LoginAction;
import action.ReadFile;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import connect.Connect;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.annotation.XmlElementDecl;
import java.util.Arrays;


public class ClientUI extends Application {
    public static String userName = "";
    public static String Passwd = "";
    public static String port = "";
    public static String IP = "";
    public static Stage stage;
    public static String dataNow = "";
    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientUI.stage = primaryStage;
        primaryStage.setWidth(1000);
        primaryStage.setHeight(750);

        Label userL = new Label("Login User: "+ClientUI.userName);
        Label remoteL = new Label("Remote Host: "+ClientUI.IP+":"+port);
        userL.setPrefWidth(200);
        remoteL.setPrefWidth(200);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(300);
        scrollPane.setPrefHeight(primaryStage.getHeight());
        VBox scrollBox = new VBox();
        scrollBox.setSpacing(5);
        scrollBox.setPadding(new Insets(5));
        scrollPane.setContent(scrollBox);


        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);

        MenuBar menuBar = new MenuBar();
        menuBar.setPrefWidth(primaryStage.getWidth());
        Menu file = new Menu("File");
        Menu terminal = new Menu("Terminal");
        Menu project = new Menu("Project");
        Menu help = new Menu("Help");

        MenuItem close = new MenuItem("Exit");

        MenuItem showTerminal = new MenuItem("New LinwinDB Terminal");
        MenuItem command = new MenuItem("Command");

        MenuItem OpenProject = new MenuItem("Open Project");
        MenuItem Connect = new MenuItem("Connect");

        MenuItem doc = new MenuItem("Document");
        MenuItem pro = new MenuItem("Linwin Data Server Project");
        MenuItem about = new MenuItem("About");

        file.getItems().addAll(close);
        terminal.getItems().addAll(showTerminal,command);
        project.getItems().addAll(OpenProject,Connect);
        help.getItems().addAll(doc,pro,about);

        VBox box = new VBox();
        box.setPrefWidth(primaryStage.getWidth());
        HBox index = new HBox();

        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(300);
        leftPanel.setStyle("-fx-background-color: #f7f7f7");

        HBox toolBox =new HBox();
        toolBox.setPrefWidth(300);
        toolBox.setMinHeight(35);
        toolBox.setPadding(new Insets(5));
        toolBox.setStyle("-fx-background-color: grey");
        toolBox.setSpacing(5);
        Color.rgb(255,1,1);

        Button reload = lib.buttonUI.button1("Reload");
        Button create = lib.buttonUI.button1("Create");

        VBox dataPanel = new VBox();
        dataPanel.setPrefWidth(primaryStage.getWidth()-300);
        HBox info = new HBox();
        info.setPrefWidth(primaryStage.getWidth()-300);
        info.setSpacing(10);
        info.setStyle("-fx-background-color: #e2e2e2");
        info.setPadding(new Insets(10));

        ScrollPane dataLoader = new ScrollPane();
        dataLoader.setPrefWidth(primaryStage.getWidth()-300);
        dataLoader.setPrefHeight(primaryStage.getHeight());
        VBox dataBox = new VBox();
        dataBox.setPadding(new Insets(5));
        dataLoader.setContent(dataBox);

        dataPanel.getChildren().addAll(info,dataLoader);
        info.getChildren().addAll(userL,remoteL);
        toolBox.getChildren().addAll(reload,create);
        menuBar.getMenus().addAll(file,terminal,project,help);
        box.getChildren().addAll(menuBar,index);
        leftPanel.getChildren().addAll(toolBox,scrollPane);
        index.getChildren().addAll(leftPanel,dataPanel);
        gridPane.getChildren().addAll(box);

        Thread listDatabase = new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Connect connect = new Connect();
                        if (connect.sendMessage("list database",ClientUI.IP,Integer.valueOf(port),ClientUI.userName,ClientUI.Passwd))
                        {
                            String[] splitDatabase = connect.getMessage().split("\n");
                            for (int i = 0 ; i <splitDatabase.length ; i++) {
                                if (i == 0) {
                                    ClientUI.dataNow = splitDatabase[i];
                                }
                                Button button = lib.buttonUI.button1("   "+splitDatabase[i]);
                                button.setPrefHeight(35);
                                button.setPrefWidth(290);
                                button.setAlignment(Pos.BASELINE_LEFT);
                                button.setId(splitDatabase[i]);
                                scrollBox.getChildren().addAll(button);

                                int finalI = i;
                                button.setOnAction((ActionEvent e) -> {
                                    dataBox.getChildren().clear();
                                    ClientUI.dataLoader(dataBox,splitDatabase[finalI],primaryStage);
                                });
                            }
                        }else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Connect Error");
                            alert.setHeaderText("Connect error");
                            alert.setContentText("There is a error about connect to the Server");
                            alert.showAndWait();
                        }
                    }
                });
            }
        });

        try{
            if (ReadFile.getLine("../../config/client/UIauto").replace(" ","").equals("true")) {
                String port = Json.readJson("../../config/client/autoLogin.json","port");
                String remote = Json.readJson("../../config/client/autoLogin.json","remote");
                String user = Json.readJson("../../config/client/autoLogin.json","user");
                String passwd = Json.readJson("../../config/client/autoLogin.json","passwd");

                System.out.println(port+" "+remote+" "+user+" "+passwd);
                System.out.println(LoginAction.connectRemote(remote,user,port,passwd));

                if (LoginAction.connectRemote(remote,user,port,passwd)) {
                    ClientUI.userName = user;
                    ClientUI.IP = remote;
                    ClientUI.Passwd = passwd;
                    ClientUI.port = port;

                    userL.setText("User: "+user);
                    remoteL.setText("Remote Host: "+remote+":"+port);
                    listDatabase.start();

                    primaryStage.show();
                }else {
                    Logon logon = new Logon();
                    primaryStage.show();
                    logon.start(new Stage());
                }
            }
            else {
                Logon logon = new Logon();
                primaryStage.show();
                logon.start(new Stage());
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }

        primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setPrefWidth(300);
                box.setPrefWidth(primaryStage.getWidth());
                menuBar.setPrefWidth(primaryStage.getWidth());
                dataPanel.setPrefWidth(primaryStage.getWidth()-300);
                info.setPrefWidth(primaryStage.getWidth()-300);
                dataLoader.setPrefWidth(primaryStage.getWidth()-300);
            }
        });
        primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setPrefHeight(primaryStage.getHeight());
                dataLoader.setPrefHeight(primaryStage.getHeight());
            }
        });
    }
    public static void main(String[] args) {
        ClientUI.launch();
    }
    public static void dataLoader(VBox box,String database,Stage stage) {
        box.setPadding(new Insets(20));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Connect connect = new Connect();
                        Boolean b1 = connect.sendMessage("view "+database,ClientUI.IP,Integer.valueOf(port),ClientUI.userName,ClientUI.Passwd);

                        Label title = new Label("Name");
                        Label value = new Label("Value");
                        Label type = new Label("Type");
                        Label createTime = new Label("Create Time");
                        Label updateTime = new Label("Update Time");
                        Label note = new Label("Note");

                        title.setMinWidth(80);
                        value.setMinWidth(80);
                        type.setMinWidth(80);
                        createTime.setMinWidth(80);
                        updateTime.setMinWidth(80);
                        note.setMinWidth(80);

                        HBox titleBox = new HBox();
                        titleBox.getChildren().addAll(title,value,type,createTime,updateTime,note);
                        box.getChildren().add(titleBox);
                        box.setSpacing(5);

                        stage.widthProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                titleBox.setPrefWidth(stage.getWidth());
                                title.setPrefWidth((stage.getWidth()-320)/7);
                                value.setPrefWidth((stage.getWidth()-320)/7);
                                type.setPrefWidth((stage.getWidth()-320)/7);
                                createTime.setPrefWidth((stage.getWidth()-320)/7);
                                updateTime.setPrefWidth((stage.getWidth()-320)/7);
                                note.setPrefWidth((stage.getWidth()-320)/7);
                            }
                        });

                        if (b1) {
                            String[] split = connect.getMessage().split("\n");
                            for (int i = 0 ; i < split.length ; i++)
                            {
                                HBox hBox = new HBox();
                                hBox.setPrefWidth(stage.getWidth());
                                hBox.setStyle("" +
                                        "-fx-border-color: black;" +
                                        "-fx-border-width: 0.3");
                                hBox.setMinHeight(28);
                                hBox.setPadding(new Insets(5));
                                hBox.setId(split[i]);

                                ClientUI.DataContentLoader(hBox,split[i],stage);
                                stage.widthProperty().addListener(new ChangeListener<Number>() {
                                    @Override
                                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                        hBox.setPrefWidth(stage.getWidth());
                                    }
                                });
                                Glow glow = new Glow();
                                hBox.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        hBox.setEffect(glow);
                                    }
                                });
                                hBox.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        hBox.setEffect(null);
                                    }
                                });

                                box.getChildren().add(hBox);
                            }
                        }else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Connect Error");
                            alert.setHeaderText("Connect error");
                            alert.setContentText("There is a error about connect to the Server");
                            alert.showAndWait();
                        }
                    }
                });
            }
        });
        thread.start();
    }
    public static void DataContentLoader(HBox box,String split,Stage stage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String[] splitData = split.split("  |  ");

                for (int j = 0 ; j < splitData.length ;j++) {
                    Label data = new Label(splitData[j]);
                    data.setMinWidth(50);

                    stage.widthProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            data.setPrefWidth((stage.getWidth()-320)/13);
                        }
                    });

                    box.getChildren().add(data);
                }
            }
        });
    }
}
