package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.outPut.OutPutFileSystem;
import LinwinVOS.runtime.Func;

public class ReData {
    public String reData(String user,String command) {
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
            if (vosDatabase == null) {
                return "Do not find target database!";
            }else {
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
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
