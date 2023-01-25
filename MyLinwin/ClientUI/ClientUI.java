import action.Json;
import action.LoginAction;
import action.ReadFile;
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
    public static String userName = "";
    public static String Passwd = "";
    public static String port = "";
    public static String IP = "";
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientUI.stage = primaryStage;
        primaryStage.setWidth(1000);
        primaryStage.setHeight(750);

        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);

        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
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

        menuBar.getMenus().addAll(file,terminal,project,help);
        gridPane.getChildren().add(menuBar);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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
            }
        });
    }
    public static void main(String[] args) {
        ClientUI.launch();
    }
}
