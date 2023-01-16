package LinwinVOS.runtime;
import LinwinVOS.FileSystem.*;
import LinwinVOS.LinwinVOS;

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
    public void execLdbScript(String script) {
        Exec exec = new Exec();
        exec.setEngine(this);
        script = MydbEngine.replaceSpace(script);

        if (script.indexOf("list database") != -1) {
            String list = exec.listDatabase(script);
            this.getFunction = list;
        }else {
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
