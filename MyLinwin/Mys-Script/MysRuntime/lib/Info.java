package MysRuntime.lib;
import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Users.UsersFileSystem;

public class Info {
    public String info(String user,String command) {
        /**
         * 'info' command is to show the information of the database.
         *
         * (This command is to get the database cell 'main''s size)
         * [1] info 'main'.size
         *
         * (This command is to get all the value of the data in the database.)
         * [2] info 'main'.value
         */
        try {
            String[] splitCommand = command.split(" ");
            String commandMAIN = splitCommand[1];
            String databaseName = commandMAIN.substring(commandMAIN.indexOf("'") + 1, commandMAIN.lastIndexOf("'"));
            String getType = commandMAIN.substring(commandMAIN.lastIndexOf(".") + 1, commandMAIN.length());

            UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
            VosDatabase vosDatabase = usersFileSystem.get(databaseName);
            if (vosDatabase == null) {
                return "Do not find target database";
            }
            if (getType.equals("size")) {
                return String.valueOf(vosDatabase.getSize())+"\n";
            }
            if (getType.equals("value")) {
                StringBuffer stringBuffer = new StringBuffer("");
                for (Data data : vosDatabase.dataHashMap.values()) {
                    stringBuffer.append(data.getValue());
                    stringBuffer.append("\n");
                }
                return stringBuffer.toString();
            }
            if (getType.equals("update")) {
                return vosDatabase.getModificationTime()+"\n";
            }
            else {
                return "Command syntax error!";
            }
        } catch (Exception exception) {
            return "Command syntax error!";
        }
    }
}
