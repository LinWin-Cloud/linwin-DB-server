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
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.annotation.XmlElementDecl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public class ClientUI extends Application {
    public static String userName = "";
    public static String Passwd = "";
    public static String port = "";
    public static String IP = "";
    public static Stage stage;
    public static String dataNow = "";
    public static TextArea logOut;
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
        Menu help = new Menu("Help");

        MenuItem close = new MenuItem("Exit");

        MenuItem showTerminal = new MenuItem("New LinwinDB Terminal");
        MenuItem command = new MenuItem("Command");

        MenuItem doc = new MenuItem("Document");
        MenuItem pro = new MenuItem("Linwin Data Server Project");
        MenuItem about = new MenuItem("About");

        file.getItems().addAll(close);
        terminal.getItems().addAll(showTerminal,command);
        help.getItems().addAll(doc,pro,about);

        VBox box = new VBox();
        box.setPrefWidth(primaryStage.getWidth());
        HBox index = new HBox();

        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(300);
        leftPanel.setStyle("-fx-background-color: #f7f7f7");

        TextArea textArea = new TextArea();
        textArea.setPrefHeight(primaryStage.getWidth());
        textArea.setMinHeight(130);
        textArea.setStyle("" +
                "-fx-border-color: black;" +
                "-fx-border-width: 0.3;" +
                "-fx-background-color: #ccc");
        textArea.setEditable(false);

        ScrollPane dataLoader = new ScrollPane();
        dataLoader.setPrefWidth(primaryStage.getWidth()-300);
        dataLoader.setPrefHeight(primaryStage.getHeight());

        VBox dataBox = new VBox();
        dataBox.setPadding(new Insets(5));
        dataLoader.setContent(dataBox);

        HBox toolBox =new HBox();
        toolBox.setPrefWidth(300);
        toolBox.setMinHeight(35);
        toolBox.setPadding(new Insets(5));
        toolBox.setStyle("-fx-background-color: grey");
        toolBox.setSpacing(5);
        Color.rgb(255,1,1);

        Button reload = lib.buttonUI.button1("Reload");
        Button create = lib.buttonUI.button1("Create");

        reload.setOnAction((ActionEvent e) -> {
            ClientUI.listDatabase(scrollBox,textArea,dataBox);
        });

        VBox dataPanel = new VBox();
        dataPanel.setPrefWidth(primaryStage.getWidth()-300);
        HBox info = new HBox();
        info.setPrefWidth(primaryStage.getWidth()-300);
        info.setSpacing(10);
        info.setStyle("-fx-background-color: #e2e2e2");
        info.setPadding(new Insets(10));

        Label label = new Label("Select Database and next.");
        label.setFont(Font.font(30));
        label.setEffect(new Glow());
        dataBox.getChildren().addAll(label);

        dataPanel.getChildren().addAll(info,dataLoader);
        info.getChildren().addAll(userL,remoteL);
        toolBox.getChildren().addAll(reload,create);
        menuBar.getMenus().addAll(file,terminal,help);
        box.getChildren().addAll(menuBar,index);
        leftPanel.getChildren().addAll(toolBox,scrollPane);
        index.getChildren().addAll(leftPanel,dataPanel);
        gridPane.getChildren().addAll(box);

        VBox outPutBox = new VBox();
        outPutBox.setPrefWidth(primaryStage.getWidth());
        outPutBox.setPrefHeight(160);
        outPutBox.setAlignment(Pos.CENTER);

        ClientUI.logOut = textArea;

        outPutBox.getChildren().addAll(textArea);

        HBox bottomTool = new HBox();
        bottomTool.setPadding(new Insets(0,0,0,10));
        bottomTool.setSpacing(10);

        Button clear = lib.buttonUI.button1("Clean Log");
        bottomTool.getChildren().addAll(clear);

        clear.setOnAction((ActionEvent e) -> {
            textArea.setText("== Clean ==");
        });

        textArea.setText(textArea.getText()+"\nLogin={"+ClientUI.userName+"} ; Remote={"+ClientUI.IP+":"+ClientUI.port+"}");

        box.getChildren().addAll(outPutBox,bottomTool);
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
                    ClientUI.listDatabase(scrollBox,textArea,dataBox);

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
    public static void listDatabase(VBox scrollBox,TextArea textArea,VBox dataBox) {
        Thread listDatabase = new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Connect connect = new Connect();
                        if (connect.sendMessage("list database",ClientUI.IP,Integer.valueOf(port),ClientUI.userName,ClientUI.Passwd))
                        {
                            textArea.setText(textArea.getText()+"\nSuccessful Command={list database};");
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
                                    ClientUI.dataNow = button.getId();
                                    scrollBox.getChildren().clear();
                                    ClientUI.listDatabase(scrollBox,textArea,dataBox);
                                    textArea.setText(textArea.getText()+"\nGet All Data From: "+splitDatabase[finalI]+"; ["+action.Func.getNowTime()+"]");
                                    dataBox.getChildren().clear();
                                    ClientUI.dataLoader(dataBox,splitDatabase[finalI],ClientUI.stage,textArea);
                                });
                            }
                        }else {
                            textArea.setText(textArea.getText()+"\nConnect Error!");
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
        listDatabase.start();
    }
    public static void main(String[] args) {
        ClientUI.launch();
    }
    public static void dataLoader(VBox box,String database,Stage stage,TextArea logOut) {
        box.setPadding(new Insets(20));

        HBox tableOption = new HBox();
        tableOption.setPrefWidth(stage.getWidth());
        tableOption.setPadding(new Insets(10));
        tableOption.setSpacing(10);

        Button createData = lib.buttonUI.button1("Create the Data");
        tableOption.getChildren().addAll(createData);

        createData.setOnAction((ActionEvent e)-> {
            CreateData createDataWin = new CreateData();
            try{
                System.out.println("Select="+ClientUI.dataNow);
                createDataWin.setDatabase(ClientUI.dataNow);
                createDataWin.setBox(logOut,box);
                createDataWin.start(new Stage());
            }catch (Exception exception){
                exception.printStackTrace();
            }
        });

        box.getChildren().add(tableOption);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Connect connect = new Connect();
                        Boolean b1 = connect.sendMessage("view "+database,ClientUI.IP,Integer.valueOf(port),ClientUI.userName,ClientUI.Passwd);

                        Label title = new Label("Name | ");
                        Label value = new Label("Value | ");
                        Label type = new Label("Type | ");
                        Label createTime = new Label("Create Time | ");
                        Label updateTime = new Label("Update Time | ");
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
                                        "-fx-border-width: 0.3;" +
                                        "-fx-background-color: white");
                                hBox.setMinHeight(28);
                                hBox.setPadding(new Insets(5));
                                hBox.setId(split[i].split("  |  ")[0]);

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
                                        hBox.setStyle("" +
                                                "-fx-border-color: black;" +
                                                "-fx-border-width: 0.3;"+
                                                "-fx-background-color: #ccc");
                                    }
                                });
                                hBox.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        hBox.setEffect(null);
                                        hBox.setStyle("" +
                                                "-fx-border-color: black;" +
                                                "-fx-border-width: 0.3;"+
                                                "-fx-background-color: white");
                                    }
                                });
                                int finalI = i;
                                hBox.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        try{
                                            System.out.println(split[finalI]);
                                            String Data = split[finalI];
                                            String[] splitData = Data.split("  |  ");

                                            System.out.println(new ArrayList<String>(Arrays.asList(splitData)));
                                            String name = splitData[0];
                                            String value = splitData[2];
                                            String type = splitData[4];
                                            String createTime = splitData[6];
                                            String update = splitData[8];
                                            String note = splitData[10];
                                            System.out.println("Name="+name+";\n" +
                                                    "Value="+value+";\n" +
                                                    "Type="+type+";\n" +
                                                    "CreateTime="+createTime+";\n" +
                                                    "Update="+update+";\n" +
                                                    "Note="+note);

                                            DataOptions dataOptions = new DataOptions();
                                            dataOptions.setBox(hBox,logOut,box);
                                            dataOptions.setDatabase(database);
                                            dataOptions.setData(name,type,update,createTime,value,note);
                                            dataOptions.setID(hBox.getId());
                                            dataOptions.start(new Stage());
                                        }catch (Exception exception){
                                            exception.printStackTrace();
                                        }
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
