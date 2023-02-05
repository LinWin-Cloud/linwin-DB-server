package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.outPut.OutPutFileSystem;
import LinwinVOS.runtime.Func;
public class Copy {
    public String copy(String user,String command) {
        try{
            String[] split = command.split(" ");
            String resource = split[1];
            String target = split[2];

            resource = resource.substring(resource.indexOf("'")+1,resource.lastIndexOf("'"));
            target = target.substring(target.indexOf("'")+1,target.lastIndexOf("'"));

            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            VosDatabase database = usersFileSystem.get(resource);
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
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
