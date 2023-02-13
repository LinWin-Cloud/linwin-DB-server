package MirrorRuntime.lib;

import MirrorRuntime.outPut.OutPutFileSystem;
import remote.Database;
import remote.Data;
import remote.UserRemote;

import java.io.File;
import java.io.FileWriter;

public class Create {
    public String create(String command) {
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
         *
         *
         * This mean create a data call 'hello', value is 'hello world', Note is 'LinwinDB',
         * save it in 'main' database.
         *
         * [1] create data 'hello' setting('hello world','LinwinDB') in main
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
                Database save = UserRemote.usersHashMap.get(saveDatabase);

                //System.out.println(value+" "+note+" "+createName+" "+saveDatabase);
                if (save == null) {
                    return "Can not find this database";
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
                    UserRemote.usersHashMap.get(saveDatabase).putData(createName,data);
                    UserRemote.usersHashMap.get(saveDatabase).setModificationTime(Func.getNowTime());

                    OutPutFileSystem.writeDatabase(saveDatabase);

                    return "Create Successful!\n";
                }
            }catch (Exception exception){
                return "Command syntax error!";
            }
        }else {
            return "Command syntax error!";
        }
    }
}
