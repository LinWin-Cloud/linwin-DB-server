package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Mirror.MirrorHost;
import LinwinVOS.Users.UsersFileSystem;

import java.io.File;
import java.nio.file.FileSystem;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class Get {
    private static StringBuffer Result = new StringBuffer("");
    public String get(String user,String command)
    {
        String[] getCommand = command.split(" ");
        int commandLength = getCommand.length;

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
                        Get.Result.append(data.getValue());
                        continue;
                    }
                    if (getDataValue.equals("type")) {
                        Get.Result.append(data.getType());
                        continue;
                    }
                    if (getDataValue.equals("update")) {
                        Get.Result.append(data.getModificationTime());
                        continue;
                    }
                    if (getDataValue.equals("createTime")) {
                        Get.Result.append(data.getCreateTime());
                        continue;
                    }
                    if (getDataValue.equals("note")) {
                        Get.Result.append(data.getNote());
                        continue;
                    }else {
                        return "Error command Type.";
                    }
                }
            }
            return Get.Result.toString();
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
                        return data.getValue() + "\n";
                    }
                    if (getDataValue.equals("type")) {
                        return data.getType() + "\n";
                    }
                    if (getDataValue.equals("update")) {
                        return data.getModificationTime();
                    }
                    if (getDataValue.equals("createTime")) {
                        return data.getCreateTime() + "\n";
                    }
                    if (getDataValue.equals("note")) {
                        return data.getNote() + "\n";
                    }else {
                        return "Command Value Error!";
                    }
                }else {
                    return "Can not find target data.";
                }
            }
            else if (vosDatabase == null) {
                Future<Integer> future = null;
                for (MirrorHost mirrorHost : LinwinVOS.FileSystem.get(user).getMirrorHosts())
                {
                    String name = getDataName;
                    String value = getDataValue;
                    //String find = getFIND_DATABASE;
                    future = LinwinVOS.executorService.submit(new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            String message = mirrorHost.sendCommand("get '"+name+"'."+value+" in "+getFIND_DATABASE);
                            if (message.equals("Do not find data") || message.equals("Command Value Error")) {
                                return 1;
                            }
                            String[] split = message.split("\n");
                            for (String i : split)
                            {
                                Get.Result.append(i);
                                Get.Result.append("\n");
                            }
                            return 0;
                        }
                    });
                }
                try{
                    future.get();
                }catch (Exception exception){}
                return Get.Result.toString();
            }
            else {
                return "Can not find target database.";
            }
        }else {
            return "Command Value Error! Error="+command;
        }
    }
}
