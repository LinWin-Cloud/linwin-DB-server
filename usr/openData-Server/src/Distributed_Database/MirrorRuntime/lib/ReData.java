package MirrorRuntime.lib;

import remote.Data;
import remote.Database;
import remote.UserRemote;

public class ReData {
    public String reData(String command) {
        try{
            String TMP = command;
            TMP = TMP.replace("  ","");

            String data = TMP.substring(TMP.indexOf("'")+1,TMP.indexOf("'."));
            TMP = TMP.substring(TMP.indexOf("'.")+2);
            String type = TMP.substring(0,TMP.indexOf(" "));
            String content = TMP.substring(TMP.indexOf("'")+1,TMP.lastIndexOf("'"));
            String database = TMP.substring(TMP.lastIndexOf("in ")+3);

            Database vosDatabase = UserRemote.usersHashMap.get(database);
            if (vosDatabase != null) {
                Data getData = vosDatabase.getData(data);
                if (getData == null) {
                    return "Can not find target data";
                }else {
                    if (type.equals("value")) {
                        getData.setValue(content);
                        getData.setModificationTime(Func.getNowTime());
                        vosDatabase.putData(data,getData);
                        UserRemote.usersHashMap.put(database,vosDatabase);
                        return "Successful!\n";
                    }if (type.equals("note")) {
                        getData.setNote(content);
                        getData.setModificationTime(Func.getNowTime());
                        vosDatabase.putData(data,getData);
                        UserRemote.usersHashMap.put(database,vosDatabase);
                        return "Successful!\n";
                    }else {
                        return "Error Type";
                    }
                }
            }else {
                return "Can not find target database";
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
