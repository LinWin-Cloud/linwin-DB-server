package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.runtime.Exec;
public class Move {
    public static String move(String user,String command) {
        try{
            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            String[] split = command.split(" ");

            String resource = split[1];
            String target = split[2];

            resource = resource.substring(resource.indexOf("'")+1,resource.lastIndexOf("'"));
            target = target.substring(target.indexOf("'")+1,target.lastIndexOf("'"));

            VosDatabase vosDatabase = usersFileSystem.get(resource);
            if (vosDatabase == null) {
                return "Do not find resource database";
            }else {
                Exec exec = new Exec();
                exec.Copy(user,"copy '"+resource+"' '"+target+"'");
                exec.deleteData(user,"delete database "+ resource);
                return "Successful!\n";
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
