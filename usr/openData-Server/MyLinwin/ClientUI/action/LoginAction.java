package action;

import javafx.scene.control.TextField;
import connect.Connect;
public class LoginAction {
    public LoginAction(int id) {
        this.id = id;
    }
    private int id;
    public static TextField Server = null;
    public static TextField UserName = null;
    public static TextField Passwd = null;
    public static TextField port = null;

    public TextField getServer() {
        return this.Server;
    }
    public TextField getUserName() {
        return this.UserName;
    }
    public TextField getPasswd() {
        return this.Passwd;
    }
    public void setServer(TextField textField) {
        this.Server = textField;
    }
    public void setUserName(TextField textField) {
        this.UserName = textField;
    }
    public void setPasswd(TextField textField) {
        this.Passwd = textField;
    }
    public int getId() {
        return this.id;
    }
    public static Boolean connectRemote(String ip,String user,String port,String passwd) {
        String[] command = {ip,port,user,passwd,"list database"};
        Connect connect = new Connect();
        return connect.connect(command);
    }
}
