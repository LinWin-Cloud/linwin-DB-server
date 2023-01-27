package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;

public class Index {
    public String IndexCommand(String user,String command) {
        StringBuffer stringBuffer = new StringBuffer("");
        UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
        try{
            String FindData = command.substring(command.indexOf("'")+1,command.lastIndexOf("'"));
            int e = command.lastIndexOf("'");
            int s = command.lastIndexOf("in ");
            if (s > e) {
                String DatabaseName = command.substring(s+3);
                VosDatabase vosDatabase = usersFileSystem.get(DatabaseName);
                if (vosDatabase == null) {
                    return "Do not find target database!";
                }else {
                    for (Data data : vosDatabase.getListData()) {
                        String name = data.getName();
                        int find = name.indexOf(FindData);
                        if (find != -1) {
                            stringBuffer.append(name);
                            stringBuffer.append("\n");
                            continue;
                        }
                    }
                    return stringBuffer.toString();
                }
            }else {
                return "Command syntax error!";
            }
        }catch (Exception exception) {
            return "Command syntax error!";
        }
    }
}
