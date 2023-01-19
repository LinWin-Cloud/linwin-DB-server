package LinwinVOS.runtime;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;

import java.util.HashSet;
import java.util.List;

public class Exec {
    private MydbEngine mydbEngine;
    public void setEngine(MydbEngine mydbEngine) {
        this.mydbEngine = mydbEngine;
    }
    public String listDatabase(String user) {
        /**
         * 'list database' Command:
         *
         * List all the databases on users group.
         */
        String list = "";
        UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
        HashSet<VosDatabase> databases = usersFileSystem.getDatabase();
        for (VosDatabase vosDatabase : databases) {
            list = list + vosDatabase.getName() + "\n";
        }
        return list;
    }
    public String Find(String user,String command) {
        /**
         * 'Find' Command:
         *
         * This is a command to find all of the data or databases were have a specific char
         * or char groups.
         *
         *
         * How to use this command:
         *
         * [This is a command to find all the databases were have this word 'default']
         * find database default
         */
        String[] getCommand = command.split(" ");

        String FindResult = "";

        if (getCommand.length >= 3) {
            String findType = getCommand[1];
            String findIndex = "";

            if (getCommand.length == 3) {
                findIndex = getCommand[2];
            }else {
                for (int j = 0 ; j < getCommand.length ; j++) {
                    //System.out.println(j+" "+getCommand[j]);
                    if (j >= 2 ) {
                        if(findIndex.equals("")) {
                            continue;
                        }else {
                            findIndex = findIndex + " "+getCommand[j];
                        }
                    }
                }
            }
            if (findType.equals("database")) {
                UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
                HashSet<VosDatabase> databases = usersFileSystem.getDatabase();
                for (VosDatabase vosDatabase : databases) {
                    String databaseName = vosDatabase.getName();
                    int s = databaseName.indexOf(findIndex);
                    if (s != -1) {
                        FindResult = FindResult + databaseName + "\n";
                    }
                }
                return FindResult;
            }else if (findType.equals("data")) {

                UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
                HashSet<VosDatabase> databases = usersFileSystem.getDatabase();
                for (VosDatabase vosDatabase : databases) {
                    HashSet<Data> dataList = vosDatabase.getListData();
                    for (Data data : dataList) {
                        String dataName = data.getName();
                        int s = dataName.indexOf(findIndex);
                        if (s != -1) {
                            FindResult = FindResult + dataName + "\n";
                        }
                    }
                }
                return FindResult;
            }else {
                return "Do Not Find The Target {Error='Send Type Error'}";
            }
        }else {
            return "Command Value Error!";
        }
    }
    public String Get(String user,String command)
    {
        /**
         * 'get' command:
         *
         * This command is to get the data , data type and content.
         * Three are three or four values that the client must to
         * input.
         *
         * How to use:
         * (This function is to get the data "name"'s 'value' attribute.
         * And the DB Service will find all the data in all the databases.)
         * [1]      get 'name'.value
         *          get 'name'.type
         *
         * [2]      get 'name'.value in main
         */
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
                return "Command Value Error! Error=1";
            }
            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            HashSet<VosDatabase> databases = usersFileSystem.getDatabase();

            for (VosDatabase vosDatabase : databases) {
                Data data = vosDatabase.getData(getDataName);
                if (data != null) {
                    if (getDataValue.equals("value")) {
                        Result = Result + data.getValue() + "\n";
                    }
                    if (getDataValue.equals("type")) {
                        Result = Result + data.getType() + "\n";
                    }
                    if (getDataValue.equals("update")) {
                        Result = Result + data.getModificationTime();
                    }
                    if (getDataValue.equals("createTime")) {
                        Result = Result + data.getCreateTime() + "\n";
                    }
                    if (getDataValue.equals("note")) {
                        Result = Result + data.getNote() + "\n";
                    }else {
                        Result = "Command Value Error!";
                    }
                }
            }
            return Result;
        }else if(commandLength == 4) {
            return null;
        }else {
            return "Command Value Error! Error";
        }
    }
}
