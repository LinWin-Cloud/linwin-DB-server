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
        List<VosDatabase> databases = usersFileSystem.getDatabase();
        for (int i = 0 ; i < databases.size() ;i++) {
            list = list + databases.get(i).getName() + "\n";
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
                List<VosDatabase> databases = usersFileSystem.getDatabase();
                for (int i = 0 ; i < databases.size() ;i++) {
                    String databaseName = databases.get(i).getName();
                    int s = databaseName.indexOf(findIndex);
                    if (s != -1) {
                        FindResult = FindResult + databaseName + "\n";
                    }
                }
                return FindResult;
            }else if (findType.equals("data")) {

                UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
                List<VosDatabase> databases = usersFileSystem.getDatabase();
                for (int i = 0 ; i < databases.size() ;i++) {
                    List<Data> dataList = databases.get(i).getListData();
                    HashSet<Data> hashSet = new HashSet<>(dataList);
                    for (Data data : hashSet) {
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
         * [1]      get name value
         *
         *
         * 
         */
        int commandLength = command.length();
        if (commandLength == 3) {

        }else if(commandLength == 4) {

        }else {
            return "Command Value Error!";
        }
    }
}
