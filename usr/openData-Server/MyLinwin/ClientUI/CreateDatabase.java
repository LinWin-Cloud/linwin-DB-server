import action.Func;
import connect.Connect;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CreateDatabase extends Application {
    private String database;
    public void setDatabase(String database){
        this.database = database;
    }

    private String dataID;
    private String name;
    private String value;
    private String type;
    private String note;
    private String update;
    private String createTime;
    private HBox boxPanel;
    private TextArea logOut;
    private VBox box;
    public void setID(String id) {
        this.dataID = id;
    }
    public String getDataID() {
        return this.dataID;
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
    public void setBox(TextArea logOut,VBox box) {
        this.logOut = logOut;
        this.box = box;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initOwner(ClientUI.stage);
        primaryStage.setTitle("Create new Data");
        primaryStage.setWidth(380);
        primaryStage.setHeight(240);
        primaryStage.setResizable(false);
        primaryStage.show();

        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);

        VBox box = new VBox();
        box.setPadding(new Insets(10));
        box.setSpacing(10);

        Label label = new Label("Create new Data");
        label.setFont(Font.font(25));
        label.setEffect(new Glow());

        HBox name = new HBox();
        HBox value = new HBox();
        HBox note = new HBox();

        Label nameLabel = new Label("Name: ");
        Label valueLabel = new Label("Value: ");
        Label noteLabel = new Label("Note: ");

        nameLabel.setPrefWidth(100);
        valueLabel.setPrefWidth(100);
        noteLabel.setPrefWidth(100);

        TextField nameText = new TextField();
        TextField valueText = new TextField();
        TextField noteText = new TextField();

        nameText.setPrefWidth(260);
        valueText.setPrefWidth(260);
        noteText.setPrefWidth(260);

        Button cancel = lib.buttonUI.button1("Cancel");
        Button ok = lib.buttonUI.button1("OK");
        HBox bottomBox = new HBox();
        bottomBox.setSpacing(10);
        bottomBox.getChildren().addAll(cancel,ok);

        name.getChildren().addAll(nameLabel,nameText);
        value.getChildren().addAll(valueLabel,valueText);
        note.getChildren().addAll(noteLabel,noteText);

        ok.setOnAction((ActionEvent e) -> {
            String getName = nameText.getText();
            String getValue = valueText.getText();
            String getNote = noteText.getText();
            if (getName.equals("") || getValue.equals("") || getNote.equals("")) {
                CreateData.showFunc();
            }
            if (getName == null || getValue == null || getNote == null){
                CreateData.showFunc();
            }else if (
                    getName.replace(" ","").equals("")||
                            getValue.replace(" ","").equals("")||
                            getNote.replace(" ","").equals("")
            )
            {
                CreateData.showFunc();
            }
            else {
                Connect connect = new Connect();
                Boolean b = connect.sendMessage("create data '"+getName+"' setting('"+getValue+"','"+getNote+"') in "+this.database,ClientUI.IP,Integer.valueOf(ClientUI.port),ClientUI.userName,ClientUI.Passwd);
                System.out.println("create data '"+getName+"' setting('"+getValue+"','"+getNote+"') in "+ClientUI.dataNow);
                if (b) {
                    this.box.getChildren().clear();
                    ClientUI.dataLoader(this.box,this.database,ClientUI.stage,this.logOut);
                    this.logOut.setText(this.logOut.getText()+"\n"+"Successful ["+ Func.getNowTime()+"]");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Information");
                    alert.setContentText("Create Successful!");
                    alert.showAndWait();
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
        cancel.setOnAction((ActionEvent e) -> {
            primaryStage.close();
        });

        box.getChildren().addAll(label,name,value,note,bottomBox);
        gridPane.getChildren().addAll(box);
    }
    public static void showFunc() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText("Mustn't have space and Special characters");
        alert.showAndWait();
    }
}
