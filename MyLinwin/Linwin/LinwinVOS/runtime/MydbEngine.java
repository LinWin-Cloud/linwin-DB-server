package LinwinVOS.runtime;
import LinwinVOS.FileSystem.*;
import LinwinVOS.LinwinVOS;

import java.util.List;

public class MydbEngine {
    private String getFunction = "";
    private String User;
    private String usersPhysicalPath;

    public String getReturn() {
        return this.getFunction;
    }
    public void setUser(String user) {
        this.User = user;
        String getRunPath = LinwinVOS.UsersNowPath.get(this.User);
        this.usersPhysicalPath = LinwinVOS.DatabasePath+"/"+user+getRunPath;
    }
    public String getUser() {
        return this.User;
    }
    public void execLdbScript(String script,String user) {
        try{
            Exec exec = new Exec();
            exec.setEngine(this);
            script = MydbEngine.replaceSpace(script);
            if (script == null) {
                return;
            }
            if (script.equals("list database")) {
                String list = exec.listDatabase(user);
                this.getFunction = list;
            }
            else if(script.substring(0,4).equals("find")) {
                String getFind = exec.Find(user,script);
                this.getFunction = getFind;
            }
            else if(script.substring(0,3).equals("get")) {
                this.getFunction = exec.Get(user,script);
            }
            else if(script.substring(0,2).equals("ls")) {
                this.getFunction = exec.LsDatabase(user,script);
            }
            else if(script.substring(0,6).equals("create")) {
                this.getFunction = exec.create(user,script);
            }
            else if(script.substring(0,6).equals("delete")) {
                this.getFunction = exec.deleteData(user,script);
            }else if(script.substring(0,4).equals("info")) {
                this.getFunction = exec.Info(user,script);
            }
            else {
                this.getFunction = "Error Command and Script";
            }
        }catch (Exception exception){
            this.getFunction = "Error Command and Script";
        }
    }
    public static String replaceSpace(String str) {
        int length = str.length();
        String getSpaceStr = null;
        for (int i = 0 ; i < length ;i++) {
            String charset = str.substring(i,i+1);
            if (!charset.equals(" ")) {
                String Code = str.substring(str.indexOf(charset),length);
                getSpaceStr = Code;
                break;
            }
        }
        return getSpaceStr;
    }
}
