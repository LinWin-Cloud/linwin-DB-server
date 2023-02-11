package MirrorRuntime;

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
}
