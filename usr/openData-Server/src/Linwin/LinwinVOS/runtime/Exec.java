/**
 * Mys Script Runtime.
 * Author: LinwinCloud.
 * Start time: 2023.1.14
 */

package LinwinVOS.runtime;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Mirror.MirrorHost;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.DataLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import LinwinVOS.outPut.OutPutFileSystem;
import LinwinVOS.runtime.lib.*;


public class Exec {
    private MydbEngine mydbEngine;
    public void setEngine(MydbEngine mydbEngine) {
        this.mydbEngine = mydbEngine;
    }
    private ExecutorService executorService = null;
    public void setFuture(ExecutorService executorService) {
        this.executorService = executorService;
    }
    public boolean shutdownDatabase(String user) {
        /**
         * Shutdown the LinwinSQL
         */
        if (user.equals("root")) {
            return true;
        }else {
            return false;
        }
    }
    public String listDatabase(String user) {
        /**
         * 'list database' Command:
         *
         * List all the databases on users group.
         */
        ListDatabase listDatabase = new ListDatabase();
        return listDatabase.listDatabase(user);
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
         *
         * [This is a command to find all the data from user's database.]
         * find data 1
         */
        Find find = new Find();
        return find.find(command,user);
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
         *          get 'name'.update
         *          get 'name'.createTime
         *          get 'name'.note
         *
         * [2]      get 'name'.value in main
         */
        Get get = new Get();
        return get.get(user,command);
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
         * ls default
         */
        Ls ls = new Ls();
        return ls.ls(user,command);
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
         * This mean create a data call 'hello', value is 'hello world', Note is 'LinwinDB',
         * save it in 'main' database.
         *
         * [2] create data 'hello' setting('hello world','LinwinDB') in main
         */

        Boolean isData = false;
        String[] splitCommand = command.split(" ");
        String createName = "";

        try{
            String dataType = splitCommand[1];
            //System.out.println(dataType+";"+dataType.equals("data"));
            if (dataType.equals("data")) {
                isData = true;
            }else {
                isData = false;
            }
            createName = splitCommand[2];
            createName = createName.substring(createName.indexOf("'")+1,createName.lastIndexOf("'"));
        }catch (Exception exception){
            return "Command syntax error!";
        }
        if (isData) {
            try{
                String func = command.substring(command.indexOf(createName)+createName.length()+2,command.lastIndexOf(")"));
                func = func.replace(" ","<Spacex00000x0ds12x657fgh2>");
                String[] splitInput = func.split("','");
                String value = splitInput[0].replace("<Spacex00000x0ds12x657fgh2>"," ");
                String note = splitInput[1].replace("<Spacex00000x0ds12x657fgh2>"," ");
                value = value.substring(value.indexOf("'")+1,value.length());
                note = note.substring(0,note.lastIndexOf("'"));

                String saveDatabase = command.substring(command.lastIndexOf("in ")+3,command.length());
                VosDatabase save = LinwinVOS.FileSystem.get(user).get(saveDatabase);
                if (save == null) {
                    return "Do not find this database";
                }else {
                    int a1 = createName.indexOf("'");
                    int b1 = createName.indexOf(";");
                    int c1 = createName.indexOf(".");
                    int d1 = createName.indexOf(")");
                    int e1 = createName.indexOf("(");

                    if (a1 != -1 || b1 != -1 || c1 != -1 || d1 != -1 || e1 != -1) {
                        return "Mustn't have Special characters: ' ; . ( )";
                    }
                    Data data = new Data();
                    data.setName(createName);
                    data.setCreateTime(Func.getNowTime());
                    data.setNote(note);
                    data.setValue(value);
                    data.setModificationTime(Func.getNowTime());
                    data.setSaveDatabase(saveDatabase);
                    LinwinVOS.FileSystem.get(user).get(saveDatabase).putData(createName,data);
                    LinwinVOS.FileSystem.get(user).get(saveDatabase).setModificationTime(Func.getNowTime());

                    StringBuffer writePath = new StringBuffer(LinwinVOS.DatabasePath+"/"+user+"/Database/"+createName+".mydb");
                    //LinwinVOS.outPutMap.put(createName,new FileWriter(writePath.toString(),false));
                    OutPutFileSystem.writeDatabase(saveDatabase,user);

                    return "Create Successful!\n";
                }
            }catch (Exception exception){
                return "Command syntax error!";
            }
        }else {
            try{
                UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
                VosDatabase vosDatabase = new VosDatabase();
                vosDatabase.setName(createName);
                vosDatabase.setCreateTime(Func.getNowTime());
                vosDatabase.setUser(user);
                vosDatabase.setModificationTime(Func.getNowTime());
                vosDatabase.setSavePath("/");

                usersFileSystem.putDatabase(createName,vosDatabase);
                new File(LinwinVOS.DatabasePath+"/"+user+"/Database/"+vosDatabase.getName()+".mydb").createNewFile();
                LinwinVOS.outPutMap.put(createName,new FileWriter(LinwinVOS.DatabasePath+"/"+user+"/Database/"+createName+".mydb"));
                return "Create Successful!\n";
            }catch (Exception exception){
                return "Do not create new database in the Physical Path";
            }
        }
    }
    public String deleteData(String user,String command) {
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
            if (command.equals("delete *")) {
                File file = new File(LinwinVOS.DatabasePath + "/" + user + "/Database/");
                File[] files = file.listFiles();

                UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
                usersFileSystem.removeAll();

                for (int i = 0; i < files.length; i++) {
                    File ListDB = files[i];
                    if (Func.getLastName(ListDB.getName()).equals(".mydb")) {
                        ListDB.delete();
                        continue;
                    }
                }
                return "Delete Successful!";
            } else {
                String[] splitCommand = command.split(" ");
                Boolean isData = splitCommand[1].equals("data");
                UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
                if (isData) {
                    String dataName = command.substring(command.indexOf("'") + 1, command.lastIndexOf("'"));
                    String DataBase = command.substring(command.lastIndexOf("in ") + 3, command.length());
                    VosDatabase vosDatabase = usersFileSystem.get(DataBase);
                    if (vosDatabase == null) {
                        return "Do not have this database!";
                    } else {
                        vosDatabase.removeData(dataName);
                        OutPutFileSystem.writeDatabase(vosDatabase.getName(),user);
                        return "Delete Successful!\n";
                    }
                } else {
                    String getName = splitCommand[2];
                    VosDatabase vosDatabase = usersFileSystem.get(getName);
                    if (vosDatabase == null) {
                        return "Do not have this database!";
                    } else {
                        try {
                            usersFileSystem.deleteDataBase(getName);
                            File file = new File(LinwinVOS.DatabasePath + "/" + user + "/Database/" + getName + ".mydb");
                            if (file.delete()) {
                                LinwinVOS.outPutMap.remove(vosDatabase.getName());
                                return "Delete Successful!\n";
                            } else {
                                return "Do have Permissions to delete Target File";
                            }
                        } catch (Exception exception) {
                            return "Do have Permissions to delete Target File";
                        }
                    }
                }
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
    public String Info(String user,String command) {
        Info info = new Info();
        return info.info(user,command);
    }
    public String ReName(String user,String command) {
        ReName reName = new ReName();
        return reName.reName(user,command);
    }
    public String UpdateDatabase(String user,String command) {
        File usersDatabase = new File(LinwinVOS.DatabasePath+"/"+user+"/Database");
        File[] listDataBase = usersDatabase.listFiles();

        LinwinVOS.FileSystem.get(user).setLoadOK(false);
        LinwinVOS.FileSystem.get(user).removeAll();
        long start = System.currentTimeMillis();
        DataLoader.UsersLoad(listDataBase,LinwinVOS.FileSystem.get(user),user);
        long end = System.currentTimeMillis();
        LinwinVOS.FileSystem.get(user).setLoadOK(true);
        return "[*]Finish Load All the Data From User: "+user+" Use Time: "+(end-start)+"ms\n";
    }
    public String Index(String user,String command) {
        /**
         * 'index' command:
         * This command is to Indexes the specified from the database.
         *
         * How to use it:
         *
         * (This command is to index all the datas have this word 'hello' in the
         * Database cell 'main')
         * [1] index 'hello' in main
         *
         */
        Index index = new Index();
        return index.IndexCommand(user,command);
    }
    public String ReData(String user,String command) {
        /**
         * 'redata' command:
         * THis command is to revalue the data from the database.
         * For example: you want to Re-modify the data content. You can use this command.
         *
         * How to use it:
         *
         * (This command is to Re-modify the data cell 'hello''s value to 'hello world' from the database 'main')
         * [1] redata 'hello'.value 'hello world' in main
         * [2] redata 'hello'.note 'hello world' in main
         */
        ReData reData = new ReData();
        return reData.reData(user,command);
    }
    public String Copy(String user,String command) {
        /**
         * 'copy' command:
         * Copy other database's content to a database.
         * If the target database is not exists then will create a new one.
         *
         * If the database was have the data in it.this optioning will add Data
         * into database.
         *
         * This command is to copy all the data in the 'main' to 'hello'.
         * [1] copy 'main' 'hello'
         */
        Copy copy = new Copy();
        return copy.copy(user,command);
    }
    public String View(String user,String command) {
        /**
         * 'view' command:
         * This is a command to show all the data in the database the information
         *
         * Show all the data in 'main' database.
         * [1] view 'main'
         */
        View view = new View();
        return view.view(user,command);
    }
    public String Move(String user,String command) {
        /**
         * 'move' command:
         * Move all the data from database to target database.
         *
         * This command mean move the database name 'resource' to target database 'target'.
         * IF the target database is not exists, then will create a new.
         * [1] move 'resource' 'target'
         *
         */
        Move move = new Move();
        return move.move(user,command);
    }
}
