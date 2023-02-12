package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Mirror.MirrorHost;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.outPut.OutPutFileSystem;
import LinwinVOS.runtime.Func;
public class Copy {
    public static boolean isRemote_resource = false;
    public static boolean isRemote_target = false;
    public static MirrorHost sourceHost = null;
    public static MirrorHost targetHost = null;
    public String copy(String user,String command) {
        try{
            String[] split = command.split(" ");
            String resource = split[1];
            String target = split[2];

            resource = resource.substring(resource.indexOf("'")+1,resource.lastIndexOf("'"));
            target = target.substring(target.indexOf("'")+1,target.lastIndexOf("'"));

            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            VosDatabase database = usersFileSystem.get(resource);


            for (MirrorHost mirrorHost : LinwinVOS.FileSystem.get(user).getMirrorHosts())
            {
                if (mirrorHost.sendCommand("existdatabase "+resource).equals("true")) {
                    Copy.isRemote_resource = true;
                    Copy.sourceHost = mirrorHost;
                }
                if (mirrorHost.sendCommand("existdatabase "+target).equals("true")) {
                    Copy.isRemote_target = true;
                    Copy.targetHost = mirrorHost;
                }
            }
            if (Copy.isRemote_resource && Copy.isRemote_target)
            {
                StringBuffer stringBuffer = new StringBuffer("");
                String[] dataList = sourceHost.sendCommand("ls "+resource).split("\n");
                return "";
            }
            else if (Copy.isRemote_target && !Copy.isRemote_resource)
            {
                return "";
            }
            else if (Copy.isRemote_resource && !Copy.isRemote_target)
            {
                return "";
            }
            else {
                if (database == null) {
                    return "Do not find resource database!";
                }else {
                    VosDatabase TargetCopy = usersFileSystem.get(target);
                    if (TargetCopy == null) {
                        VosDatabase vosDatabase = database;
                        vosDatabase.setName(target);
                        vosDatabase.setCreateTime(Func.getNowTime());
                        vosDatabase.setSavePath("/"+user+"/"+target);
                        vosDatabase.setModificationTime(Func.getNowTime());
                        usersFileSystem.putDatabase(target,vosDatabase);

                        OutPutFileSystem.writeDatabase(vosDatabase.getName(),user);
                        return "Copy Successful!\n";
                    }
                    else {
                        for (Data data : database.getListData()) {
                            data.setModificationTime(Func.getNowTime());
                            TargetCopy.putData(data.getName(),data);
                        }
                        usersFileSystem.putDatabase(target,TargetCopy);
                        OutPutFileSystem.writeDatabase(TargetCopy.getName(),user);
                        return "Copy Successful!\n";
                    }
                }
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
