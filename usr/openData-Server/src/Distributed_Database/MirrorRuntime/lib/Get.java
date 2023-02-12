package MirrorRuntime.lib;

import remote.Database;
import remote.UserRemote;
import remote.Data;
import java.util.HashSet;

public class Get {
    public String get(String command) {
        String[] getCommand = command.split(" ");
        int commandLength = getCommand.length;
        String Result = "";

        if (commandLength == 2) {
            String getWillGET = getCommand[1];
            String getDataName = "";
            String getDataValue = "";
            try {
                getDataName = getWillGET.substring(getWillGET.indexOf("'") + 1, getWillGET.lastIndexOf("'"));
                getDataValue = getWillGET.substring(getWillGET.lastIndexOf(".") + 1, getWillGET.length());
                getDataValue = getDataValue.replace(" ", "");
            } catch (Exception exception) {
                return "Command Value Error!";
            }
            HashSet<Database> databases = new HashSet<>(UserRemote.usersHashMap.values());

            for (Database vosDatabase : databases) {
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
                    } else {
                        Result = "Command Value Error";
                        break;
                    }
                }
            }
            if (Result.equals("")) {
                return "Do not find data";
            } else {
                return Result;
            }
        } else if (commandLength == 4) {
            String getWillGET = getCommand[1];
            String getFIND_DATABASE = getCommand[3];
            String getDataName = "";
            String getDataValue = "";
            try {
                getDataName = getWillGET.substring(getWillGET.indexOf("'") + 1, getWillGET.lastIndexOf("'"));
                getDataValue = getWillGET.substring(getWillGET.lastIndexOf(".") + 1, getWillGET.length());
                getDataValue = getDataValue.replace(" ", "");
            } catch (Exception exception) {
                return "Command Value Error!";
            }
            Database database = UserRemote.usersHashMap.get(getFIND_DATABASE);
            if (database != null) {
                Data data = database.getData(getDataName);
                if (data != null) {
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
                    } else {
                        return  "Command Value Error!";
                    }
                } else {
                    return "Can not find target data.";
                }
            }
            else
            {
                return "Can not find target database";
            }
        }else {
            return "Command Value Error!";
        }
    }
}
