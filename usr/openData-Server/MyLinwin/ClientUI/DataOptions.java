import action.Func;
import connect.Connect;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class DataOptions extends Application {
    private String dataID;
    private String name;
    private String value;
    private String type;
    private String note;
    private String update;
    private String createTime;
    private HBox boxPanel;
    private String database;
    private TextArea logOut;
    private VBox box;
    public void setID(String id) {
        this.dataID = id;
    }
    public String getDataID() {
        return this.dataID;
    }
    public void setDatabase(String database) {
        this.database = database;
    }
    public void setData(String name,String type,String update,String createTime,String value,String note) {
        /*
        this.name = Func.replaceHead(name);
        this.type = Func.replaceHead(type);
        this.update = Func.replaceHead(update);
        this.createTime = Func.replaceHead(createTime);
        this.value = Func.replaceHead(value);
        this.note = Func.replaceHead(note);
         */
        this.name = name;
        this.type = type;
        this.update = update;
        this.createTime = createTime;
        this.value = value;
        this.note = note;
    }
    public void setBox(HBox hBox,TextArea logOut,VBox box) {
        this.boxPanel = hBox;
        this.logOut = logOut;
        this.box = box;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initOwner(ClientUI.stage);
        primaryStage.requestFocus();
        primaryStage.setWidth(380);
        primaryStage.setHeight(300);
        primaryStage.setTitle("Edit: "+this.dataID+" -");
        primaryStage.setResizable(false);
        primaryStage.show();

        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);

        VBox box = new VBox();
        box.setPadding(new Insets(20));
        box.setSpacing(10);

        HBox name = new HBox();
        HBox value = new HBox();
        HBox note = new HBox();

        Label nameLabel = new Label("Name: ");
        Label valueLabel = new Label("Value: ");
        Label noteLabel = new Label("Note: ");

        nameLabel.setPrefWidth(100);
        valueLabel.setPrefWidth(100);
        noteLabel.setPrefWidth(100);

        TextField nameText = new TextField(this.name);
        TextField valueText = new TextField(this.value);
        TextField noteText = new TextField(this.note);

        nameText.setPrefWidth(260);
        valueText.setPrefWidth(260);
        noteText.setPrefWidth(260);

        Label createTime_Label = new Label("Create Time: "+this.createTime);
        Label updateTime_Label = new Label("Update Time: "+this.update);
        Label type_Label = new Label("Type: "+this.type);

        HBox bottomBtn = new HBox();

        Button delete = lib.buttonUI.button1("Delete");
        Button exit = lib.buttonUI.button1("Cancel");
        Button ok = lib.buttonUI.button1("OK");
        bottomBtn.getChildren().addAll(delete,exit,ok);
        bottomBtn.setSpacing(10);

        delete.setOnAction((ActionEvent e)->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Dialog!");
            alert.setContentText("Do you want to delete: "+this.name);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // ... user chose OK
                Connect connect = new Connect();
                Boolean b = connect.sendMessage("delete data '"+this.name+"' in "+this.database,ClientUI.IP,Integer.valueOf(ClientUI.port),ClientUI.userName,ClientUI.Passwd);
                if (b) {
                    this.box.getChildren().clear();
                    ClientUI.dataLoader(this.box,database,ClientUI.stage,this.logOut);
                    this.logOut.setText(this.logOut.getText()+"\n"+"Successful! ["+Func.getNowTime()+"]");
                    primaryStage.close();
                }else {
                    Alert alerts = new Alert(Alert.AlertType.ERROR);
                    alerts.setTitle("Connect Error");
                    alerts.setHeaderText("Connect error");
                    alerts.setContentText("There is a error about connect to the Server");
                    alerts.showAndWait();
                }
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        });

        exit.setOnAction((ActionEvent e) ->{
            primaryStage.close();
        });
        ok.setOnAction((ActionEvent e)->{
            String getName = nameText.getText();
            String getValue = valueText.getText();
            String getNote = noteText.getText();

           if (getName.equals(this.name) && getValue.equals(this.value) && getNote.equals(this.note)) {
               primaryStage.close();
           }else if (getName.equals(this.name) && getValue.equals(this.value) && !getNote.equals(this.note)) {
                /**
                 * Can reData the 'note';
                 */
                String command = "redata '"+this.name+"'.note '"+getNote+"' in "+this.database;
                Connect connect = new Connect();
                Boolean b = connect.sendMessage(command,ClientUI.IP,Integer.valueOf(ClientUI.port),ClientUI.userName,ClientUI.Passwd);
                if (b){
                    //logOut.setText(logOut.getText()+"\nConnect to server successful [Update Data From: "+this.database+"]");
                    logOut.setText(logOut.getText()+"\n"+"["+Func.getNowTime()+"] ["+this.database+"] "+connect.getMessage());
                    primaryStage.close();
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Connect Error");
                    alert.setHeaderText("Connect error");
                    alert.setContentText("There is a error about connect to the Server");
                    alert.showAndWait();
                }
           }
           else if (getName.equals(this.name) && getNote.equals(this.note) && !getValue.equals(this.value)) {
               String command = "redata '"+this.name+"'.value '"+getValue+"' in "+this.database;
               Connect connect = new Connect();
               Boolean b = connect.sendMessage(command,ClientUI.IP,Integer.valueOf(ClientUI.port),ClientUI.userName,ClientUI.Passwd);
               if (b){
                   //logOut.setText(logOut.getText()+"\nConnect to server successful [Update Data From: "+this.database+"]");
                   logOut.setText(logOut.getText()+"\n"+"["+Func.getNowTime()+"] ["+this.database+"] "+connect.getMessage());
                   primaryStage.close();
               }else {
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Connect Error");
                   alert.setHeaderText("Connect error");
                   alert.setContentText("There is a error about connect to the Server");
                   alert.showAndWait();
               }
           }
           else if (!getName.equals(this.name) && getNote.equals(this.note) && getValue.equals(this.value)) {
               String command = "rename data '"+this.name+"' '"+getName+"' in "+this.database;
               Connect connect = new Connect();
               Boolean b = connect.sendMessage(command,ClientUI.IP,Integer.valueOf(ClientUI.port),ClientUI.userName,ClientUI.Passwd);
               if (b){
                   //logOut.setText(logOut.getText()+"\nConnect to server successful [Update Data From: "+this.database+"]");
                   logOut.setText(logOut.getText()+"\n"+"["+Func.getNowTime()+"] ["+this.database+"] "+connect.getMessage());
                   //ClientUI.dataLoader(box,dataID,ClientUI.stage,this.logOut);
                   primaryStage.close();
               }else {
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Connect Error");
                   alert.setHeaderText("Connect error");
                   alert.setContentText("There is a error about connect to the Server");
                   alert.showAndWait();
               }
           }else {
               String command = "redata '" + this.name + "'.value '" + getValue + "' in " + this.database;
               String command1 = "redata '" + this.name + "'.note '" + getNote + "' in " + this.database;
               String command2 = "rename data '" + this.name + "' '" + getName + "' in " + this.database;
               Connect connect = new Connect();
               Connect connect1 = new Connect();
               Connect connect2 = new Connect();
               Boolean b = connect.sendMessage(command, ClientUI.IP, Integer.valueOf(ClientUI.port), ClientUI.userName, ClientUI.Passwd);
               connect1.sendMessage(command1, ClientUI.IP, Integer.valueOf(ClientUI.port), ClientUI.userName, ClientUI.Passwd);
               connect2.sendMessage(command2, ClientUI.IP, Integer.valueOf(ClientUI.port), ClientUI.userName, ClientUI.Passwd);
               if (b) {
                   //logOut.setText(logOut.getText()+"\nConnect to server successful [Update Data From: "+this.database+"]");
                   logOut.setText(logOut.getText() + "\n" + "[" + Func.getNowTime() + "] [" + this.database + "] " + connect.getMessage());
                   primaryStage.close();
               } else {
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Connect Error");
                   alert.setHeaderText("Connect error");
                   alert.setContentText("There is a error about connect to the Server");
                   alert.showAndWait();
               }
           }
           this.box.getChildren().clear();
           ClientUI.dataLoader(this.box,this.database,ClientUI.stage,this.logOut);
        });
        name.getChildren().addAll(nameLabel,nameText);
        value.getChildren().addAll(valueLabel,valueText);
        note.getChildren().addAll(noteLabel,noteText);
        box.getChildren().addAll(name,value,note,createTime_Label,updateTime_Label,type_Label,bottomBtn);
        gridPane.getChildren().add(box);
        ClientUI.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                primaryStage.close();
            }
        });
    }
}
