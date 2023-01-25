package action;

import javafx.scene.control.TextField;
import client.ClientShell;

public class LoginAction {
    public LoginAction(int id) {
    }
    private int id;
    public static TextField Server;
    public static TextField UserName;
    public static TextField Passwd;
    public static TextField port;

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
        ClientShell.main(command);
        return true;
    }
}
