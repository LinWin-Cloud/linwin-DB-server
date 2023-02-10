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
}
