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
        this.name = Func.replaceHead(name);
        this.type = Func.replaceHead(type);
        this.update = Func.replaceHead(update);
        this.createTime = Func.replaceHead(createTime);
        this.value = Func.replaceHead(value);
        this.note = Func.replaceHead(note);
    }
    public void setBox(HBox hBox,TextArea logOut) {
        this.boxPanel = hBox;
        this.logOut = logOut;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(380);
        primaryStage.setHeight(300);
        primaryStage.setTitle("Edit: "+this.dataID+" -");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
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

        Button exit = lib.buttonUI.button1("Cancel");
        Button ok = lib.buttonUI.button1("OK");
        bottomBtn.getChildren().addAll(exit,ok);
        bottomBtn.setSpacing(10);

        exit.setOnAction((ActionEvent e) ->{
            primaryStage.close();
        });
        ok.setOnAction((ActionEvent e)->{
            String getName = nameText.getText();
            String getValue = valueText.getText();
            String getNote = noteText.getText();

           if (getName.equals(this.name) && getValue.equals(this.value) && getNote.equals(this.note)) {
               primaryStage.close();
           }else if (getName.equals(this.name) && getValue.equals(this.value)) {
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
