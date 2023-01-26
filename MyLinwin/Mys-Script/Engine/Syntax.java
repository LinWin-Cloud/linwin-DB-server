package Engine;

import java.io.File;

import main.Connect;

public class Syntax {
    private String user;
    private String passwd;
    private String remote;
    private String port;
    public void setUser(String user) {
        this.user = user;
    }
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    public void setRemote(String remote) {
        this.remote = remote;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public Boolean isAnnotated(String code) {
        Boolean toBool = false;
        try{
            if (code.substring(0,2).equals("//")) {
                toBool = true;
            }
            if (code.substring(0,3).equals("/**")) {
                toBool = true;
            }
            if (code.substring(0,1).equals("*")) {
                toBool = true;
            }
            if (code.substring(0,1).equals("#")) {
                toBool = true;
            }
        }catch (Exception exception){}
        return toBool;
    }
    public String UpLoad_Command(String command,String path) {
        try{
            String upLoadPath = command.substring(command.indexOf("'")+1,command.lastIndexOf("'"));
            File filePath1 = new File(upLoadPath);
            File filePath2 = new File(System.getProperty("user.dir")+"/"+upLoadPath);
            if (filePath1.exists()) {
                upLoadPath = filePath1.getAbsolutePath();
            }
            if (filePath2.exists()) {
                upLoadPath = filePath2.getAbsolutePath();
            }

            DbLoader dbLoader = new DbLoader();
            for (String i : dbLoader.LoadDB_CommandScript(upLoadPath))
            {
                if (Connect.sendMessage(i,this.remote,Integer.valueOf(this.port),this.user,this.passwd)) {
                    continue;
                }else {
                    return "Error Options!";
                }
            }
            return "Run Successful!";
        }catch (Exception exception){
            return "Error command syntax";
        }
    }
    public static String getFileName(String str) {
        try{
            return str.substring(0,str.lastIndexOf("."));
        }catch (Exception exception){
            return str;
        }
    }
}
