package LinwinVOS;

import LinwinVOS.Users.logon;
import java.util.HashMap;
import LinwinVOS.FileSystem.VMDirectory;

public class LinwinVOS {
    public static HashMap<String,String> UsersNowPath = new HashMap<String,String>();
    public static HashMap<String,VMDirectory> FileSystem = new HashMap<String,VMDirectory>();
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
    }
}
