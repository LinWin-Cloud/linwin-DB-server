package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
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
                StringBuffer stringBuffer = new StringBuffer("");
                for (Data data : vosDatabase.getListData()) {
                    stringBuffer.append(data.getName());
                    stringBuffer.append("   |   ");
                    stringBuffer.append(data.getValue());
                    stringBuffer.append("   |   ");
                    stringBuffer.append(data.getType());
                    stringBuffer.append("   |   ");
                    stringBuffer.append(data.getCreateTime());
                    stringBuffer.append("   |   ");
                    stringBuffer.append(data.getModificationTime());
                    stringBuffer.append("   |   ");
                    stringBuffer.append(data.getNote());
                    stringBuffer.append("   |   ");
                    stringBuffer.append("\n");
                }
                return stringBuffer.toString()+"\n";
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
