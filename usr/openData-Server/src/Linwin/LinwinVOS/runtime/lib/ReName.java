package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.outPut.OutPutFileSystem;
import LinwinVOS.runtime.Exec;
import LinwinVOS.runtime.Func;

public class ReName {
    public String reName(String user,String command) {
        /**
         * 'rename' command:
         * This command is to rename the database or the data.
         * For example: The database name cell 'hello' , you can use
         * this command rename it to 'world'.
         *
         * This command is rename the database cell 'hello' to world.
         * [1] rename database 'hello' 'world'
         *
         * This command to rename the data cell 'data1' to 'data2' from the database
         * name cell 'main'.
         * [2] rename data 'data1' 'data2' in main
         */
        try{
            String[] splitCommand = command.split(" ");
            String type = splitCommand[1];
            Boolean isData = type.equals("data");

            if (isData) {
                String commandMain = command.substring(command.indexOf("'")+1,command.lastIndexOf("'"));
                commandMain = commandMain.replace("  "," ");
                String[] splitMain = commandMain.split("' '");
                String lastName = splitMain[0];
                String NewName = splitMain[1];
                String database = command.substring(command.lastIndexOf("in ")+3);
                UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
                VosDatabase vosDatabase = usersFileSystem.get(database);

                if (vosDatabase != null) {
                    Data data = vosDatabase.getData(lastName);
                    if (data == null) {
                        return "Co not find target data";
                    }else {
                        data.setName(NewName);
                        data.setModificationTime(Func.getNowTime());
                        vosDatabase.removeData(lastName);
                        vosDatabase.putData(NewName,data);
                        OutPutFileSystem.writeDatabase(vosDatabase.getName(),user);
                        return "Rename Successful!\n";
                    }
                }
                else
                {
                    String value = new Get().get(user,"get '"+lastName+"'.value in "+database).replace("\n","");
                    String note = new Get().get(user,"get '"+lastName+"'.note in "+database).replace("\n","");

                    if (value.equals("Can not find target database.") || value.equals("Can not find target data.") || note.equals("Can not find target data.") || note.equals("Can not find target database."))
                    {
                        return "ReName error!";
                    }
                    Exec exec = new Exec();
                    String create = exec.create(user,"create data '"+ NewName +"' setting('"+value+"','"+note+"') in "+database);
                    //System.out.println("create data '"+ NewName +"' setting('"+value+"','"+note+"') in "+database);
                    //System.out.println(database+";");
                    if (create.replace("\n","").equals("Can not find this database")) {
                        return "ReName error! type=2";
                    }
                    exec.deleteData(user,"delete data '"+lastName+"' in "+database);
                    return "ReName Successful!\n";
                }
            }else {
               String dataBase = splitCommand[2];
               String NewWord = splitCommand[3];

               dataBase = dataBase.substring(dataBase.indexOf("'")+1,dataBase.lastIndexOf("'"));
               NewWord = NewWord.substring(NewWord.indexOf("'")+1,NewWord.lastIndexOf("'"));

               UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
               VosDatabase vosDatabase = usersFileSystem.get(dataBase);
               if (vosDatabase == null) {
                   return "Do not find target database";
               }
               else {
                   vosDatabase.setName(NewWord);
                   vosDatabase.setModificationTime(Func.getNowTime());
                   usersFileSystem.deleteDataBase(NewWord);
                   usersFileSystem.putDatabase(NewWord,vosDatabase);
                   return "Rename Successful!\n";
               }
            }
        }catch (Exception exception){
            return "Command syntax error!";
        }
    }
}
