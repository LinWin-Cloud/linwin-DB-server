package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;

public class View {
    public String view(String user,String command) {
        try{
            String database = command.substring(command.indexOf(" ")+1);
            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            VosDatabase vosDatabase = usersFileSystem.get(database);
            if (vosDatabase == null) {
                return "Do not find target database";
            }else {
                
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
