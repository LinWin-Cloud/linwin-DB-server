package LinwinVOS.runtime.lib;

import LinwinVOS.FileSystem.Data;
import LinwinVOS.FileSystem.VosDatabase;
import LinwinVOS.LinwinVOS;
import LinwinVOS.Mirror.MirrorHost;
import LinwinVOS.Users.UsersFileSystem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Index {
    public String IndexCommand(String user,String command) {
        StringBuffer stringBuffer = new StringBuffer("");
        UsersFileSystem usersFileSystem = LinwinVOS.FileSystem.get(user);
        try{
            String FindData = command.substring(command.indexOf("'")+1,command.lastIndexOf("'"));
            int e = command.lastIndexOf("'");
            int s = command.lastIndexOf("in ");
            if (s > e) {
                String DatabaseName = command.substring(s+3);
                VosDatabase vosDatabase = usersFileSystem.get(DatabaseName);
                if (vosDatabase != null) {
                    for (Data data : vosDatabase.getListData()) {
                        String name = data.getName();
                        int find = name.indexOf(FindData);
                        if (find != -1) {
                            stringBuffer.append(name);
                            stringBuffer.append("\n");
                        }
                    }
                    return stringBuffer.toString();
                }else{
                    Ls ls = new Ls();
                    String[] split = ls.ls(user,"ls "+DatabaseName).split("\n");
                    HashSet<String> hashSet = new HashSet<>();
                    hashSet.addAll(Arrays.asList(split));

                    for (String i : hashSet) {
                        int f = i.indexOf(FindData);
                        if (f != -1) {
                            stringBuffer.append(i);
                            stringBuffer.append("\n");
                        }
                    }
                    return stringBuffer.toString();
                }
            }else {
                return "Command syntax error!";
            }
        }catch (Exception exception) {
            return "Command syntax error!";
        }
    }
}
