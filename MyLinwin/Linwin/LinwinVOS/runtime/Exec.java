/**
 * Mys Script Runtime.
 * Author: LinwinCloud.
 * Start time: 2023.1.14
 */

package LinwinVOS.runtime;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;

import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
                String[] findData = {""};

                UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
                HashSet<VosDatabase> databases = usersFileSystem.getDatabase();
                ExecutorService executorService = Executors.newFixedThreadPool(1);
                Future<Integer> future = null;
                for (VosDatabase vosDatabase : databases) {
                    final String index = findIndex;
                     future = executorService.submit(new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            for (Data data : vosDatabase.getListData()) {
                                String name = data.getName();
                                int s = name.indexOf(index);
                                if (s != -1) {
                                    findData[0] = findData[0] + name + "\n";
                                }
                            }
                            return 0;
                        }
                    });
                }
                try{
                    future.get();
                }catch (Exception exception){
                    exception.printStackTrace();
                }
                executorService.shutdownNow();
                return findData[0];
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
                return "Do not find datas";
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
            Boolean findDatabase = false;
            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            for (VosDatabase vosDatabase : usersFileSystem.getDatabase()){
                String name = vosDatabase.getName();
                if (name.equals(getFIND_DATABASE)) {
                    findDatabase = true;
                    Data data = vosDatabase.getData(getDataName);
                    if (data != null){
                        if (getDataValue.equals("value")) {
                            Result = data.getValue() + "\n";
                            break;
                        }
                        if (getDataValue.equals("type")) {
                            Result = data.getType() + "\n";
                            break;
                        }
                        if (getDataValue.equals("update")) {
                            Result = data.getModificationTime();
                            break;
                        }
                        if (getDataValue.equals("createTime")) {
                            Result = data.getCreateTime() + "\n";
                            break;
                        }
                        if (getDataValue.equals("note")) {
                            Result = data.getNote() + "\n";
                            break;
                        }else {
                            Result = "Command Value Error!";
                            break;
                        }
                    }else {
                        return "Do not find target data.";
                    }
                }
            }
            if (!findDatabase) {
                return "Do not find database.";
            }else {
                return Result;
            }
        }else {
            return "Command Value Error! Error="+command;
        }
    }
    public String LsDatabase(String user,String command) {
        /**
         * 'ls' command:
         *
         * This is a command to list all the data in the database.
         * Update version in: 1.0
         *
         * How to use:
         * [This command is to list all the data in database name 'default']
         * ls 'default'
         */
        try{
            String listDatabase = command.substring(3,command.length());
            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            VosDatabase vosDatabase = usersFileSystem.get(listDatabase);

            StringBuffer stringBuffer = new StringBuffer("");

            if (vosDatabase == null){
                return "Do not find target database";
            }else {
                for (Data data : vosDatabase.dataHashMap.values()) {
                    stringBuffer.append(data.getName());
                    stringBuffer.append("\n");
                }
            }
            String result = stringBuffer.toString();
            if (result.equals("")) {
                return "Do not have data in the database.";
            }else {
                return result;
            }
        }catch (Exception exception) {
            return "Command syntax error!";
        }
    }
    public String create(String user,String command) {
        /**
         * 'create' command:
         *
         * This is a command to create new data or database.
         *
         * How to use:
         * (Notice: The database's name can not have space,or the Linwin Data Server
         * when load the database will replace the space
         * For example: You write the database Name 'hello world',Linwin
         * Data Server will deal to 'helloworld'.
         * )
         *
         * [1] create database 'helloworld'             [This is to create a database name cell 'helloworld']
         *
         * The content 'setting''s Input Value;
         * 1. 'hello world' is your want to save the value of this data.
         * 2. 'LinwinDB' is your want to save the note of this data.
         * The UpdateTime,CreateTime and the Type Value The LinwinDB will deal these
         * and you needn't worry these.
         *
         * [2] create data 'hello' setting('hello world','LinwinDB')
         */

        Boolean isData = false;
        String[] splitCommand = command.split(" ");
        String createName = "";

        try{
            String dataType = splitCommand[1];
            if (dataType.equals("data")) {
                isData = true;
            }if (dataType.equals("database")) {
                isData = false;
            }else {
                return "Error Command Value";
            }
            createName = splitCommand[2];
            createName = createName.substring(createName.indexOf("'")+1,createName.lastIndexOf("'"));
        }catch (Exception exception){
            return "Command syntax error!";
        }
        if (isData) {

        }else {
            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            VosDatabase vosDatabase = new VosDatabase();
            vosDatabase.setName(createName);
            vosDatabase.setCreateTime(Func.getNowTime());
            vosDatabase.setUser(user);
            vosDatabase.setModificationTime(Func.getNowTime());
            vosDatabase.setSavePath("/");

            usersFileSystem.putDatabase(createName,vosDatabase);
            return "Create Successful!";
        }
    }
}
