package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Mirror.MirrorHost;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.outPut.OutPutFileSystem;
import LinwinVOS.runtime.Func;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ReData {
    public static boolean fix = false;

    public String reData(String user, String command) {
        try{
            String TMP = command;
            TMP = TMP.replace("  ","");

            String data = TMP.substring(TMP.indexOf("'")+1,TMP.indexOf("'."));
            TMP = TMP.substring(TMP.indexOf("'.")+2);
            String type = TMP.substring(0,TMP.indexOf(" "));
            String content = TMP.substring(TMP.indexOf("'")+1,TMP.lastIndexOf("'"));
            String database = TMP.substring(TMP.lastIndexOf("in ")+3);

            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            VosDatabase vosDatabase = usersFileSystem.get(database);
            if (vosDatabase != null) {
                Data getData = vosDatabase.getData(data);
                if (getData == null) {
                    return "Do not find target data!";
                }else {
                    if (type.equals("value")) {
                        getData.setValue(content);
                        getData.setModificationTime(Func.getNowTime());
                        OutPutFileSystem.writeDatabase(vosDatabase.getName(),user);
                        return "Successful!\n";
                    }if (type.equals("note")) {
                        getData.setNote(content);
                        getData.setModificationTime(Func.getNowTime());
                        OutPutFileSystem.writeDatabase(vosDatabase.getName(),user);
                        return "Successful!\n";
                    }else {
                        return "Error Type!";
                    }
                }
            }else {
                Future<Integer> future = null;
                for (MirrorHost mirrorHost : usersFileSystem.getMirrorHosts())
                {
                    future = LinwinVOS.executorService.submit(new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            String message = mirrorHost.sendCommand("redata '"+data+"'."+type+" in "+database);
                            if (message.equals("Error Type") || message.equals("Can not find target database") || message.equals("Can not find target data"))
                            {
                                return 1;
                            }else {
                                fix = true;
                                return 0;
                            }
                        }
                    });
                }
                try{
                    future.get();
                }catch (Exception exception){}
                if (fix) {
                    return "Successful!\n";
                }else {
                    return "Create Error!";
                }
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
