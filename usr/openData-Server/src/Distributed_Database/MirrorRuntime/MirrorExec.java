package MirrorRuntime;

import MirrorRuntime.lib.Get;
import MirrorRuntime.lib.ReData;
import remote.Data;
import remote.Database;
import remote.UserRemote;

public class MirrorExec {
    public String Show_Database()
    {
        StringBuffer stringBuffer = new StringBuffer("");
        for (Database database : UserRemote.usersHashMap.values())
        {
            stringBuffer.append(database.getName());
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }
    public String LS_Database(String command) {
        StringBuffer stringBuffer = new StringBuffer("");
        try
        {
            String target = command.substring(command.indexOf(" ")+1);
            Database database = UserRemote.usersHashMap.get(target);
            if (database == null)
            {
                return "Can not find target database";
            }
            else {
                for (Data data : database.getListData())
                {
                    stringBuffer.append(data.getName());
                    stringBuffer.append("\n");
                }
                return stringBuffer.toString();
            }
        }
        catch (Exception exception)
        {
            return "Command syntax error!";
        }
    }
    public String findData(String command) {
        try {
            StringBuffer stringBuffer = new StringBuffer("");
            String findIndex = command.substring(command.indexOf(" ")+1);

            for (Database database : UserRemote.usersHashMap.values())
            {
                for (Data data : database.getListData())
                {
                    if (data.getName().indexOf(findIndex) != -1) {
                        stringBuffer.append(data.getName());
                        stringBuffer.append("\n");
                    }
                }
            }
            return stringBuffer.toString();
        }
        catch (Exception exception){
            return "Command syntax error!";
        }
    }
    public String get(String command) {
        Get get = new Get();
        return get.get(command);
    }
    public String reData(String command) {
        ReData reData = new ReData();
        return reData.reData(command);
    }
}
