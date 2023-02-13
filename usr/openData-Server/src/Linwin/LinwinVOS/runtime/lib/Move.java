package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Mirror.MirrorHost;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.runtime.Exec;
public class Move {
    public static boolean isRemote_target = false;
    public static boolean isIsRemote_source = false;
    public static MirrorHost sourceHost;
    public static MirrorHost targetHost;
    public static String move(String user,String command) {
        try{
            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            String[] split = command.split(" ");

            String resource = split[1];
            String target = split[2];

            resource = resource.substring(resource.indexOf("'")+1,resource.lastIndexOf("'"));
            target = target.substring(target.indexOf("'")+1,target.lastIndexOf("'"));

            VosDatabase vosDatabase = usersFileSystem.get(resource);
            if (vosDatabase != null) {
                Exec exec = new Exec();
                exec.Copy(user,"copy '"+resource+"' '"+target+"'");
                exec.deleteData(user,"delete database "+ resource);
                return "Successful!\n";
            }else {
                boolean isSource = false;
                boolean isTarget = false;

                for (MirrorHost mirrorHost : LinwinVOS.FileSystem.get(user).getMirrorHosts())
                {
                    if (mirrorHost.sendCommand("existdb "+resource).replace("\n","").equals("true")) {
                        isSource = true;
                    }
                    if (mirrorHost.sendCommand("existdb "+target).replace("\n","").equals("true")) {
                        isTarget = true;
                    }
                }
                //System.out.println(isSource+" "+isTarget);
                if (!isSource) {
                    return "Can not find resource database!";
                }
                if (!isTarget) {
                    new Exec().create(user,"create database '"+target);
                }
                Copy copy = new Copy();
                String c = copy.copy(user,"copy '"+resource+"' '"+target+"'");
                if (c.replace("\n","").equals("Copy error!")) {
                    return "Move error!";
                }

                Exec exec = new Exec();
                exec.deleteData(user,"delete database "+resource);
                return "Move Successful!\n";
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
