package MirrorRuntime;

import MirrorRuntime.lib.Create;
import MirrorRuntime.lib.Get;
import MirrorRuntime.lib.ReData;
import MirrorRuntime.outPut.OutPutFileSystem;
import remote.Data;
import remote.Database;
import remote.Loader;
import remote.UserRemote;

import java.io.File;

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
    public String LS_Database(String command) {
        StringBuffer stringBuffer = new StringBuffer("");
        try
        {
            String target = command.substring(command.indexOf(" ")+1);
            Database database = UserRemote.usersHashMap.get(target);
            if (database == null)
            {
                return "Can not find target database";
            }
            else {
                for (Data data : database.getListData())
                {
                    stringBuffer.append(data.getName());
                    stringBuffer.append("\n");
                }
                return stringBuffer.toString();
            }
        }
        catch (Exception exception)
        {
            return "Command syntax error!";
        }
    }
    public String findData(String command) {
        try {
            StringBuffer stringBuffer = new StringBuffer("");
            String findIndex = command.substring(command.indexOf(" ")+1);

            for (Database database : UserRemote.usersHashMap.values())
            {
                for (Data data : database.getListData())
                {
                    if (data.getName().indexOf(findIndex) != -1) {
                        stringBuffer.append(data.getName());
                        stringBuffer.append("\n");
                    }
                }
            }
            return stringBuffer.toString();
        }
        catch (Exception exception){
            return "Command syntax error!";
        }
    }
    public String get(String command) {
        Get get = new Get();
        return get.get(command);
    }
    public String reData(String command) {
        ReData reData = new ReData();
        return reData.reData(command);
    }
    public String existDatabase(String command) {
        try
        {
            String name = command.substring(command.indexOf(" ")+1);
            Database database = UserRemote.usersHashMap.get(name);
            if (database == null) {
                return  "false";
            }else {
                return "true";
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
    public String create(String command) {
        Create create = new Create();
        return create.create(command);
    }
    public String viewDatabase(String command)
    {
        try
        {
            String targetView = command.substring(command.indexOf(" ")+1);
            Database database = UserRemote.usersHashMap.get(targetView);

            if (database != null) {
                StringBuffer stringBuffer = new StringBuffer("");
                for (Data data : database.getListData()) {
                    stringBuffer.append(data.getName());
                    stringBuffer.append("---");
                    stringBuffer.append(data.getValue());
                    stringBuffer.append("---");
                    stringBuffer.append(data.getType());
                    stringBuffer.append("---");
                    stringBuffer.append(data.getCreateTime());
                    stringBuffer.append("---");
                    stringBuffer.append(data.getModificationTime());
                    stringBuffer.append("---");
                    stringBuffer.append(data.getNote());
                    stringBuffer.append("---");
                    stringBuffer.append("\n");
                }
                return stringBuffer.toString()+"\n";
            }   else {
                return "Can not find target database";
            }
        }
        catch (Exception exception) {
            return "Command syntax error!";
        }
    }
    public String delete(String command)
    {
        /**
         * 'delete' command:
         * delete the data or the database.
         *
         * How to use:
         * (This command is to delete the database name call 'hello')
         * [1] delete database hello
         *
         * (This command is to delete the data from the database name call 'helloDB')
         * [2] delete data 'hello' in helloDB
         *
         * (This command will delete all the Mydb Database and data on user's FileSystem)
         * [3] delete *
         */
        try {
            String[] splitCommand = command.split(" ");
            Boolean isData = splitCommand[1].equals("data");
            if (isData) {
                String dataName = command.substring(command.indexOf("'") + 1, command.lastIndexOf("'"));
                String DataBase = command.substring(command.lastIndexOf("in ") + 3, command.length());
                Database vosDatabase = UserRemote.usersHashMap.get(DataBase);
                if (vosDatabase == null) {
                    return "Can not have this database!";
                } else {
                    vosDatabase.removeData(dataName);
                    OutPutFileSystem.writeDatabase(vosDatabase.getName());
                    return "Delete Successful!\n";
                }
            } else {
                String getName = splitCommand[2];
                Database vosDatabase = UserRemote.usersHashMap.get(getName);
                if (vosDatabase != null) {
                    try {
                        UserRemote.usersHashMap.remove(getName);
                        File file = new File("../../../../opt/LinwinRemote/RemoteData/" + getName + ".mydb");
                        if (file.delete()) {
                            return "Delete Successful!\n";
                        } else {
                            return "No Permissions to delete Target File";
                        }
                    } catch (Exception exception) {
                        return "No Permissions to delete Target File";
                    }
                } else {
                    return "Can not find target database";
                }
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
    public String update() {
        UserRemote.usersHashMap.clear();
        Loader loader = new Loader();
        loader.loadUser();
        return "Update Successful\n";
    }
}
