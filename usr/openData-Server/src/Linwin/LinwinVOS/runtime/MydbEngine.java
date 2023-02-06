package LinwinVOS.runtime;
import LinwinVOS.FileSystem.*;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.api.*;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class MydbEngine {
    private String getFunction = "";
    private String User;
    private String usersPhysicalPath;
    private ExecutorService executorService = null;

    public String getReturn() {
        return this.getFunction;
    }
    public void setUser(String user) {
        this.User = user;
        String getRunPath = LinwinVOS.UsersNowPath.get(this.User);
        this.usersPhysicalPath = LinwinVOS.DatabasePath+"/"+user+getRunPath;
    }
    public void setExecutorService(ExecutorService executorService){
        this.executorService = executorService;
    }
    public String getUser() {
        return this.User;
    }
    public void execLdbScript(String script,String user) {
        try{
            Exec exec = new Exec();
            exec.setEngine(this);
            exec.setFuture(executorService);
            script = MydbEngine.replaceSpace(script);
            script = MydbEngine.replaceLastSpace(script);
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
            else if (script.substring(0,6).equals("rename")) {
                this.getFunction = exec.ReName(user,script);
            }else if (script.substring(0,6).equals("update")) {
                this.getFunction = exec.UpdateDatabase(user,script);
            }else if (script.substring(0,5).equals("index")) {
                this.getFunction = exec.Index(user,script);
            }
            else if(script.substring(0,6).equals("redata")) {
                this.getFunction = exec.ReData(user,script);
            }
            else if (script.substring(0,4).equals("copy")) {
                this.getFunction = exec.Copy(user,script);
            }
            else if (script.substring(0,4).equals("view")) {
                this.getFunction = exec.View(user,script);
            }
            else if (script.substring(0,4).equals("sudo")) {
                Sudo sudo = new Sudo();
                this.getFunction = sudo.dealCommand(script);
            }

            else if (script.substring(0,8).equals("shutdown")) {
                if (exec.shutdownDatabase(user)) {
                    this.getFunction = "Shutdown successful";
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Thread.sleep(500);
                                System.exit(0);
                            }catch (Exception exception){
                                exception.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }else {
                    this.getFunction = "Only root user can shutdown the database";
                }
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
    public static String replaceLastSpace(String str) {
        int length = str.length();
        String getSpaceStr = null;
        for (int i = 0 ; i < length ;i++) {
            String charset = str.substring(length-i-1,length-i);
            if (!charset.equals(" ")) {
                String Code = str.substring(0,str.lastIndexOf(charset)+1);
                getSpaceStr = Code;
                break;
            }
        }
        return getSpaceStr;
    }
}
