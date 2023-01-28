package LinwinVOS;

import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.Users.logon;
import LogService.LogService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LinwinVOS {
    public static HashMap<String,String> UsersNowPath = new HashMap<String,String>();
    public static HashMap<String, UsersFileSystem> FileSystem = new HashMap<String, UsersFileSystem>();
    public static List<UsersFileSystem> usersFileSystems = new ArrayList<UsersFileSystem>();
    public static LogService logService;
    public static String DatabasePath;

    public void BootSystem() {
        logon.setPath = System.getProperty("user.dir");
        logon.LoadUsers();
        LinwinVOS.DatabasePath = System.getProperty("user.dir")+"/../../Data/";
        /**
         * Load all the users config and information.
         *
         *
         */
        for  (int i = 0 ; i < logon.UsersList.size() ; i++)
        {
            String[] userList = logon.UsersList.toArray(new String[logon.UsersList.size()]);
            String userName = userList[i];
            LinwinVOS.UsersNowPath.put(userName,"/");
        }
        //System.out.println();
        DataLoader.loadData();
    }
    public HashSet<UsersFileSystem> getUserFileSystem() {
        return new HashSet<UsersFileSystem>(LinwinVOS.FileSystem.values());
    }
    public int getDataSize() {
        int y = 0;
        for (UsersFileSystem usersFileSystem : LinwinVOS.FileSystem.values())
        {
            y = y + usersFileSystem.getSize();
        }
        return y;
    }
    public void setLogService(LogService logService) {
        LinwinVOS.logService = logService;
    }
}
