package LinwinVOS;

import LinwinVOS.Mirror.LoadMirror;
import LinwinVOS.Mirror.MirrorHost;
import LinwinVOS.Users.UsersFileSystem;
import LinwinVOS.Users.logon;
import LogService.LogService;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LinwinVOS {
    public static HashMap<String,String> UsersNowPath = new HashMap<String,String>();
    public static HashMap<String, UsersFileSystem> FileSystem;
    public static List<UsersFileSystem> usersFileSystems = new ArrayList<UsersFileSystem>();
    public static HashMap<String, FileWriter> outPutMap = new HashMap<>();
    public static LogService logService;
    public static String DatabasePath;
    public static ExecutorService executorService = Executors.newFixedThreadPool(500);


    public LinwinVOS() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LinwinVOS.FileSystem = new HashMap<String, UsersFileSystem>();
            }
        });
        thread.start();
    }

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
            LinwinVOS.UsersNowPath.put(userName,"/user/");
        }
        //System.out.println();
        DataLoader.loadData();

        /**
         * Add the Mirror Host from Config File.
         *
         *
         */
        LoadMirror loadMirror = new LoadMirror();
        loadMirror.load();
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
