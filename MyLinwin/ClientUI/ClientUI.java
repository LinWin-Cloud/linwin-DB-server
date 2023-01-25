import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClientUI extends Application {
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientUI.stage = primaryStage;
        primaryStage.setWidth(1000);
        primaryStage.setHeight(750);

        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Menu file = new Menu("File");
        Menu terminal = new Menu("Terminal");
        Menu project = new Menu("Project");
        Menu help = new Menu("Help");

        MenuItem newMydb = new MenuItem("New Mydb File");
        MenuItem newMys = new MenuItem("New Mys File");
        MenuItem startProject = new MenuItem("Open Project");
        MenuItem setConnect = new MenuItem("Set Connect to LinwinDB");
        MenuItem close = new MenuItem("Exit");

        MenuItem showTerminal = new MenuItem("New LinwinDB Terminal");
        MenuItem command = new MenuItem("Command");

        MenuItem OpenProject = new MenuItem("Open Project");
        MenuItem Connect = new MenuItem("Connect");

        MenuItem doc = new MenuItem("Document");
        MenuItem pro = new MenuItem("Linwin Data Server Project");
        MenuItem about = new MenuItem("About");

        file.getItems().addAll(newMydb,newMys,startProject,setConnect,close);
        terminal.getItems().addAll(showTerminal,command);
        project.getItems().addAll(OpenProject,Connect);
        help.getItems().addAll(doc,pro,about);

        menuBar.getMenus().addAll(file,terminal,project,help);
        gridPane.getChildren().add(menuBar);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try{
                    Logon logon = new Logon();
                    logon.start(new Stage());
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });
    }
    public static void main(String[] args) {
        ClientUI.launch();
    }
}
