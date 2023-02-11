package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;

import java.io.File;
import java.nio.file.FileSystem;
import java.util.HashSet;

public class Get {
    public String get(String user,String command)
    {
        String[] getCommand = command.split(" ");
        int commandLength = getCommand.length;
        String Result = "";

        if (commandLength == 2) {
            String getWillGET = getCommand[1];
            String getDataName = "";
            String getDataValue = "";
            try{
                getDataName = getWillGET.substring(getWillGET.indexOf("'")+1,getWillGET.lastIndexOf("'"));
                getDataValue = getWillGET.substring(getWillGET.lastIndexOf(".")+1,getWillGET.length());
                getDataValue = getDataValue.replace(" ","");
            }catch (Exception exception){
                return "Command Value Error!";
            }
            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            HashSet<VosDatabase> databases = usersFileSystem.getDatabase();

            for (VosDatabase vosDatabase : databases) {
                Data data = vosDatabase.getData(getDataName);
                if (data != null) {
                    if (getDataValue.equals("value")) {
                        Result = Result + data.getValue() + "\n";
                        continue;
                    }
                    if (getDataValue.equals("type")) {
                        Result = Result + data.getType() + "\n";
                        continue;
                    }
                    if (getDataValue.equals("update")) {
                        Result = Result + data.getModificationTime();
                        continue;
                    }
                    if (getDataValue.equals("createTime")) {
                        Result = Result + data.getCreateTime() + "\n";
                        continue;
                    }
                    if (getDataValue.equals("note")) {
                        Result = Result + data.getNote() + "\n";
                        continue;
                    }else {
                        Result = "Command Value Error ! Error="+command;
                        break;
                    }
                }
            }
            if (Result.equals("")) {
                return "Can not find data";
            }else {
                return Result;
            }
        }else if(commandLength == 4) {
            String getWillGET = getCommand[1];
            String getFIND_DATABASE = getCommand[3];
            String getDataName = "";
            String getDataValue = "";
            try{
                getDataName = getWillGET.substring(getWillGET.indexOf("'")+1,getWillGET.lastIndexOf("'"));
                getDataValue = getWillGET.substring(getWillGET.lastIndexOf(".")+1,getWillGET.length());
                getDataValue = getDataValue.replace(" ","");
            }catch (Exception exception){
                return "Command Value Error! Error=1";
            }
            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            VosDatabase vosDatabase = usersFileSystem.get(getFIND_DATABASE);

            if (vosDatabase != null) {
                Data data = vosDatabase.getData(getDataName);
                if (data != null){
                    if (getDataValue.equals("value")) {
                        Result = data.getValue() + "\n";
                    }
                    if (getDataValue.equals("type")) {
                        Result = data.getType() + "\n";
                    }
                    if (getDataValue.equals("update")) {
                        Result = data.getModificationTime();
                    }
                    if (getDataValue.equals("createTime")) {
                        Result = data.getCreateTime() + "\n";
                    }
                    if (getDataValue.equals("note")) {
                        Result = data.getNote() + "\n";
                    }else {
                        Result = "Command Value Error!";
                    }
                }else {
                    return "Can not find target data.";
                }
            }

            return Result;
        }else {
            return "Command Value Error! Error="+command;
        }
    }
}
